package top.infra.jackson2.tests;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import top.infra.jackson2.Jackson2MapperCustomizer;
import top.infra.jackson2.JaxbAnnotationJackson2MapperCustomizerFactory;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule",
    })
@RunWith(ExcludeClassJunitRunner.class)
public class JaxbAbsentJaxbAnnotationJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final JaxbAnnotationJackson2MapperCustomizerFactory factory = new JaxbAnnotationJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertFalse(mapperCustomizer.isPresent());
    }
}
