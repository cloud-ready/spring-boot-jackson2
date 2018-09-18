package top.infra.jackson2.tests;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.infra.jackson2.Jackson2Properties;
import top.infra.jackson2.Jackson2Utils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonsLangJackson2TestApplication.class)
@Slf4j
public class PairTests {

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

    @Data
    public static class PairKey {
        private String name;
        private String desc;
    }

    @Data
    public static class PairValue {
        private String value;
    }

    @Data
    public static class Entity {
        private Pair<PairKey, Pair<PairKey, PairValue>> pair;
    }

    @Test
    @SneakyThrows
    public void testPairSerializer() {
        final PairKey pairKey = new PairKey();
        pairKey.setName("key");
        pairKey.setDesc("pair key");
        final PairValue pairValue = new PairValue();
        pairValue.setValue("value");

        final Pair<PairKey, PairValue> innerPair = new ImmutablePair<>(pairKey, pairValue);
        final Pair<PairKey, Pair<PairKey, PairValue>> pair = new ImmutablePair<>(pairKey, innerPair);

        final Entity entity = new Entity();
        entity.setPair(pair);

        log.info("pair customObjectMapper: {}", this.customObjectMapper.writeValueAsString(pair));
        log.info("pair rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(pair));
        log.info("pair springObjectMapper: {}", this.springObjectMapper.writeValueAsString(pair));

        log.info("entity customObjectMapper: {}", this.customObjectMapper.writeValueAsString(entity));
        log.info("entity rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(entity));
        log.info("entity springObjectMapper: {}", this.springObjectMapper.writeValueAsString(entity));
    }
}
