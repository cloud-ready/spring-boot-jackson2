package top.infra.jackson2.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.DefaultJackson2Customizer;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "com.google.common.collect.Multimap",
    },
    runner = SpringRunner.class)
@RunWith(ExcludeClassJunitRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
@Slf4j
public class GuavaAbsentTest {

    @Autowired
    private DefaultJackson2Customizer defaultJackson2Customizer;

    @Test
    @SneakyThrows
    public void testJackson2Absent() {
        assertNotNull(this.defaultJackson2Customizer);
        assertFalse(this.defaultJackson2Customizer.isGuavaPresent(Thread.currentThread().getContextClassLoader()));
        assertFalse(this.defaultJackson2Customizer.isGuavaPresent(getClass().getClassLoader()));
    }
}
