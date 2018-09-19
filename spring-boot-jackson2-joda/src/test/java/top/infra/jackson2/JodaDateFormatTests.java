package top.infra.jackson2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

@Slf4j
public class JodaDateFormatTests {

    @Test
    public void testDateFormat() throws JsonProcessingException {
        final Jackson2Properties jackson2Properties = new Jackson2Properties();
        jackson2Properties.setDateFormat(Jackson2MapperCustomizer.PATTERN_JAVA_ISO8601);

        final JodaTimeJackson2Customizer customizer = new JodaTimeJackson2Customizer();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        customizer.customize(jackson2Properties, objectMapper);

        final DateTime now = DateTime.now(DateTimeZone.UTC);
        final String nowSerialized = objectMapper.writeValueAsString(now).replaceAll("\"", "");
        log.info("nowSerialized testDateFormat: {}", nowSerialized);
        assertTrue(nowSerialized.startsWith("{"));
        assertTrue(nowSerialized.endsWith("}"));
    }

    @Test
    public void testJodaDateTimeFormat() throws JsonProcessingException {
        final Jackson2Properties jackson2Properties = new Jackson2Properties();
        jackson2Properties.setJodaDateTimeFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        final JodaTimeJackson2Customizer customizer = new JodaTimeJackson2Customizer();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        customizer.customize(jackson2Properties, objectMapper);

        final DateTime now = DateTime.now(DateTimeZone.UTC);
        final String nowSerialized = objectMapper.writeValueAsString(now).replaceAll("\"", "");

        log.info("nowSerialized testJodaDateTimeFormat: {}", nowSerialized);
        assertTrue(nowSerialized.endsWith("+0000"));
        final DateTime nowParsed = customizer
            .getJacksonJodaDateFormat(jackson2Properties)
            .withTimeZone(DateTimeZone.UTC.toTimeZone())
            .rawFormatter()
            .parseDateTime(nowSerialized);
        assertEquals(now, nowParsed);
    }
}
