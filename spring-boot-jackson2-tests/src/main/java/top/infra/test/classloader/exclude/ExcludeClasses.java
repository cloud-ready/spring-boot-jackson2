package top.infra.test.classloader.exclude;

import org.junit.runners.BlockJUnit4ClassRunner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcludeClasses {

    String[] excludesClasses();

    Class<?> runner() default BlockJUnit4ClassRunner.class;
}
