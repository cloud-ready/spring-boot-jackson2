package top.infra.jackson2.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Optional;

import top.infra.jackson2.Jackson2MapperCustomizer;
import top.infra.jackson2.JodaTimeJackson2MapperCustomizerFactory;

public class JodaTimeJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final JodaTimeJackson2MapperCustomizerFactory factory = new JodaTimeJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertTrue(mapperCustomizer.isPresent());
    }
}
