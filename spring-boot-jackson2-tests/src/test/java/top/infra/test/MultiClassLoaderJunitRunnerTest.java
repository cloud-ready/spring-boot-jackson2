package top.infra.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import top.infra.test.classloader.multi.LibrarySets;
import top.infra.test.classloader.multi.MultiClassLoaderJunitRunner;

@LibrarySets(
    librarySets = {
        "https://repo1.maven.org/maven2/com/google/guava/guava/18.0/guava-18.0.jar,file:target/classes,file:target/test-classes",
        "https://repo1.maven.org/maven2/com/google/guava/guava/19.0/guava-19.0.jar,file:target/classes,file:target/test-classes",
    }
)
@RunWith(MultiClassLoaderJunitRunner.class)
public class MultiClassLoaderJunitRunnerTest {

    @Test
    public void testGuavaAndCommonUser() {
        assertEquals("AAAA", GuavaAndCommonUser.toUpperCase("AAaa"));
    }
}
