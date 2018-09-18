package top.infra.jackson2.tests;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import top.infra.jackson2.CommonsLangJackson2MapperCustomizerFactory;
import top.infra.jackson2.Jackson2MapperCustomizer;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "org.apache.commons.lang3.tuple.MutablePair",
        "org.apache.commons.lang3.tuple.Pair",
    })
@RunWith(ExcludeClassJunitRunner.class)
public class CommonsLangAbsentCommonsLangJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final CommonsLangJackson2MapperCustomizerFactory factory = new CommonsLangJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertFalse(mapperCustomizer.isPresent());
    }
}
