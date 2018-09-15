package top.infra.test;

import static org.junit.Assert.assertEquals;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.test.classloader.ClassUtils;
import top.infra.test.classloader.exclude.ExcludeClassJunitRunner;
import top.infra.test.classloader.exclude.ExcludeClasses;

@ExcludeClasses(
    excludesClasses = {
        "top.infra.test.GuavaAndCommonUser",
    },
    runner = SpringRunner.class
)
@RunWith(ExcludeClassJunitRunner.class)
@SpringBootTest(classes = ExcludeClassJunitRunnerTest.ExcludeClassJunitRunnerTestApplication.class)
@Slf4j
public class ExcludeClassJunitRunnerTest {

    @SpringBootApplication
    public static class ExcludeClassJunitRunnerTestApplication {

        public static void main(final String... args) {
            new SpringApplicationBuilder(ExcludeClassJunitRunnerTestApplication.class).web(true).run(args);
        }
    }

    @Test(expected = NoClassDefFoundError.class)
    public void testExcludeClassesInTest() throws Exception {
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        log.info("testExcludeClassesInTest contextClassLoader " + contextClassLoader);
        log.info("testExcludeClassesInTest callerClassLoader " + this.getClass().getClassLoader());

        final String className = "top.infra.test.GuavaAndCommonUser";
        //final Class<?> clazz = Class.forName(className);
        ClassUtils.printClassLoader(GuavaAndCommonUser.class);

        assertEquals("AA", top.infra.test.GuavaAndCommonUser.toUpperCase("aA"));
    }
}
