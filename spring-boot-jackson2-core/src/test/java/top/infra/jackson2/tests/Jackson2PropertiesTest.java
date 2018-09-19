package top.infra.jackson2.tests;

import static org.junit.Assert.assertTrue;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.Jackson2Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
@Slf4j
public class Jackson2PropertiesTest {

    static {
        System.setProperty("spring.jackson.jaxb.enabled", "true");
    }

    @Autowired
    private Jackson2Properties jackson2Properties;

    @Test
    @SneakyThrows
    public void testJaxbEnabled() {
        assertTrue(this.jackson2Properties.getJaxb().getEnabled());
    }
}
