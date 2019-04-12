package top.infra.jackson2.tests;

import static org.springframework.boot.WebApplicationType.SERVLET;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class JaxbAnnotationJackson2TestApplication {

    public static void main(final String... args) {
        new SpringApplicationBuilder(JaxbAnnotationJackson2TestApplication.class).web(SERVLET).run(args);
    }
}
