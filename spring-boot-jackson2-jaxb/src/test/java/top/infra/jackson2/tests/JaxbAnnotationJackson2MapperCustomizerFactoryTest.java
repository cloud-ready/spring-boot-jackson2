package top.infra.jackson2.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Optional;

import top.infra.jackson2.Jackson2MapperCustomizer;
import top.infra.jackson2.JaxbAnnotationJackson2MapperCustomizerFactory;

public class JaxbAnnotationJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final JaxbAnnotationJackson2MapperCustomizerFactory factory = new JaxbAnnotationJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertTrue(mapperCustomizer.isPresent());
    }
}
