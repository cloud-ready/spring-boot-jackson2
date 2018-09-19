package top.infra.jackson2.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import top.infra.jackson2.Jackson2Properties;
import top.infra.jackson2.Jackson2Utils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
@Slf4j
public class Jackson2UtilsTests {

    @Autowired
    private Jackson2Properties jackson2Properties;

    @Test
    public void testFromToJson() {
        final ObjectMapper objectMapper = Jackson2Utils.customize(this.jackson2Properties, new ObjectMapper());
        final Optional<String> stringOptional = Optional.of("stringOptional");
        final String stringOptionalSerialized = Jackson2Utils.toJson(objectMapper).apply(stringOptional);
        assertEquals(stringOptionalSerialized, Jackson2Utils.toJson(objectMapper, stringOptional));

        final Optional<String> stringOptionalDeserialized = Jackson2Utils.fromJson(objectMapper, new TypeReference<Optional<String>>() {
        }).apply(stringOptionalSerialized);
        assertEquals(stringOptional, stringOptionalDeserialized);
    }

    @Test
    public void testIsJackson2Present() {
        assertTrue(Jackson2Utils.isJackson2Present());
    }
}
