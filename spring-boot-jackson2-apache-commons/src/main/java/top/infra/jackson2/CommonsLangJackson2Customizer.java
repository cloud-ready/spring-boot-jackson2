package top.infra.jackson2;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

public class CommonsLangJackson2Customizer implements Jackson2Customizer {

    @Override
    public void customize(final Jackson2Properties properties, final ObjectMapper mapper) {
        final CommonsLangModule commonsLangModule = new CommonsLangModule();
        mapper.registerModule(commonsLangModule);
    }

    @Override
    public void customize(final Jackson2Properties properties, final Jackson2ObjectMapperBuilder builder) {
        final CommonsLangModule commonsLangModule = new CommonsLangModule();
        final List<Module> modules = this.modules(builder);
        modules.add(commonsLangModule);
        builder.modulesToInstall(modules.toArray(new Module[0]));
    }
}
