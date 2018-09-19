package top.infra.jackson2;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.ClassUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * see: {@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration}.
 * see: org.springframework.hateoas.config.HypermediaSupportBeanDefinitionRegistrar
 * see: org.springframework.hateoas.config.EnableHypermediaSupport
 */
public class DefaultJackson2Customizer implements Jackson2Customizer {

    @SuppressWarnings("unchecked")
    @Override
    public void customize(final Jackson2Properties properties, final ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
            this.feature(properties, DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, TRUE));

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            this.feature(properties, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, FALSE));

        mapper.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS,
            this.feature(properties, JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, FALSE));

        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS,
            this.feature(properties, JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, TRUE));

        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
            this.feature(properties, JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, TRUE));

        // see: https://github.com/FasterXML/jackson-databind/issues/1218
        // since jackson2 2.9.x
        //mapper.configure(MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES,
        //    this.feature(properties, MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES, FALSE));

        mapper.configure(SerializationFeature.INDENT_OUTPUT,
            this.feature(properties, SerializationFeature.INDENT_OUTPUT, FALSE));

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            this.feature(properties, SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, FALSE));
        //WRITE_DATES_WITH_ZONE_ID

        mapper.setTimeZone(this.timeZone(properties));
        this.configureDateFormat(properties, mapper);

        // Jackson2ObjectMapperBuilder.registerWellKnownModulesIfAvailable
        new Jackson2ObjectMapperBuilder().configure(mapper);

        final ClassLoader moduleClassLoader = getClass().getClassLoader();
        if (isGuavaPresent(moduleClassLoader)) {
            try {
                final Class<? extends Module> guavaModuleClass = (Class<? extends Module>)
                    ClassUtils.forName("com.fasterxml.jackson.datatype.guava.GuavaModule", moduleClassLoader);
                final Module guavaModule = BeanUtils.instantiateClass(guavaModuleClass);
                mapper.registerModule(guavaModule);
            } catch (final ClassNotFoundException ex) {
                // jackson-datatype-guava or guava not available
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void customize(final Jackson2Properties properties, final Jackson2ObjectMapperBuilder builder) {
        this.feature(builder, DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
            this.feature(properties, DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, TRUE));

        this.feature(builder, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            this.feature(properties, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, FALSE));

        this.feature(builder, JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS,
            this.feature(properties, JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, FALSE));

        this.feature(builder, JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS,
            this.feature(properties, JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, TRUE));

        this.feature(builder, JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,
            this.feature(properties, JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, TRUE));

        // see: https://github.com/FasterXML/jackson-databind/issues/1218
        // since jackson2 2.9.x
        //this.feature(builder, MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES,
        //    this.feature(properties, MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES, FALSE));

        this.feature(builder, SerializationFeature.INDENT_OUTPUT,
            this.feature(properties, SerializationFeature.INDENT_OUTPUT, FALSE));

        this.feature(builder, SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
            this.feature(properties, SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, FALSE));
        //WRITE_DATES_WITH_ZONE_ID

        builder.timeZone(this.timeZone(properties));
        properties.setDateFormat(this.dateFormat(properties));

        final ClassLoader moduleClassLoader = this.moduleClassLoader(builder);
        if (isGuavaPresent(moduleClassLoader)) {
            try {
                final Class<? extends Module> guavaModuleClass = (Class<? extends Module>)
                    ClassUtils.forName("com.fasterxml.jackson.datatype.guava.GuavaModule", moduleClassLoader);
                final Module guavaModule = BeanUtils.instantiateClass(guavaModuleClass);
                final List<Module> modules = this.modules(builder);
                modules.add(guavaModule);
                builder.modulesToInstall(modules.toArray(new Module[0]));
            } catch (final ClassNotFoundException ex) {
                // jackson-datatype-guava or guava not available
            }
        }
    }

    public boolean isGuavaPresent(final ClassLoader classLoader) {
        return ClassUtils.isPresent("com.google.common.collect.Multimap", classLoader)
            && ClassUtils.isPresent("com.fasterxml.jackson.datatype.guava.GuavaModule", classLoader);
    }

    /**
     * see: {@link org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration}
     * Jackson2ObjectMapperBuilderCustomizerConfiguration#configureDateFormat
     */
    private void configureDateFormat(final Jackson2Properties properties, final ObjectMapper mapper) {
        // We support a fully qualified class name extending DateFormat or a date
        // pattern string value
        String dateFormat = properties.getDateFormat();
        if (dateFormat != null) {
            try {
                Class<?> dateFormatClass = ClassUtils.forName(dateFormat, null);
                mapper.setDateFormat((DateFormat) BeanUtils.instantiateClass(dateFormatClass));
            } catch (ClassNotFoundException ex) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    dateFormat);
                // Since Jackson 2.6.3 we always need to set a TimeZone (see
                // gh-4170). If none in our properties fallback to the Jackson's
                // default
                TimeZone timeZone = properties.getTimeZone();
                if (timeZone == null) {
                    timeZone = new ObjectMapper().getSerializationConfig()
                        .getTimeZone();
                }
                simpleDateFormat.setTimeZone(timeZone);
                mapper.setDateFormat(simpleDateFormat);
            }
        }
    }

    private void feature(final Jackson2ObjectMapperBuilder builder, final Object feature, final Boolean enabled) {
        if (enabled) {
            builder.featuresToEnable(feature);
        } else {
            builder.featuresToDisable(feature);
        }
    }

    Boolean feature(final Jackson2Properties properties, final DeserializationFeature feature, final Boolean defaultValue) {
        return properties.getDeserialization().getOrDefault(feature, defaultValue);
    }

    Boolean feature(final Jackson2Properties properties, final JsonGenerator.Feature feature, final Boolean defaultValue) {
        return properties.getGenerator().getOrDefault(feature, defaultValue);
    }

    Boolean feature(final Jackson2Properties properties, final JsonParser.Feature feature, final Boolean defaultValue) {
        return properties.getParser().getOrDefault(feature, defaultValue);
    }

    Boolean feature(final Jackson2Properties properties, final MapperFeature feature, final Boolean defaultValue) {
        return properties.getMapper().getOrDefault(feature, defaultValue);
    }

    Boolean feature(final Jackson2Properties properties, final SerializationFeature feature, final Boolean defaultValue) {
        return properties.getSerialization().getOrDefault(feature, defaultValue);
    }
}
