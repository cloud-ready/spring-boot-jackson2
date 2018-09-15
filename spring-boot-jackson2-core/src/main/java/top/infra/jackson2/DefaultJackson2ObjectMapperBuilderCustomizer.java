package top.infra.jackson2;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableList;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.OrderComparator;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

@Slf4j
public class DefaultJackson2ObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer {

    private final List<Jackson2Customizer> customizers;
    private final Jackson2Properties properties;

    public DefaultJackson2ObjectMapperBuilderCustomizer(final List<Jackson2Customizer> customizers, final Jackson2Properties properties) {
        this.customizers = customizers != null ?
            customizers.stream().sorted(OrderComparator.INSTANCE).collect(toList()) :
            ImmutableList.of();
        this.properties = properties;
    }

    @Override
    public void customize(final Jackson2ObjectMapperBuilder builder) {
        this.customizers.forEach(customizer -> customizer.customize(properties, builder));
        log.info("done.");
    }
}
