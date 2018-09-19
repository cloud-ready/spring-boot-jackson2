package top.infra.jackson2;

import static top.infra.jackson2.JaxbAnnotationJackson2MapperCustomizerFactory.CLASS_JAXB_ANNOTATION_INTROSPECTOR;
import static top.infra.jackson2.JaxbAnnotationJackson2MapperCustomizerFactory.CLASS_JAXB_ANNOTATION_MODULE;
import static top.infra.jackson2.JaxbAnnotationJackson2MapperCustomizerFactory.CLASS_JAXB_ANNOTATION_MODULE_PRIORITY;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import top.infra.common.ClassUtils;
import top.infra.common.EnumUtils;


/**
 * see: http://wiki.fasterxml.com/JacksonJAXBAnnotations see: https://github.com/FasterXML/jackson-module-jaxb-annotations
 */
@Slf4j
public class JaxbAnnotationJackson2Customizer implements Jackson2Customizer {

    @Override
    public void customize(final Jackson2Properties properties, final ObjectMapper mapper) {
        final Boolean isXmlMapper = this.isXmlMapper(mapper);

        if (properties.getJaxb().getEnabled()) {
            final Optional<Module> module = this.jaxbAnnotationModule(isXmlMapper);
            module.ifPresent(mapper::registerModule);
        }
    }

    @Override
    public void customize(final Jackson2Properties properties, final Jackson2ObjectMapperBuilder builder) {
        final Boolean isXmlMapper = this.isXmlMapper(builder);

        if (properties.getJaxb().getEnabled()) {
            final Optional<Module> jaxbAnnotationModule = this.jaxbAnnotationModule(isXmlMapper);
            if (jaxbAnnotationModule.isPresent()) {
                final List<Module> modules = this.modules(builder);
                modules.add(jaxbAnnotationModule.get());
                builder.modulesToInstall(modules.toArray(new Module[0]));
            }
        }
    }

    private Optional<Module> jaxbAnnotationModule(final Boolean isXmlMapper) {
        final Optional<Module> jaxbAnnotationModule = this.jaxbAnnotationModule();

        final Optional<Object> priority = this.priority(isXmlMapper);
        if (jaxbAnnotationModule.isPresent() && priority.isPresent()) {
            this.setJaxbAnnotationModulePriority(jaxbAnnotationModule.get(), priority.get());
        }

        return jaxbAnnotationModule;
    }

    private Optional<Module> jaxbAnnotationModule() {
        try {
            final Optional<Module> module;
            final Optional<Class<?>> moduleClass = ClassUtils.forName(CLASS_JAXB_ANNOTATION_MODULE);
            final Optional<Class<?>> introspectorClass = ClassUtils.forName(CLASS_JAXB_ANNOTATION_INTROSPECTOR);
            if (moduleClass.isPresent() && introspectorClass.isPresent()) {
                module = Optional.of((Module) moduleClass.get().getConstructor(introspectorClass.get()) //
                    .newInstance(new HackedJackson2JaxbAnnotationIntrospector()));
            } else {
                module = Optional.empty();
            }

            return module;
        } catch (final ReflectiveOperationException ex) {
            log.info("JaxbAnnotationModule config error", ex);

            return Optional.empty();
        }
    }

    private Optional<Object> priority(final Boolean isXmlMapper) {
        final Optional<Object> priority;

        final String priorityName = isXmlMapper ? "PRIMARY" : "SECONDARY";

        final Class<?> enumType = ClassUtils.forName(CLASS_JAXB_ANNOTATION_MODULE_PRIORITY).orElse(null);
        priority = Optional.ofNullable(enumType != null ? EnumUtils.objectValueOf(enumType, priorityName) : null);
        return priority;
    }

    private void setJaxbAnnotationModulePriority(final Module jaxbAnnotationModule, final Object priority) {
        try {
            final Class<?> enumType = ClassUtils.forName(CLASS_JAXB_ANNOTATION_MODULE_PRIORITY).orElse(null);
            final Method setPriorityMethod = jaxbAnnotationModule.getClass().getDeclaredMethod("setPriority", enumType);
            setPriorityMethod.invoke(jaxbAnnotationModule, priority);
        } catch (final ReflectiveOperationException ex) {
            log.info("JaxbAnnotationModule config error", ex);
        }
    }
}
