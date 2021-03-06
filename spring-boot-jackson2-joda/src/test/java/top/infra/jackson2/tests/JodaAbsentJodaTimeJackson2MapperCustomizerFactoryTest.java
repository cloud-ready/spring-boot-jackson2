package top.infra.jackson2.tests;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import top.infra.jackson2.Jackson2MapperCustomizer;
import top.infra.jackson2.JodaTimeJackson2MapperCustomizerFactory;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat",
        "com.fasterxml.jackson.datatype.joda.JodaModule",
    })
@RunWith(ExcludeClassJunitRunner.class)
public class JodaAbsentJodaTimeJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final JodaTimeJackson2MapperCustomizerFactory factory = new JodaTimeJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertFalse(mapperCustomizer.isPresent());
    }
}
