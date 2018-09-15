package top.infra.jackson2.tests;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.JaxbAnnotationJackson2Customizer;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule",
    },
    runner = SpringRunner.class)
@RunWith(ExcludeClassJunitRunner.class)
@SpringBootTest(classes = JaxbAnnotationJackson2TestApplication.class)
public class JaxbAbsentJaxbAnnotationJackson2CustomizerTest {

    @Autowired(required = false)
    private JaxbAnnotationJackson2Customizer customizer;

    @Test
    public void smokeTest() {
        assertNull(this.customizer);
    }
}
