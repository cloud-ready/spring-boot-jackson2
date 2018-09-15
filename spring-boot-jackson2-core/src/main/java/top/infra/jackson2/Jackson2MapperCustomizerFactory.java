package top.infra.jackson2;

import java.util.Collection;
import java.util.Optional;

import top.infra.common.ClassUtils;

public interface Jackson2MapperCustomizerFactory {

    String CLASS_JACKSON2_OBJECT_MAPPER_BUILDER = "org.springframework.http.converter.json.Jackson2ObjectMapperBuilder";

    Optional<Jackson2MapperCustomizer> getObject();

    default Optional<Jackson2MapperCustomizer> newInstanceIfPresent(
        final String customizerClass, final Collection<String> classesMustPresent) {
        try {
            final Optional<Jackson2MapperCustomizer> customizer;

            if (ClassUtils.isPresent(customizerClass) && classesMustPresent.stream().allMatch(ClassUtils::isPresent)) {
                final Optional<Class<?>> classOptional = ClassUtils.forName(customizerClass);
                if (classOptional.isPresent()) {
                    customizer = Optional.of((Jackson2MapperCustomizer) classOptional.get().newInstance());
                } else {
                    customizer = Optional.empty();
                }
            } else {
                customizer = Optional.empty();
            }

            return customizer;
        } catch (final ReflectiveOperationException ex) {
            return Optional.empty();
        }
    }
}
