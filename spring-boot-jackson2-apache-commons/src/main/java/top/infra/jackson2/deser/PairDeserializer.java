package top.infra.jackson2.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * see: {@link com.fasterxml.jackson.databind.deser.std.MapEntryDeserializer}.
 */
public class PairDeserializer extends StdDeserializer<Pair<Object, Object>> implements ContextualDeserializer {

    private static final long serialVersionUID = 1;

    // // Configuration: typing, deserializers

    protected final JavaType _type;

    /**
     * Key deserializer to use; either passed via constructor
     * (when indicated by annotations), or resolved when
     * {@link #createContextual} is called;
     */
    protected final JsonDeserializer<Object> _keyDeserializer;
    /**
     * If key instances have polymorphic type information, this
     * is the type deserializer that can handle it
     */
    protected final TypeDeserializer _keyTypeDeserializer;

    /**
     * Value deserializer.
     */
    protected final JsonDeserializer<Object> _valueDeserializer;

    /**
     * If value instances have polymorphic type information, this
     * is the type deserializer that can handle it
     */
    protected final TypeDeserializer _valueTypeDeserializer;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    public PairDeserializer(
        final JavaType type,
        final JsonDeserializer<Object> keyDeser,
        final TypeDeserializer keyTypeDeserializer,
        final JsonDeserializer<Object> valueDeser,
        final TypeDeserializer valueTypeDeser
    ) {
        super(type);
        if (type.containedTypeCount() != 2) { // sanity check
            throw new IllegalArgumentException("Missing generic type information for " + type);
        }
        this._type = type;
        this._keyDeserializer = keyDeser;
        this._keyTypeDeserializer = keyTypeDeserializer;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
    }

    /**
     * Copy-constructor that can be used by sub-classes to allow
     * copy-on-write styling copying of settings of an existing instance.
     */
    protected PairDeserializer(final PairDeserializer src) {
        super(src._type);
        this._type = src._type;
        this._keyDeserializer = src._keyDeserializer;
        this._keyTypeDeserializer = src._keyTypeDeserializer;
        this._valueDeserializer = src._valueDeserializer;
        this._valueTypeDeserializer = src._valueTypeDeserializer;
    }

    protected PairDeserializer(
        final PairDeserializer src,
        final JsonDeserializer<Object> keyDeser,
        final TypeDeserializer keyTypeDeserializer,
        final JsonDeserializer<Object> valueDeser,
        final TypeDeserializer valueTypeDeser
    ) {
        super(src._type);
        this._type = src._type;
        this._keyDeserializer = keyDeser;
        this._keyTypeDeserializer = keyTypeDeserializer;
        this._valueDeserializer = valueDeser;
        this._valueTypeDeserializer = valueTypeDeser;
    }

    /**
     * Fluent factory method used to create a copy with slightly
     * different settings. When sub-classing, MUST be overridden.
     */
    @SuppressWarnings("unchecked")
    protected PairDeserializer withResolved(
        final TypeDeserializer keyTypeDeserializer, final JsonDeserializer<?> keyDeser,
        final TypeDeserializer valueTypeDeser, final JsonDeserializer<?> valueDeser
    ) {
        if ((this._keyDeserializer == keyDeser) && (this._valueDeserializer == valueDeser)
            && (this._valueTypeDeserializer == valueTypeDeser)) {
            return this;
        }
        return new PairDeserializer(
            this,
            (JsonDeserializer<Object>) keyDeser,
            keyTypeDeserializer,
            (JsonDeserializer<Object>) valueDeser,
            valueTypeDeser);
    }

    /*
    /**********************************************************
    /* Validation, post-processing (ResolvableDeserializer)
    /**********************************************************
     */

    /**
     * Method called to finalize setup of this deserializer,
     * when it is known for which property deserializer is needed for.
     */
    @Override
    public JsonDeserializer<?> createContextual(
        final DeserializationContext ctxt,
        final BeanProperty property
    ) throws JsonMappingException {
        JsonDeserializer<?> kd = this._keyDeserializer;
        kd = findConvertingContentDeserializer(ctxt, property, kd);
        JavaType keyType = this._type.containedType(0);
        if (kd == null) {
            kd = ctxt.findContextualValueDeserializer(keyType, property);
        } else { // if directly assigned, probably not yet contextual, so:
            kd = ctxt.handleSecondaryContextualization(kd, property, keyType);
        }
        TypeDeserializer ktd = this._keyTypeDeserializer;
        if (ktd != null) {
            ktd = ktd.forProperty(property);
        }

        JsonDeserializer<?> vd = this._valueDeserializer;
        vd = findConvertingContentDeserializer(ctxt, property, vd);
        JavaType valueType = this._type.containedType(1);
        if (vd == null) {
            vd = ctxt.findContextualValueDeserializer(valueType, property);
        } else { // if directly assigned, probably not yet contextual, so:
            vd = ctxt.handleSecondaryContextualization(vd, property, valueType);
        }
        TypeDeserializer vtd = this._valueTypeDeserializer;
        if (vtd != null) {
            vtd = vtd.forProperty(property);
        }
        return withResolved(ktd, kd, vtd, vd);
    }

    /*
    /**********************************************************
    /* JsonDeserializer API
    /**********************************************************
     */

    @SuppressWarnings("unchecked")
    @Override
    public Pair<Object, Object> deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        // Ok: must point to START_OBJECT, FIELD_NAME or END_OBJECT
        JsonToken t = p.getCurrentToken();
        if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME && t != JsonToken.END_OBJECT) {
            // String may be ok however:
            // slightly redundant (since String was passed above), but
            return _deserializeFromEmpty(p, ctxt);
        }
        if (t == JsonToken.START_OBJECT) {
            t = p.nextToken();
        }
        if (t != JsonToken.FIELD_NAME) {
            if (t == JsonToken.END_OBJECT) {
                ctxt.reportMappingException("Can not deserialize a Pair out of empty JSON Object");
                return null;
            }
            return (Pair<Object, Object>) ctxt.handleUnexpectedToken(handledType(), p);
        }

        final JsonDeserializer<Object> keyDes = this._keyDeserializer;
        final TypeDeserializer keyTypeDeser = this._keyTypeDeserializer;
        final JsonDeserializer<Object> valueDes = this._valueDeserializer;
        final TypeDeserializer valueTypeDeser = this._valueTypeDeserializer;

        final String keyStr = p.getCurrentName();
        Object key = null;
        //Object key = keyDes.deserializeKey(keyStr, ctxt);
        t = p.nextToken();
        try {
            // Note: must handle null explicitly here; value deserializers won't
            if (t == JsonToken.VALUE_NULL) {
                key = keyDes.getNullValue(ctxt);
            } else if (valueTypeDeser == null) {
                key = keyDes.deserialize(p, ctxt);
            } else {
                key = keyDes.deserializeWithType(p, ctxt, keyTypeDeser);
            }
        } catch (Exception e) {
            wrapAndThrow(e, Pair.class, keyStr);
        }

        t = p.nextToken();
        final String valueStr = p.getCurrentName();
        Object value = null;
        // And then the value...
        t = p.nextToken();
        try {
            // Note: must handle null explicitly here; value deserializers won't
            if (t == JsonToken.VALUE_NULL) {
                value = valueDes.getNullValue(ctxt);
            } else if (valueTypeDeser == null) {
                value = valueDes.deserialize(p, ctxt);
            } else {
                value = valueDes.deserializeWithType(p, ctxt, valueTypeDeser);
            }
        } catch (Exception e) {
            wrapAndThrow(e, Pair.class, valueStr);
        }

        // Close, but also verify that we reached the END_OBJECT
        t = p.nextToken();
        if (t != JsonToken.END_OBJECT) {
            if (t == JsonToken.FIELD_NAME) { // most likely
                ctxt.reportMappingException("Problem binding JSON into Pair: more than one entry in JSON (second field: '" + p.getCurrentName() + "')");
            } else {
                // how would this occur?
                ctxt.reportMappingException("Problem binding JSON into Pair: unexpected content after JSON Object entry: " + t);
            }
            return null;
        }
        return new ImmutablePair<>(key, value);
    }

    @Override
    public Pair<Object, Object> deserialize(
        final JsonParser p, final DeserializationContext ctxt, final Pair<Object, Object> result
    ) throws IOException {
        throw new IllegalStateException("Can not update Pair values");
    }

    @Override
    public Object deserializeWithType(
        final JsonParser p,
        final DeserializationContext ctxt,
        final TypeDeserializer typeDeserializer
    ) throws IOException, JsonProcessingException {
        // In future could check current token... for now this should be enough:
        return typeDeserializer.deserializeTypedFromObject(p, ctxt);
    }

    /*
    /**********************************************************
    /* Other public accessors
    /**********************************************************
     */

    @Override
    public JavaType getValueType() {
        return this._type;
    }

    /*
    /**********************************************************
    /* Shared methods for sub-classes (from ContainerDeserializerBase)
    /**********************************************************
     */

    /**
     * Helper method called by various Map(-like) deserializers.
     */
    protected void wrapAndThrow(Throwable t, final Object ref, String key) throws IOException {
        // to handle StackOverflow:
        while (t instanceof InvocationTargetException && t.getCause() != null) {
            t = t.getCause();
        }
        // Errors and "plain" IOExceptions to be passed as is
        if (t instanceof Error) {
            throw (Error) t;
        }
        // ... except for mapping exceptions
        if (t instanceof IOException && !(t instanceof JsonMappingException)) {
            throw (IOException) t;
        }
        // for [databind#1141]
        if (key == null) {
            key = "N/A";
        }
        throw JsonMappingException.wrapWithPath(t, ref, key);
    }
}
