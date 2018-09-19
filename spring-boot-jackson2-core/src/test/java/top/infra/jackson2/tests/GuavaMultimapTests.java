package top.infra.jackson2.tests;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.Jackson2Properties;
import top.infra.jackson2.Jackson2Utils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Jackson2TestApplication.class)
@Slf4j
public class GuavaMultimapTests {

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
    public void testMultimapSerialize() {
        final MapKey key = new MapKey();
        key.setName("key");
        key.setDesc("pair key");
        final MapValue value1 = new MapValue();
        value1.setValue("value1");
        final MapValue value2 = new MapValue();
        value1.setValue("value2");

        final Multimap<MapKey, MapValue> multimap = LinkedHashMultimap.create();
        multimap.put(key, value1);
        multimap.put(key, value2);

        log.info("multimap customObjectMapper: {}", this.customObjectMapper.writeValueAsString(multimap));
        log.info("multimap rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(multimap));
        log.info("multimap springObjectMapper: {}", this.springObjectMapper.writeValueAsString(multimap));
    }

    @Data
    public static class MapKey {
        private String name;
        private String desc;
    }

    @Data
    public static class MapValue {
        private String value;
    }
}
