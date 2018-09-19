package top.infra.jackson2;

import static java.lang.Boolean.FALSE;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import top.infra.common.ClassUtils;

@Slf4j
public class HalJackson2Customizer implements Jackson2Customizer {

    @Override
    public void customize(final Jackson2Properties properties, final ObjectMapper mapper) {
        final Optional<Module> jackson2HalModule = this.jackson2HalModule();
        if (jackson2HalModule.isPresent() && !this.isAlreadyRegisteredIn(mapper, jackson2HalModule.get().getClass())) {
            mapper.registerModule(jackson2HalModule.get());
        }
    }

    @Override
    public void customize(final Jackson2Properties properties, final Jackson2ObjectMapperBuilder builder) {
        final Optional<Module> jackson2HalModule = this.jackson2HalModule();

        final List<Module> modules = this.modules(builder);
        if (jackson2HalModule.isPresent() && modules.stream().noneMatch(
            module -> jackson2HalModule.get().getClass().isAssignableFrom(module.getClass()))) {

            modules.add(jackson2HalModule.get());
            builder.modulesToInstall(modules.toArray(new Module[0]));
        }
    }

    public Boolean isAlreadyRegisteredIn(final ObjectMapper mapper, final Class<?> jackson2HalModuleClass) {
        try {
            final Boolean result;
            final Method methodIsAlreadyRegisteredIn = jackson2HalModuleClass.getDeclaredMethod(
                "isAlreadyRegisteredIn", ObjectMapper.class);
            result = (Boolean) methodIsAlreadyRegisteredIn.invoke(null, mapper);
            return result;
        } catch (final ReflectiveOperationException | SecurityException | IllegalArgumentException ex) {
            log.info("Jackson2HalModule config error", ex);
            return FALSE;
        }
    }

    public Optional<Module> jackson2HalModule() {
        try {
            final Optional<Class<?>> moduleClass = ClassUtils.forName(HalJackson2MapperCustomizerFactory.CLASS_JACKSON2_HAL_MODULE);
            final Object jackson2HalModule;
            if (moduleClass.isPresent()) {
                jackson2HalModule = moduleClass.get().newInstance();
            } else {
                jackson2HalModule = null;
            }
            return Optional.ofNullable((Module) jackson2HalModule);
        } catch (final ReflectiveOperationException ex) {
            log.info("Jackson2HalModule config error", ex);
            return Optional.empty();
        }
    }
}
