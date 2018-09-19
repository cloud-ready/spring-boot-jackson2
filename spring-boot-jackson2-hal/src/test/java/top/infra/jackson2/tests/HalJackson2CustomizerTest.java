package top.infra.jackson2.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import top.infra.jackson2.HalJackson2Customizer;
import top.infra.jackson2.Jackson2Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HalJackson2TestApplication.class)
public class HalJackson2CustomizerTest {

    @Autowired
    private HalJackson2Customizer customizer;

    @Autowired
    private Jackson2Properties jackson2Properties;

    @Test
    public void smokeTest() {
        assertNotNull(this.customizer);

        final ObjectMapper objectMapper = new ObjectMapper();
        this.customizer.customize(this.jackson2Properties, objectMapper);
        final Optional<Module> moduleOptional = this.customizer.jackson2HalModule();
        assertTrue(moduleOptional.isPresent());
        assertTrue(this.customizer.isAlreadyRegisteredIn(objectMapper, moduleOptional.get().getClass()));
    }
}
