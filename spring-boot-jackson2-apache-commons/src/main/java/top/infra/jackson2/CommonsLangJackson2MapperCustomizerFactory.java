package top.infra.jackson2;

import com.google.common.collect.Lists;

import java.util.Optional;

public class CommonsLangJackson2MapperCustomizerFactory implements Jackson2MapperCustomizerFactory {

    static final String CLASS_MUTABLE_PAIR = "org.apache.commons.lang3.tuple.MutablePair";
    static final String CLASS_PAIR = "org.apache.commons.lang3.tuple.Pair";

    @Override
    public Optional<Jackson2MapperCustomizer> getObject() {
        return this.newInstanceIfPresent(
            "top.infra.jackson2.JodaTimeJackson2Customizer",
            Lists.newArrayList(
                CLASS_MUTABLE_PAIR,
                CLASS_PAIR,
                CLASS_JACKSON2_OBJECT_MAPPER_BUILDER
            ));
    }
}
