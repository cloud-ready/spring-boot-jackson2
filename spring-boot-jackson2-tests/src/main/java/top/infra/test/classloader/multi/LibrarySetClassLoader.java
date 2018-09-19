package top.infra.test.classloader.multi;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class LibrarySetClassLoader extends URLClassLoader {

    private final ClassLoader classLoader;

    public LibrarySetClassLoader(final ClassLoader classLoader, final String[] libraries) throws IOException {
        super(toUrls(libraries), Integer.class.getClassLoader());
        this.classLoader = classLoader;
    }

    public static URL[] toUrls(final String[] libraries) throws IOException {
        return Arrays.stream(libraries).map((s) -> {
            try {
                if (s.startsWith("file:")) {
                    return new File(s.substring("file:".length())).toURI().toURL();
                }
                return new URL(s);
            } catch (final MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).toArray(URL[]::new);
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        try {
            return super.loadClass(name);
        } catch (final ClassNotFoundException e) {
            return this.classLoader.loadClass(name);
        }
    }
}
