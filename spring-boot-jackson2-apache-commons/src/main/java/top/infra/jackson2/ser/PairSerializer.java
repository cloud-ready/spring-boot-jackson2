package top.infra.jackson2.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Map;

public class PairSerializer extends StdSerializer<Pair<?, ?>> implements ContextualSerializer {

    /**
     * property being serialized with this instance
     */
    protected final BeanProperty _property;

    /**
     * Whether static types should be used for serialization of values
     * or not (if not, dynamic runtime type is used)
     */
    protected final boolean _staticTyping;

    protected final JavaType _entryType, _keyType, _valueType;

    /**
     * Key serializer to use, if it can be statically determined
     */
    protected JsonSerializer<Object> _ks;

    /**
     * Value serializer to use, if it can be statically determined
     */
    protected JsonSerializer<Object> _vs;

    /**
     * keyTypeSerializer, Type identifier serializer used for keys, if any.
     */
    protected final TypeSerializer _kts;
    /**
     * valueTypeSerializer, Type identifier serializer used for values, if any.
     */
    protected final TypeSerializer _vts;

    /**
     * dynamicKeySerializers, If key type can not be statically determined, mapping from
     * runtime key types to serializers are stored in this object.
     */
    protected PropertySerializerMap _dks;
    /**
     * dynamicValueSerializers, If value type can not be statically determined, mapping from
     * runtime value types to serializers are stored in this object.
     */
    protected PropertySerializerMap _dvs;

    public PairSerializer(
        final JavaType type, final JavaType keyType, final JavaType valueType,
        final boolean staticTyping,
        final TypeSerializer kts, final TypeSerializer vts,
        final BeanProperty property
    ) {
        super(type);
        this._entryType = type;
        this._keyType = keyType;
        this._valueType = valueType;
        this._staticTyping = staticTyping;
        this._kts = kts;
        this._vts = vts;
        this._dks = PropertySerializerMap.emptyForProperties();
        this._dvs = PropertySerializerMap.emptyForProperties();
        this._property = property;
    }

    @SuppressWarnings("unchecked")
    protected PairSerializer(
        final PairSerializer src,
        final BeanProperty property,
        final TypeSerializer vts,
        final JsonSerializer<?> keySer,
        final JsonSerializer<?> valueSer
    ) {
        super(Map.class, false);
        this._entryType = src._entryType;
        this._keyType = src._keyType;
        this._valueType = src._valueType;
        this._staticTyping = src._staticTyping;
        this._kts = src._kts;
        this._vts = src._vts;
        this._ks = (JsonSerializer<Object>) keySer;
        this._vs = (JsonSerializer<Object>) valueSer;
        this._dks = src._dks;
        this._dvs = src._dvs;
        this._property = src._property;
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider provider, final BeanProperty property) throws JsonMappingException {
        JsonSerializer<?> vs = null;
        JsonSerializer<?> ks = null;

        final AnnotationIntrospector intr = provider.getAnnotationIntrospector();
        final AnnotatedMember propertyAcc = (property == null) ? null : property.getMember();

        // First: if we have a property, may have property-annotation overrides
        if (propertyAcc != null && intr != null) {
            Object serDef = intr.findKeySerializer(propertyAcc);
            if (serDef != null) {
                ks = provider.serializerInstance(propertyAcc, serDef);
            }
            serDef = intr.findContentSerializer(propertyAcc);
            if (serDef != null) {
                vs = provider.serializerInstance(propertyAcc, serDef);
            }
        }

        if (vs == null) {
            vs = this._vs;
        }
        // [databind#124]: May have a content converter
        vs = findConvertingContentSerializer(provider, property, vs);
        if (vs == null) {
            // 30-Sep-2012, tatu: One more thing -- if explicit content type is annotated,
            //   we can consider it a static case as well.
            // 20-Aug-2013, tatu: Need to avoid trying to access serializer for java.lang.Object tho
            if (this._staticTyping && !this._valueType.isJavaLangObject()) {
                vs = provider.findValueSerializer(this._valueType, property);
            }
        } else {
            vs = provider.handleSecondaryContextualization(vs, property);
        }

        if (ks == null) {
            ks = this._ks;
        }
        ks = findConvertingContentSerializer(provider, property, ks);
        if (ks == null) {
            if (this._staticTyping && !this._valueType.isJavaLangObject()) {
                ks = provider.findValueSerializer(this._valueType, property);
            }
        } else {
            ks = provider.handleSecondaryContextualization(ks, property);
        }

        PairSerializer mser = withResolved(property, ks, vs);
        // but note: no filtering, ignored entries or sorting (unlike Maps)
        return mser;
    }

    public PairSerializer withResolved(BeanProperty property, JsonSerializer<?> ks, JsonSerializer<?> vs) {
        return new PairSerializer(this, property, this._vts, ks, vs);
    }

    @Override
    public void serialize(
        final Pair<?, ?> value, final JsonGenerator gen, final SerializerProvider provider
    ) throws IOException {
        gen.writeStartObject(value);
        if (this._vs != null) {
            //serializeUsing(value, gen, provider, _vs);
        } else {
            serializeDynamic(value, gen, provider);
        }
        gen.writeEndObject();
    }

    protected void serializeDynamic(
        final Pair<?, ?> value, final JsonGenerator jgen, final SerializerProvider provider
    ) throws IOException {
        //final JsonSerializer<Object> keySerializer = this._ks;
        //final boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
        final TypeSerializer vts = this._vts;
        final TypeSerializer kts = this._kts;

        PropertySerializerMap dvs = this._dvs;
        PropertySerializerMap dks = this._dks;

        Object valueElem = value.getValue();
        Object keyElem = value.getKey();

        jgen.writeFieldName("left");
        if (keyElem == null) {
            provider.defaultSerializeNull(jgen);
        } else {
            Class<?> cc = keyElem.getClass();
            JsonSerializer<Object> ser = dks.serializerFor(cc);
            if (ser == null) {
                if (this._valueType.hasGenericTypes()) {
                    ser = _findAndAddDynamicKeySerializer(dks, provider.constructSpecializedType(this._keyType, cc), provider);
                } else {
                    ser = _findAndAddDynamicKeySerializer(dks, cc, provider);
                }
                dks = this._dks;
            }
            try {
                if (kts == null) {
                    ser.serialize(keyElem, jgen, provider);
                } else {
                    ser.serializeWithType(keyElem, jgen, provider, kts);
                }
            } catch (final Exception e) {
                // [JACKSON-55] Need to add reference information
                String keyDesc = "" + keyElem;
                wrapAndThrow(provider, e, value, keyDesc);
            }
        }

        // And then value
        jgen.writeFieldName("right");
        if (valueElem == null) {
            provider.defaultSerializeNull(jgen);
        } else {
            Class<?> cc = valueElem.getClass();
            JsonSerializer<Object> ser = dvs.serializerFor(cc);
            if (ser == null) {
                if (this._valueType.hasGenericTypes()) {
                    ser = _findAndAddDynamicValueSerializer(dvs, provider.constructSpecializedType(this._valueType, cc), provider);
                } else {
                    ser = _findAndAddDynamicValueSerializer(dvs, cc, provider);
                }
                dvs = this._dvs;
            }
            try {
                if (vts == null) {
                    ser.serialize(valueElem, jgen, provider);
                } else {
                    ser.serializeWithType(valueElem, jgen, provider, vts);
                }
            } catch (final Exception e) {
                // [JACKSON-55] Need to add reference information
                String keyDesc = "" + keyElem;
                wrapAndThrow(provider, e, value, keyDesc);
            }
        }
    }


    protected final JsonSerializer<Object> _findAndAddDynamicKeySerializer(
        final PropertySerializerMap map,
        final Class<?> type,
        final SerializerProvider provider
    ) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
        if (map != result.map) {
            this._dks = result.map;
        }
        return result.serializer;
    }

    protected final JsonSerializer<Object> _findAndAddDynamicKeySerializer(
        final PropertySerializerMap map,
        final JavaType type,
        final SerializerProvider provider
    ) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
        if (map != result.map) {
            this._dks = result.map;
        }
        return result.serializer;
    }

    protected final JsonSerializer<Object> _findAndAddDynamicValueSerializer(
        final PropertySerializerMap map,
        final Class<?> type,
        final SerializerProvider provider
    ) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
        if (map != result.map) {
            this._dvs = result.map;
        }
        return result.serializer;
    }

    protected final JsonSerializer<Object> _findAndAddDynamicValueSerializer(
        final PropertySerializerMap map,
        final JavaType type,
        final SerializerProvider provider
    ) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
        if (map != result.map) {
            this._dvs = result.map;
        }
        return result.serializer;
    }
}
