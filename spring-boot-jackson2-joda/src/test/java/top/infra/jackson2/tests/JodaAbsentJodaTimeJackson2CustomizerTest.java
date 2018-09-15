package top.infra.jackson2.tests;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.JodaTimeJackson2Customizer;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat",
        "com.fasterxml.jackson.datatype.joda.JodaModule",
    },
    runner = SpringRunner.class)
@RunWith(ExcludeClassJunitRunner.class)
@SpringBootTest(classes = JodaTimeJackson2TestApplication.class)
public class JodaAbsentJodaTimeJackson2CustomizerTest {

    @Autowired(required = false)
    private JodaTimeJackson2Customizer customizer;

    @Test
    public void smokeTest() {
        assertNull(this.customizer);
    }
}
