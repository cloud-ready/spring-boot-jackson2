package top.infra.test.classloader.exclude;

import com.google.common.collect.ImmutableList;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

/**
 * see: https://medium.com/@davehagler/junit-testrunner-with-a-custom-classloader-337d447dba4f
 */
@Slf4j
public class ExcludeClassesClassLoader extends URLClassLoader {

    private final ClassLoader parentClassLoader;
    private Collection<String> excludes;

    public ExcludeClassesClassLoader(
        final ClassLoader parentClassLoader,
        final URL[] urls,
        final Collection<String> excludes
    ) {

        super(urls, null);

        log.info("ExcludeClassesClassLoader parentClassLoader " + parentClassLoader);

        this.setExcludes(excludes);
        this.parentClassLoader = parentClassLoader;
    }

    @Override
    public synchronized Class<?> loadClass(final String name) throws ClassNotFoundException {
        //log.info("loadClass " + name);

        if (!this.excludes.contains(name)) {
            // User's test class is already loaded by parentClassLoader
            // Must use parentClassLoader to load top.infra.test.classloader.exclude.ExcludeClasses.
            if (!name.startsWith("java.")
                && !name.startsWith("javax.")
                && !name.startsWith("org.junit.")
                && !name.startsWith("org.xml.")
                && !name.startsWith("org.w3c.")
                && !name.startsWith("sun.")
                && !"top.infra.test.classloader.exclude.ExcludeClasses".equals(name)
            ) {
                final Class<?> result;
                final Class<?> loadedClass = this.findLoadedClass(name);
                if (loadedClass == null) {
                    result = this.findClass(name);
                } else {
                    result = loadedClass;
                }
                return result;
            }

            return this.parentClassLoader.loadClass(name);
        } else {
            throw new ClassNotFoundException("class " + name + " is excluded from classpath");
        }
    }

    public synchronized void setExcludes(final Collection<String> excludes) {
        this.excludes = excludes != null ? excludes : ImmutableList.of();
    }

    // @Deprecated
    // private static Class<?> findLoadedClass(final ClassLoader classLoader, final String name) {
    //     final java.lang.reflect.Method method = getDeclaredMethod(classLoader, "findLoadedClass", String.class);
    //     try {
    //         return (Class<?>) method.invoke(classLoader, name);
    //     } catch (final NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException ex) {
    //         throw new UnsupportedOperationException(ex);
    //     }
    // }

    static java.lang.reflect.Method getDeclaredMethod(
        final Object instance,
        final String name,
        final Class<?>... parameterTypes
    ) throws NoSuchMethodException {
        final java.lang.reflect.Method method = instance.getClass().getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    static java.lang.reflect.Method getMethod(
        final Object instance,
        final String name,
        final Class<?>... parameterTypes
    ) throws NoSuchMethodException {
        final java.lang.reflect.Method method = instance.getClass().getMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }
}
