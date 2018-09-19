package top.infra.jackson2;

import static java.lang.Boolean.FALSE;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Before;
import org.junit.Test;

public class DefaultJackson2CustomizerTest {

    private DefaultJackson2Customizer defaultJackson2Customizer;

    @Before
    public void setUp() {
        this.defaultJackson2Customizer = new DefaultJackson2Customizer();
    }

    @Test
    public void testDefaultFeatures() {
        final Jackson2Properties jackson2Properties = new Jackson2Properties();
        assertFalse(this.defaultJackson2Customizer.feature(jackson2Properties, SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, FALSE));
    }
}
