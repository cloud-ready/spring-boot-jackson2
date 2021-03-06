package top.infra.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureBefore({Jackson2AutoConfiguration.class, JacksonAutoConfiguration.class})
@ConditionalOnClass({JaxbAnnotationModule.class, JaxbAnnotationIntrospector.class, ObjectMapper.class})
@ConditionalOnWebApplication
@Configuration
public class JaxbAnnotationJackson2AutoConfiguration {

    @Bean
    public JaxbAnnotationJackson2Customizer jaxbAnnotationJackson2Customizer() {
        return new JaxbAnnotationJackson2Customizer();
    }
}
