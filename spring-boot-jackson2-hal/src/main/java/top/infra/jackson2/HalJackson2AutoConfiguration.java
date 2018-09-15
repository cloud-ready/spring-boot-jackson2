package top.infra.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.hal.Jackson2HalModule;

@AutoConfigureBefore({Jackson2AutoConfiguration.class, JacksonAutoConfiguration.class})
@ConditionalOnClass({Jackson2HalModule.class, ObjectMapper.class})
@ConditionalOnWebApplication
@Configuration
public class HalJackson2AutoConfiguration {

    @Bean
    public HalJackson2Customizer halJackson2Customizer() {
        return new HalJackson2Customizer();
    }
}
