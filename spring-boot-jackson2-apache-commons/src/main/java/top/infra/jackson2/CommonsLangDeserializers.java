package top.infra.jackson2;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.apache.commons.lang3.tuple.Pair;

import top.infra.jackson2.deser.PairDeserializer;

/**
 * {@link com.fasterxml.jackson.databind.deser.BasicDeserializerFactory#findDefaultDeserializer(DeserializationContext, JavaType, BeanDescription)}
 * {@link com.fasterxml.jackson.databind.deser.BeanDeserializerFactory}
 * com.fasterxml.jackson.datatype.guava.GuavaDeserializers
 */
public class CommonsLangDeserializers extends Deserializers.Base {

    private final DeserializerFactory deserializerFactory;

    public CommonsLangDeserializers(final DeserializerFactory deserializerFactory) {
        this.deserializerFactory = deserializerFactory;
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(
        final JavaType type, final DeserializationConfig config, final BeanDescription beanDesc
    ) throws JsonMappingException {
        if (type.hasRawClass(Pair.class)) {
            JavaType kt = type.containedType(0);
            if (kt == null) {
                kt = TypeFactory.unknownType();
            }
            TypeDeserializer kts = (TypeDeserializer) kt.getTypeHandler();
            if (kts == null) {
                kts = this.deserializerFactory.findTypeDeserializer(config, kt);
            }

            JavaType vt = type.containedType(1);
            if (vt == null) {
                vt = TypeFactory.unknownType();
            }
            TypeDeserializer vts = (TypeDeserializer) vt.getTypeHandler();
            if (vts == null) {
                vts = this.deserializerFactory.findTypeDeserializer(config, vt);
            }
            JsonDeserializer<Object> valueDeser = vt.getValueHandler();
            JsonDeserializer<Object> keyDes = kt.getValueHandler();
            return new PairDeserializer(type, keyDes, kts, valueDeser, vts);
        }
        return null;
    }
}
