package top.infra.jackson2;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.core.OrderComparator;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import top.infra.common.ClassUtils;

/**
 * Created by zhanghaolun on 16/6/9.
 */
@Slf4j
public abstract class Jackson2Utils {

    private static Boolean JACKSON2_PRESENT;

    static {
        JACKSON2_PRESENT = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper")
            && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator");
    }

    private Jackson2Utils() {
    }

    /**
     * Setup a Mapper.
     *
     * @param properties   jackson2Properties
     * @param objectMapper ObjectMapper or XmlMapper to customize
     */
    public static void customize(final Jackson2Properties properties, final ObjectMapper objectMapper) {
        for (final Jackson2MapperCustomizer customizer : jackson2MapperCustomizers()) {
            log.info("customize objectMapper: '{}' using: '{}'.", objectMapper, customizer);
            customizer.customize(properties, objectMapper);
        }
    }

    public static <T> Function<String, T> fromJson( //
        final ObjectMapper objectMapper, //
        final TypeReference<T> typeReference //
    ) {
        return string -> {
            try {
                return objectMapper.readValue(string, typeReference);
            } catch (final IOException wrapped) {
                throw new RuntimeJsonProcessingException("error read from JSON.", wrapped);
            }
        };
    }

    /**
     * parse JSON.
     *
     * @param objectMapper objectMapper
     * @param type         type
     * @param <T>          type
     * @return object
     */
    public static <T> Function<String, T> fromJson( //
        final ObjectMapper objectMapper, //
        final Class<T> type //
    ) {
        return string -> {
            try {
                return objectMapper.readValue(string, type);
            } catch (final IOException wrapped) {
                throw new RuntimeJsonProcessingException("error read from JSON.", wrapped);
            }
        };
    }

    public static Boolean isJackson2Present() {
        return Jackson2Utils.JACKSON2_PRESENT;
    }

    static List<Jackson2MapperCustomizer> jackson2MapperCustomizers() {
        final List<Jackson2MapperCustomizerFactory> factories = jackson2MapperCustomizerFactories();

        return factories.stream()
            .map(factory -> factory.getObject().orElse(null))
            .filter(Objects::nonNull)
            .sorted(OrderComparator.INSTANCE)
            .collect(toList());
    }

    static List<Jackson2MapperCustomizerFactory> jackson2MapperCustomizerFactories() {
        final String basePackage = Jackson2Utils.class.getName().split("\\.")[0];

        final Set<Class<Jackson2MapperCustomizerFactory>> classes = FileAndClasspathUtils.scan(basePackage,
            new FileAndClasspathUtils.AssignableFilter(Jackson2MapperCustomizerFactory.class, false, false));

        log.info("found {} jackson2MapperCustomizerFactories.", classes.size());

        return classes.stream().map(BeanUtils::instantiate).collect(toList());
    }

    /**
     * to JSON.
     *
     * @param objectMapper objectMapper
     * @param <T>          type
     * @return JSON
     */
    public static <T> Function<T, String> toJson(final ObjectMapper objectMapper) {
        return object -> {
            try {
                if (object != null) {
                    return objectMapper.writeValueAsString(object);
                } else {
                    return "";
                }
            } catch (final JsonProcessingException wrapped) {
                throw new RuntimeJsonProcessingException("error serialize to JSON.", wrapped);
            }
        };
    }

    public static <T> String toJson(final ObjectMapper objectMapper, final T item) {
        return toJson(objectMapper).apply(item);
    }

    public static class RuntimeJsonProcessingException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public RuntimeJsonProcessingException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
