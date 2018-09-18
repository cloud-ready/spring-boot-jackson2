package top.infra.jackson2.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.YearMonth;

import top.infra.jackson2.Jackson2Properties;
import top.infra.jackson2.Jackson2Utils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
@Slf4j
public class Jsr310Tests {

    @Autowired
    private Jackson2Properties jackson2Properties;
    private ObjectMapper customObjectMapper;
    private ObjectMapper rawObjectMapper;
    @Autowired
    private ObjectMapper springObjectMapper;

    @Before
    public void setUp() {
        this.customObjectMapper = Jackson2Utils.customize(this.jackson2Properties, new ObjectMapper());
        this.rawObjectMapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    public void testJsr310LocalDate() {
        final LocalDate localDate = LocalDate.now();
        log.info("localDate customObjectMapper: {}", this.customObjectMapper.writeValueAsString(localDate));
        log.info("localDate rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(localDate));
        log.info("localDate springObjectMapper: {}", this.springObjectMapper.writeValueAsString(localDate));
        assertEquals(this.springObjectMapper.writeValueAsString(localDate), this.customObjectMapper.writeValueAsString(localDate));
        assertNotEquals(this.springObjectMapper.writeValueAsString(localDate), this.rawObjectMapper.writeValueAsString(localDate));
        assertTrue(this.rawObjectMapper.writeValueAsString(localDate).contains("\"year\""));
        assertTrue(this.rawObjectMapper.writeValueAsString(localDate).contains("\"month\""));
        assertFalse(this.springObjectMapper.writeValueAsString(localDate).contains("\"year\""));
        assertFalse(this.springObjectMapper.writeValueAsString(localDate).contains(":"));
        assertFalse(this.springObjectMapper.writeValueAsString(localDate).contains("{"));
    }

    @Test
    @SneakyThrows
    public void testYearMonth() {
        final YearMonth yearMonth = YearMonth.now();
        log.info("yearMonth customObjectMapper: {}", this.customObjectMapper.writeValueAsString(yearMonth));
        log.info("yearMonth rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(yearMonth));
        log.info("yearMonth springObjectMapper: {}", this.springObjectMapper.writeValueAsString(yearMonth));
        assertEquals(this.springObjectMapper.writeValueAsString(yearMonth), this.customObjectMapper.writeValueAsString(yearMonth));
        assertNotEquals(this.springObjectMapper.writeValueAsString(yearMonth), this.rawObjectMapper.writeValueAsString(yearMonth));
        assertTrue(this.rawObjectMapper.writeValueAsString(yearMonth).contains("\"year\""));
        assertTrue(this.rawObjectMapper.writeValueAsString(yearMonth).contains("\"month\""));
        assertFalse(this.springObjectMapper.writeValueAsString(yearMonth).contains("\"year\""));
        assertFalse(this.springObjectMapper.writeValueAsString(yearMonth).contains(":"));
        assertFalse(this.springObjectMapper.writeValueAsString(yearMonth).contains("{"));
    }
}
