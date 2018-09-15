package top.infra.jackson2.tests;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import top.infra.jackson2.HalJackson2MapperCustomizerFactory;
import top.infra.jackson2.Jackson2MapperCustomizer;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "org.springframework.hateoas.hal.Jackson2HalModule",
    })
@RunWith(ExcludeClassJunitRunner.class)
public class HalAbsentHalJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final HalJackson2MapperCustomizerFactory factory = new HalJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertFalse(mapperCustomizer.isPresent());
    }
}
