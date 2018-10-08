package top.infra.jackson2;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

public class JavaTimeModuleCustomizer implements Jackson2MapperCustomizer, Jackson2BuilderCustomizer {

    @Override
    public void customize(final Jackson2Properties properties, final Jackson2ObjectMapperBuilder builder) {
        final Module javaTimeModule = new JavaTimeModule();
        final List<Module> modules = this.modules(builder);
        modules.add(javaTimeModule);
        builder.modulesToInstall(modules.toArray(new Module[0]));
    }

    @Override
    public void customize(final Jackson2Properties properties, final ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
    }
}
