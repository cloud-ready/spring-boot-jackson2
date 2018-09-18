package top.infra.jackson2.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.fasterxml.jackson.core.type.TypeReference;
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

    @Test
    @SneakyThrows
    public void testPair() {
        final Pair<PairKey, PairValue> pair = newImmutablePair();

        log.info("pair customObjectMapper: {}", this.customObjectMapper.writeValueAsString(pair));
        log.info("pair rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(pair));
        log.info("pair springObjectMapper: {}", this.springObjectMapper.writeValueAsString(pair));
        assertEquals(this.springObjectMapper.writeValueAsString(pair), this.customObjectMapper.writeValueAsString(pair));
        assertNotEquals(this.springObjectMapper.writeValueAsString(pair), this.rawObjectMapper.writeValueAsString(pair));

        final String serialized = this.springObjectMapper.writeValueAsString(pair);
        final Pair<PairKey, PairValue> deserialized = this.springObjectMapper.readValue(
            serialized,
            new TypeReference<Pair<PairKey, PairValue>>() {
            }
        );
        log.info("deserialized springObjectMapper: {}", deserialized);
        assertEquals(pair, deserialized);

        final Pair<PairKey, PairValue> pairNullKey = new ImmutablePair<>(null, pair.getValue());
        log.info("pairNullKey springObjectMapper: {}", this.springObjectMapper.writeValueAsString(pairNullKey));
        final Pair<PairKey, PairValue> pairNullValue = new ImmutablePair<>(pair.getKey(), null);
        log.info("pairNullValue springObjectMapper: {}", this.springObjectMapper.writeValueAsString(pairNullValue));
        final Pair<PairKey, PairValue> pairNull = new ImmutablePair<>(null, null);
        log.info("pairNull springObjectMapper: {}", this.springObjectMapper.writeValueAsString(pairNull));
    }

    @Test
    @SneakyThrows
    public void testNestedPair() {
        final Pair<PairKey, PairValue> pair = newImmutablePair();
        final Pair<PairKey, Pair<PairKey, PairValue>> nestedPair = new ImmutablePair<>(pair.getKey(), pair);

        log.info("nestedPair customObjectMapper: {}", this.customObjectMapper.writeValueAsString(nestedPair));
        log.info("nestedPair rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(nestedPair));
        log.info("nestedPair springObjectMapper: {}", this.springObjectMapper.writeValueAsString(nestedPair));
        assertEquals(this.springObjectMapper.writeValueAsString(pair), this.customObjectMapper.writeValueAsString(pair));
        assertNotEquals(this.springObjectMapper.writeValueAsString(pair), this.rawObjectMapper.writeValueAsString(pair));
    }

    @Test
    @SneakyThrows
    public void testPairInBean() {
        final Pair<PairKey, PairValue> pair = newImmutablePair();
        final Pair<PairKey, Pair<PairKey, PairValue>> nestedPair = new ImmutablePair<>(pair.getKey(), pair);
        final Entity entity = new Entity();
        entity.setPair(nestedPair);

        log.info("entity customObjectMapper: {}", this.customObjectMapper.writeValueAsString(entity));
        log.info("entity rawObjectMapper: {}", this.rawObjectMapper.writeValueAsString(entity));
        log.info("entity springObjectMapper: {}", this.springObjectMapper.writeValueAsString(entity));
        assertEquals(this.springObjectMapper.writeValueAsString(pair), this.customObjectMapper.writeValueAsString(pair));
        assertNotEquals(this.springObjectMapper.writeValueAsString(pair), this.rawObjectMapper.writeValueAsString(pair));
    }

    private static Pair<PairKey, PairValue> newImmutablePair() {
        final PairKey pairKey = new PairKey();
        pairKey.setName("key");
        pairKey.setDesc("pair key");
        final PairValue pairValue = new PairValue();
        pairValue.setValue("value");

        return new ImmutablePair<>(pairKey, pairValue);
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
}
