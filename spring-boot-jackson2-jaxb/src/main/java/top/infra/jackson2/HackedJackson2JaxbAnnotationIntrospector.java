package top.infra.jackson2;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * Ignore @XmlJavaTypeAdapter.
 * see: https://github.com/FasterXML/jackson-dataformat-xml/issues/37
 */
public class HackedJackson2JaxbAnnotationIntrospector extends JaxbAnnotationIntrospector {

    public HackedJackson2JaxbAnnotationIntrospector() {
        super(TypeFactory.defaultInstance());
    }

    @Override
    public Object findSerializationConverter(final Annotated annotated) {
        return null;
    }

    @Override
    public Object findDeserializationConverter(final Annotated annotated) {
        return null;
    }
}
