package top.infra.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;

import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureBefore({Jackson2AutoConfiguration.class, JacksonAutoConfiguration.class})
@ConditionalOnClass({DateTime.class, DateTimeSerializer.class, JacksonJodaDateFormat.class, ObjectMapper.class})
@ConditionalOnWebApplication
@Configuration
public class JodaTimeJackson2AutoConfiguration {

    @Bean
    public JodaTimeJackson2Customizer jodaTimeJackson2Customizer() {
        return new JodaTimeJackson2Customizer();
    }
}
