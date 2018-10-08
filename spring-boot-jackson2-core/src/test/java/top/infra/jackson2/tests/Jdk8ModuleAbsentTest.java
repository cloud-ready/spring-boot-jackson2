package top.infra.jackson2.tests;

import static org.junit.Assert.assertNull;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.Jdk8ModuleCustomizer;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "com.fasterxml.jackson.datatype.jdk8.Jdk8Module",
    },
    runner = SpringRunner.class)
@RunWith(ExcludeClassJunitRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
@Slf4j
public class Jdk8ModuleAbsentTest {

    @Autowired(required = false)
    private Jdk8ModuleCustomizer jdk8ModuleCustomizer;

    @Test
    @SneakyThrows
    public void testJackson2Absent() {
        assertNull(this.jdk8ModuleCustomizer);
    }
}
