package top.infra.jackson2.tests;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class JaxbAnnotationJackson2TestApplication {

    public static void main(final String... args) {
        new SpringApplicationBuilder(JaxbAnnotationJackson2TestApplication.class).web(true).run(args);
    }
}
