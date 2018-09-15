package top.infra.jackson2;

import com.google.common.collect.Lists;

import java.util.Optional;

public class JaxbAnnotationJackson2MapperCustomizerFactory implements Jackson2MapperCustomizerFactory {

    static final String CLASS_JAXB_ANNOTATION_INTROSPECTOR = "com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector";
    static final String CLASS_JAXB_ANNOTATION_MODULE = "com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule";
    static final String CLASS_JAXB_ANNOTATION_MODULE_PRIORITY = CLASS_JAXB_ANNOTATION_MODULE + "$Priority";

    @Override
    public Optional<Jackson2MapperCustomizer> getObject() {
        return this.newInstanceIfPresent(
            "top.infra.jackson2.JaxbAnnotationJackson2Customizer",
            Lists.newArrayList(
                CLASS_JAXB_ANNOTATION_INTROSPECTOR,
                CLASS_JAXB_ANNOTATION_MODULE,
                CLASS_JAXB_ANNOTATION_MODULE_PRIORITY,
                CLASS_JACKSON2_OBJECT_MAPPER_BUILDER
            ));
    }
}
