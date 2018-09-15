package top.infra.jackson2;

import com.google.common.collect.Lists;

import java.util.Optional;

public class DefaultJackson2MapperCustomizerFactory implements Jackson2MapperCustomizerFactory {

    @Override
    public Optional getObject() {
        return this.newInstanceIfPresent(
            "top.infra.jackson2.DefaultJackson2Customizer",
            Lists.newArrayList(
                CLASS_JACKSON2_OBJECT_MAPPER_BUILDER
            ));
    }
}
