package top.infra.jackson2.tests;

import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.Jackson2Properties;
import top.infra.jackson2.JaxbAnnotationJackson2Customizer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaxbAnnotationJackson2TestApplication.class)
public class JaxbAnnotationJackson2CustomizerTest {

    @Autowired(required = false)
    private JaxbAnnotationJackson2Customizer customizer;

    @Autowired
    private Jackson2Properties jackson2Properties;

    @Test
    public void smokeTest() {
        assertNotNull(this.customizer);

        final ObjectMapper objectMapper = new ObjectMapper();
        this.customizer.customize(this.jackson2Properties, objectMapper);
    }
}
