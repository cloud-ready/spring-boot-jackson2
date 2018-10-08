package top.infra.jackson2;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

public class Jdk8ModuleCustomizer implements Jackson2MapperCustomizer, Jackson2BuilderCustomizer {

    @Override
    public void customize(final Jackson2Properties properties, final Jackson2ObjectMapperBuilder builder) {
        final Module jdk8Module = new Jdk8Module();
        final List<Module> modules = this.modules(builder);
        modules.add(jdk8Module);
        builder.modulesToInstall(modules.toArray(new Module[0]));
    }

    @Override
    public void customize(final Jackson2Properties properties, final ObjectMapper mapper) {
        mapper.registerModule(new Jdk8Module());
    }
}
