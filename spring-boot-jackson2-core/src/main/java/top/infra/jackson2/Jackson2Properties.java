package top.infra.jackson2;

import static java.lang.Boolean.FALSE;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * see {@link org.springframework.boot.autoconfigure.jackson.JacksonProperties}
 */
@ConfigurationProperties(prefix = "spring.jackson")
@Getter
@Setter
public class Jackson2Properties {

    /**
     * Date format string (yyyy-MM-dd HH:mm:ss), or a fully-qualified date format class
     * name.
     */
    private String dateFormat;

    /**
     * Joda date time format string (yyyy-MM-dd HH:mm:ss). If not configured,
     * "date-format" will be used as a fallback if it is configured with a format string.
     */
    private String jodaDateTimeFormat;

    /**
     * One of the constants on Jackson's PropertyNamingStrategy
     * (CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES). Can also be a fully-qualified class
     * name of a PropertyNamingStrategy subclass.
     */
    private String propertyNamingStrategy;

    /**
     * Jackson on/off features that affect the way Java objects are serialized.
     */
    private Map<SerializationFeature, Boolean> serialization = new HashMap<SerializationFeature, Boolean>();

    /**
     * Jackson on/off features that affect the way Java objects are deserialized.
     */
    private Map<DeserializationFeature, Boolean> deserialization = new HashMap<DeserializationFeature, Boolean>();

    /**
     * Jackson general purpose on/off features.
     */
    private Map<MapperFeature, Boolean> mapper = new HashMap<MapperFeature, Boolean>();

    /**
     * Jackson on/off features for parsers.
     */
    private Map<JsonParser.Feature, Boolean> parser = new HashMap<JsonParser.Feature, Boolean>();

    /**
     * Jackson on/off features for generators.
     */
    private Map<JsonGenerator.Feature, Boolean> generator = new HashMap<JsonGenerator.Feature, Boolean>();

    /**
     * Controls the inclusion of properties during serialization. Configured with one of
     * the values in Jackson's JsonInclude.Include enumeration.
     */
    private JsonInclude.Include defaultPropertyInclusion;

    /**
     * Time zone used when formatting dates. Configured using any recognized time zone
     * identifier, for example "America/Los_Angeles" or "GMT+10".
     */
    private TimeZone timeZone = null;

    /**
     * Locale used for formatting.
     */
    private Locale locale;
    @NestedConfigurationProperty
    private Jaxb jaxb = new Jaxb();

    @Data
    public static class Jaxb {

        private Boolean enabled = FALSE;
    }
}
