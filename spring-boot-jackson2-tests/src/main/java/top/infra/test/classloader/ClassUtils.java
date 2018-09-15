package top.infra.test.classloader;

import org.junit.runners.model.InitializationError;

import java.net.URLClassLoader;
import java.util.Arrays;

public class ClassUtils {

    public static Class<?> loadFromCustomClassloader(final ClassLoader classLoader, final Class<?> clazz) throws InitializationError {
        try {
            final Class<?> loaded;
            loaded = Class.forName(clazz.getName(), true, classLoader);
            //loaded = customClassLoader.loadClass(clazz.getName()); // attempted  duplicate class definition
            return loaded;
        } catch (final ClassNotFoundException ex) {
            throw new InitializationError(ex);
        }
    }

    public static String printClassLoader(final Class<?> target) {
        final ClassLoader classLoader = target.getClassLoader();

        final String result;

        if (classLoader instanceof URLClassLoader) {
            result = target.getName()
                + "/classLoader = "
                + classLoader
                + "/URLs/" + Arrays.asList(((URLClassLoader) classLoader).getURLs());
        } else {
            result = target.getName() + "/classLoader = " + classLoader;
        }
        System.out.println(result);

        return result;
    }
}
