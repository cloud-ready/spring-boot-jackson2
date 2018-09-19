package top.infra.test;

import static org.junit.Assert.assertEquals;

//@LibrarySets(
//    librarySets = {
//        "http://repo1.maven.org/maven2/com/google/guava/guava/18.0/guava-18.0.jar,file:target/classes,file:target/test-classes",
//        "http://repo1.maven.org/maven2/com/google/guava/guava/19.0/guava-19.0.jar,file:target/classes,file:target/test-classes",
//    }
//)
//@RunWith(MultiClassLoaderJunitRunner.class)
public class MultiClassLoaderJunitRunnerTest {

    //@Test
    public void testGuavaAndCommonUser() {
        assertEquals("AAAA", GuavaAndCommonUser.toUpperCase("AAaa"));
    }
}
