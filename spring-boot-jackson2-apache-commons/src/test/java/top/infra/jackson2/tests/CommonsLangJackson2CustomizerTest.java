package top.infra.jackson2.tests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.CommonsLangJackson2Customizer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonsLangJackson2TestApplication.class)
public class CommonsLangJackson2CustomizerTest {

    @Autowired(required = false)
    private CommonsLangJackson2Customizer customizer;

    @Test
    public void smokeTest() {
        assertNotNull(this.customizer);
    }
}
