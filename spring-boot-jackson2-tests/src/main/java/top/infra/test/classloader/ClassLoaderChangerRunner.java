package top.infra.test.classloader;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * see: https://arnaudroger.github.io/blog/2016/07/18/testing-with-custom-classpath.html
 * see: https://github.com/arnaudroger/blog/tree/master/src/main/java/io/github/arnaudroger/tmvl
 */
public class ClassLoaderChangerRunner extends Runner {

    private final ClassLoader currentClassLoader;
    private final ClassLoader customClassLoader;
    private final BlockJUnit4ClassRunner delegate;

    public ClassLoaderChangerRunner(
        final ClassLoader currentClassLoader,
        final ClassLoader customClassLoader,
        final BlockJUnit4ClassRunner delegate
    ) {
        this.currentClassLoader = currentClassLoader;
        this.customClassLoader = customClassLoader;
        this.delegate = delegate;
    }

    @Override
    public Description getDescription() {
        return this.delegate.getDescription();
    }

    @Override
    public void run(final RunNotifier runNotifier) {
        try {
            Thread.currentThread().setContextClassLoader(this.customClassLoader);
            this.delegate.run(runNotifier);
        } finally {
            Thread.currentThread().setContextClassLoader(this.currentClassLoader);
        }
    }
}
