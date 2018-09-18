package top.infra.jackson2.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Optional;

import top.infra.jackson2.CommonsLangJackson2MapperCustomizerFactory;
import top.infra.jackson2.Jackson2MapperCustomizer;

public class CommonsLangJackson2MapperCustomizerFactoryTest {

    @Test
    public void testGetObject() {
        final CommonsLangJackson2MapperCustomizerFactory factory = new CommonsLangJackson2MapperCustomizerFactory();
        Optional<Jackson2MapperCustomizer> mapperCustomizer = factory.getObject();
        assertTrue(mapperCustomizer.isPresent());
    }
}
