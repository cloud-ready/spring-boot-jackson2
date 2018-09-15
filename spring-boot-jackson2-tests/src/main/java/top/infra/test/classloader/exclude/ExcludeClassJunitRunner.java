package top.infra.test.classloader.exclude;

import lombok.SneakyThrows;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.infra.test.classloader.ClassLoaderChangerRunner;
import top.infra.test.classloader.ClassUtils;

public class ExcludeClassJunitRunner extends Suite {

    private static URLClassLoader currentClassLoader;
    private static ExcludeClassesClassLoader customClassLoader;

    static {
        currentClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        customClassLoader = new ExcludeClassesClassLoader(
            currentClassLoader,
            currentClassLoader.getURLs(),
            null
        );
    }

    public ExcludeClassJunitRunner(final Class<?> klass) throws InitializationError {
        super(
            ClassUtils.loadFromCustomClassloader(customClassLoader, klass),
            buildRunners(ClassUtils.loadFromCustomClassloader(customClassLoader, klass)));
    }

    // Runs junit tests in a separate thread using the custom class loader
//    @Override
//    public void run(final org.junit.runner.notification.RunNotifier notifier) {
//        final Runnable runnable = () -> {
//            super.run(notifier);
//        };
//        final Thread thread = new Thread(runnable);
//        thread.setContextClassLoader(customClassLoader);
//        thread.start();
//        try {
//            thread.join();
//        } catch (final InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
//    }

    @SneakyThrows
    private static List<Runner> buildRunners(final Class<?> klass) {
        final ExcludeClasses excludeClasses = klass.getAnnotation(ExcludeClasses.class);
        if (excludeClasses == null) throw new IllegalArgumentException("Class " + klass + " is missing ExcludeClasses annotation");

        customClassLoader.setExcludes(Arrays.asList(excludeClasses.excludesClasses()));

        final Class<?> testClass = customClassLoader.loadClass(klass.getName());

        final Class<?> runnerClass = excludeClasses.runner();
        final BlockJUnit4ClassRunner junit4Runner =
            (BlockJUnit4ClassRunner) runnerClass.getConstructor(Class.class).newInstance(testClass);

        final List<Runner> runners = new ArrayList<>();
        runners.add(new ClassLoaderChangerRunner(currentClassLoader, customClassLoader, junit4Runner));
        return runners;
    }
}
