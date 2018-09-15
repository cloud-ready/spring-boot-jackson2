package top.infra.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@AutoConfigureBefore({JacksonAutoConfiguration.class})
@ConditionalOnClass({ObjectMapper.class})
@ConditionalOnWebApplication
@Configuration
@EnableConfigurationProperties({Jackson2Properties.class})
public class Jackson2AutoConfiguration {

    @Bean
    public DefaultJackson2Customizer defaultJackson2Customizer() {
        return new DefaultJackson2Customizer();
    }

    @Bean
    @ConditionalOnMissingBean(DefaultJackson2ObjectMapperBuilderCustomizer.class)
    public DefaultJackson2ObjectMapperBuilderCustomizer defaultJackson2ObjectMapperBuilderCustomizer(
        final List<Jackson2Customizer> customizers,
        final Jackson2Properties properties
    ) {
        return new DefaultJackson2ObjectMapperBuilderCustomizer(customizers, properties);
    }
}
