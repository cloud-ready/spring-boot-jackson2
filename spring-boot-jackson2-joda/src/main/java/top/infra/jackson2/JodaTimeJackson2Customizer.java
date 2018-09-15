package top.infra.jackson2;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

/**
 * see: {@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration}.
 * {@link JodaModule} will auto installed by {@link Jackson2ObjectMapperBuilder}
 */
@Slf4j
public class JodaTimeJackson2Customizer implements Jackson2Customizer {

    @Override
    public void customize(final Jackson2Properties properties, final ObjectMapper mapper) {
        final Module jodaDateTimeSerializationModule = this.jodaDateTimeSerializationModule(properties);
        mapper.registerModule(jodaDateTimeSerializationModule);
    }

    @Override
    public void customize(final Jackson2Properties properties, final Jackson2ObjectMapperBuilder builder) {
        final Module jodaDateTimeSerializationModule = this.jodaDateTimeSerializationModule(properties);
        final List<Module> modules = this.modules(builder);
        modules.add(jodaDateTimeSerializationModule);
        builder.modulesToInstall(modules.toArray(new Module[0]));
    }

    public SimpleModule jodaDateTimeSerializationModule(final Jackson2Properties properties) {
        SimpleModule module = new SimpleModule();
        JacksonJodaDateFormat jacksonJodaFormat = getJacksonJodaDateFormat(properties);
        if (jacksonJodaFormat != null) {
            module.addSerializer(DateTime.class,
                new DateTimeSerializer(jacksonJodaFormat, 1)); // see: JodaDateSerializerBase.FORMAT_STRING
        }
        return module;
    }

    private JacksonJodaDateFormat getJacksonJodaDateFormat(final Jackson2Properties properties) {
        if (properties.getJodaDateTimeFormat() != null) {
            return new JacksonJodaDateFormat(DateTimeFormat.forPattern(properties.getJodaDateTimeFormat()).withZoneUTC());
        }
        if (properties.getDateFormat() != null) {
            try {
                return new JacksonJodaDateFormat(DateTimeFormat.forPattern(this.dateFormat(properties)).withZoneUTC());
            } catch (final IllegalArgumentException ex) {
                if (log.isWarnEnabled()) {
                    log.warn("spring.jackson.date-format could not be used to "
                        + "configure formatting of Joda's DateTime. You may want "
                        + "to configure spring.jackson.joda-date-time-format as "
                        + "well.");
                }
            }
        }
        return null;
    }
}
