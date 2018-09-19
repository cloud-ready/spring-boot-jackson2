package top.infra.jackson2.tests;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import top.infra.jackson2.DefaultJackson2Customizer;
import top.infra.jackson2.Jackson2Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
@Slf4j
public class DateFormatTest {

    static {
        System.setProperty("spring.jackson.date-format", DefaultJackson2Customizer.PATTERN_JAVA_ISO8601);
        System.setProperty("spring.jackson.serialization.write_dates_as_timestamps", "false");
        System.setProperty("spring.jackson.time-zone", "UTC");
    }

    @Autowired
    private Jackson2Properties jackson2Properties;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDateFormat() throws JsonProcessingException {
        final Date now = new Date();
        final String nowSerialized = this.objectMapper.writeValueAsString(now).replaceAll("\"", "");
        log.info("nowSerialized testDateFormat: {}", nowSerialized);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DefaultJackson2Customizer.PATTERN_JAVA_ISO8601);
        simpleDateFormat.setTimeZone(this.jackson2Properties.getTimeZone());
        assertEquals(simpleDateFormat.format(now), nowSerialized);
    }
}
