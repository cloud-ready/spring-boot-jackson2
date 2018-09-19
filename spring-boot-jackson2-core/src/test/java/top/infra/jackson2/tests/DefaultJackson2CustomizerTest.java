package top.infra.jackson2.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.DefaultJackson2Customizer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
public class DefaultJackson2CustomizerTest {

    @Autowired
    private DefaultJackson2Customizer defaultJackson2Customizer;

    @Test
    public void smokeTest() {

    }

    @Test
    public void testJackson2MapperCustomizer() {
        assertFalse(this.defaultJackson2Customizer.isXmlMapper(new ObjectMapper()));
        assertTrue(this.defaultJackson2Customizer.isXmlMapper(new XmlMapper()));
    }

    @Test
    public void testJackson2BuilderCustomizer() {
        assertFalse(this.defaultJackson2Customizer.isXmlMapper(new Jackson2ObjectMapperBuilder().createXmlMapper(false)));
        assertTrue(this.defaultJackson2Customizer.isXmlMapper(new Jackson2ObjectMapperBuilder().createXmlMapper(true)));
    }
}
