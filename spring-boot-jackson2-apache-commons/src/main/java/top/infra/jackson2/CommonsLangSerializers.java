package top.infra.jackson2;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BasicSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;

import top.infra.jackson2.ser.PairSerializer;

/**
 * see: {@link BasicSerializerFactory}
 * see: {@link BeanSerializerFactory}
 * see: com.fasterxml.jackson.datatype.guava.GuavaSerializers
 */
public class CommonsLangSerializers extends Serializers.Base {

    @Override
    public JsonSerializer<?> findReferenceSerializer(
        SerializationConfig config,
        ReferenceType refType,
        BeanDescription beanDesc,
        TypeSerializer contentTypeSerializer,
        JsonSerializer<Object> contentValueSerializer
    ) {
        return super.findReferenceSerializer(config, refType, beanDesc, contentTypeSerializer, contentValueSerializer);
    }

    @Override
    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
        // see: BasicSerializerFactory.buildContainerSerializer
        if (Pair.class.isAssignableFrom(type.getRawClass())) {
            // Let's see what we can learn about element/content/value type, type serializer for it:
            JavaType lType = type.containedTypeOrUnknown(0);
            JavaType rType = type.containedTypeOrUnknown(1);
            boolean staticTyping = false;
            final TypeSerializer kts = createTypeSerializer(config, lType);
            final TypeSerializer vts = createTypeSerializer(config, rType);
            return new PairSerializer(type, lType, rType, staticTyping, kts, vts, null);
        }
        return super.findSerializer(config, type, beanDesc);
    }

    @Override
    public JsonSerializer<?> findMapLikeSerializer(
        SerializationConfig config,
        MapLikeType type,
        BeanDescription beanDesc,
        JsonSerializer<Object> keySerializer,
        TypeSerializer elementTypeSerializer,
        JsonSerializer<Object> elementValueSerializer
    ) {
        return super.findMapLikeSerializer(config, type, beanDesc, keySerializer, elementTypeSerializer, elementValueSerializer);
    }

    /**
     * see: {@link BasicSerializerFactory#createTypeSerializer(SerializationConfig, JavaType)}
     */
    public TypeSerializer createTypeSerializer(final SerializationConfig config, final JavaType baseType) {
        if (baseType == null) {
            return null;
        }
        final BeanDescription bean = config.introspectClassAnnotations(baseType.getRawClass());
        final AnnotatedClass ac = bean.getClassInfo();
        final AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findTypeResolver(config, ac, baseType);
        /* Ok: if there is no explicit type info handler, we may want to
         * use a default. If so, config object knows what to use.
         */
        Collection<NamedType> subtypes = null;
        if (b == null) {
            b = config.getDefaultTyper(baseType);
        } else {
            subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByClass(config, ac);
        }
        if (b == null) {
            return null;
        }
        // 10-Jun-2015, tatu: Since not created for Bean Property, no need for post-processing
        //    wrt EXTERNAL_PROPERTY
        return b.buildTypeSerializer(config, baseType, subtypes);
    }
}
