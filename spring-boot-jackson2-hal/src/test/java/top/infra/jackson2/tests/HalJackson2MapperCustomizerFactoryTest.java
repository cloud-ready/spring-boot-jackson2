package top.infra.jackson2.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Optional;

import top.infra.jackson2.HalJackson2MapperCustomizerFactory;
import top.infra.jackson2.Jackson2MapperCustomizer;

public class HalJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final HalJackson2MapperCustomizerFactory factory = new HalJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertTrue(mapperCustomizer.isPresent());
    }
}
