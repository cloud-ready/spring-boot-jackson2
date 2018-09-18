package top.infra.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureBefore({Jackson2AutoConfiguration.class, JacksonAutoConfiguration.class})
@ConditionalOnClass({Pair.class, MutablePair.class, ObjectMapper.class})
@ConditionalOnWebApplication
@Configuration
public class CommonsLangJackson2AutoConfiguration {

    @Bean
    public CommonsLangJackson2Customizer commonsLangJackson2Customizer() {
        return new CommonsLangJackson2Customizer();
    }
}
