package top.infra.jackson2.tests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.JodaTimeJackson2Customizer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JodaTimeJackson2TestApplication.class)
public class JodaTimeJackson2CustomizerTest {

    @Autowired(required = false)
    private JodaTimeJackson2Customizer customizer;

    @Test
    public void smokeTest() {
        assertNotNull(this.customizer);
    }
}
