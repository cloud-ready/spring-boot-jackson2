package top.infra.jackson2;

import com.google.common.collect.Lists;

import java.util.Optional;

public class HalJackson2MapperCustomizerFactory implements Jackson2MapperCustomizerFactory {

    static final String CLASS_JACKSON2_HAL_MODULE = "org.springframework.hateoas.hal.Jackson2HalModule";

    @Override
    public Optional<Jackson2MapperCustomizer> getObject() {
        return this.newInstanceIfPresent(
            "top.infra.jackson2.HalJackson2Customizer",
            Lists.newArrayList(
                CLASS_JACKSON2_HAL_MODULE,
                CLASS_JACKSON2_OBJECT_MAPPER_BUILDER
            ));
    }
}
