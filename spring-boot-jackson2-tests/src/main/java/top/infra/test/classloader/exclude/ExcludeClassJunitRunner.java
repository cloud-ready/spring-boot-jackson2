package top.infra.test.classloader.exclude;

import static top.infra.test.classloader.exclude.ExcludeClassesClassLoader.getMethod;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.SneakyThrows;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.infra.test.classloader.ClassLoaderChangerRunner;
import top.infra.test.classloader.ClassUtils;

public class ExcludeClassJunitRunner extends Suite {

    private static final Logger logger = LoggerFactory.getLogger("top.infra.test.classloader.exclude.ExcludeClassJunitRunner");

    private static ClassLoader currentClassLoader;
    private static ExcludeClassesClassLoader customClassLoader;

    static {
        // On java8 this is a java.net.URLClassLoader
        // On java11 this is a jdk.internal.loader.ClassLoaders$AppClassLoader
        currentClassLoader = Thread.currentThread().getContextClassLoader();
        final URL[] urls = getURLs(currentClassLoader);
        if (logger.isInfoEnabled()) {
            logger.info("urls: {}", Arrays.toString(urls));
        }

        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            customClassLoader = new ExcludeClassesClassLoader(
                currentClassLoader,
                urls,
                null
            );
            return null;
        });
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

    private static URL[] getURLs(final ClassLoader classLoader) {
        if (classLoader instanceof java.net.URLClassLoader) {
            return ((java.net.URLClassLoader) classLoader).getURLs();
        } else {
            final Object ucp = ucp(classLoader);
            try {
                final Method method = getMethod(ucp, "getURLs");
                return (URL[]) method.invoke(ucp);
            } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException ex) {
                logger.error("Error invoking method getURLs on ucp", ex);
                throw new UnsupportedOperationException(ex);
            }
        }
    }

    // sun.misc.URLClassPath on java8
    // jdk.internal.loader.URLClassPath on java11
    private static Object ucp(final ClassLoader classLoader) {
        try {
            final Field field = classLoader.getClass().getDeclaredField("ucp");
            field.setAccessible(true);
            return field.get(classLoader);
        } catch (final IllegalAccessException | NoSuchFieldException | IllegalArgumentException ex) {
            logger.error("Error getting field ucp", ex);
            throw new IllegalArgumentException(ex);
        }
    }
}
