package top.infra.common;

import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Optional;

public abstract class ClassUtils {

    private ClassUtils() {
    }

    public static boolean isPresent(final String className) {
        return isPresent(className, null);
    }

    /**
     * see: {@link org.springframework.boot.autoconfigure.condition.OnClassCondition}
     *
     * @param className   className
     * @param classLoader classLoader
     * @return isPresent
     */
    public static boolean isPresent(final String className, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = org.springframework.util.ClassUtils.getDefaultClassLoader();
        }
        try {
            forName(className, classLoader);
            return true;
        } catch (final Throwable ex) {
            return false;
        }
    }

    public static Class<?> forName(final String className, final ClassLoader classLoader) throws ClassNotFoundException {
        if (classLoader != null) {
            return classLoader.loadClass(className);
        }
        return Class.forName(className);
    }

    public static Optional<Class<?>> forName(final String className) {
        try {
            final ClassLoader classLoader = org.springframework.util.ClassUtils.getDefaultClassLoader();
            final Class<?> result;
            if (classLoader != null) {
                result = classLoader.loadClass(className);
            } else {
                result = Class.forName(className);
            }
            return Optional.ofNullable(result);
        } catch (final ClassNotFoundException ex) {
            return Optional.empty();
        }
    }

    public static void printClassLoader(final Class<?> target) {
        ClassLoader classLoader = target.getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            System.out.println(target.getName()
                + "/classLoader = "
                + classLoader
                + "/URLs/" + Arrays.asList(((URLClassLoader) classLoader).getURLs()));
        } else {
            System.out.println(target.getName() + "/classLoader = " + classLoader);
        }
    }
}
