package top.infra.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Configuration
    @ConditionalOnJava(JavaVersion.EIGHT)
    @ConditionalOnClass(JavaTimeModule.class)
    static class JavaTimeModuleConfiguration {

        @Bean
        @ConditionalOnMissingBean(JavaTimeModuleCustomizer.class)
        public JavaTimeModuleCustomizer javaTimeModuleCustomizer() {
            return new JavaTimeModuleCustomizer();
        }
    }

    @Configuration
    @ConditionalOnJava(JavaVersion.EIGHT)
    @ConditionalOnClass(Jdk8Module.class)
    static class Jdk8ModuleConfiguration {

        @Bean
        @ConditionalOnMissingBean(Jdk8ModuleCustomizer.class)
        public Jdk8ModuleCustomizer jdk8ModuleCustomizer() {
            return new Jdk8ModuleCustomizer();
        }
    }
}
