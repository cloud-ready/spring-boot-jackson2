package top.infra.jackson2.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Optional;

import top.infra.jackson2.DefaultJackson2MapperCustomizerFactory;
import top.infra.jackson2.Jackson2MapperCustomizer;

public class DefaultJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final DefaultJackson2MapperCustomizerFactory factory = new DefaultJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertTrue(mapperCustomizer.isPresent());
    }
}
