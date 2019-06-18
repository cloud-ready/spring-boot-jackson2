package top.infra.test.classloader.multi;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import top.infra.test.classloader.ClassLoaderChangerRunner;

public class MultiClassLoaderJunitRunner extends Suite {

    private static ClassLoader currentClassLoader;

    static {
        // On java8 this is a java.net.URLClassLoader
        // On java11 this is a jdk.internal.loader.ClassLoaders$AppClassLoader
        currentClassLoader = Thread.currentThread().getContextClassLoader();
    }

    public MultiClassLoaderJunitRunner(final Class<?> klass) throws InitializationError {
        super(klass, buildRunners(klass));
    }

    @SneakyThrows
    private static List<Runner> buildRunners(Class<?> klass) {
        final LibrarySets librarySet = klass.getAnnotation(LibrarySets.class);
        if (librarySet == null) throw new IllegalArgumentException("Class " + klass + " is missing LibrarySets annotation");

        final List<Runner> runners = new ArrayList<>();
        int i = 0;
        for (final String urlsList : librarySet.librarySets()) {
            final String suffix = String.valueOf(i);

            final ClassLoader customClassLoader = new LibrarySetClassLoader(currentClassLoader, urlsList.split(","));

            final Class<?> testClass = customClassLoader.loadClass(klass.getName());

            final BlockJUnit4ClassRunner junit4Runner = new BlockJUnit4ClassRunner(testClass) {
                @Override
                protected String getName() {
                    return super.getName() + suffix;
                }
            };

            runners.add(new ClassLoaderChangerRunner(currentClassLoader, customClassLoader, junit4Runner));
            i++;
        }
        return runners;
    }
}
