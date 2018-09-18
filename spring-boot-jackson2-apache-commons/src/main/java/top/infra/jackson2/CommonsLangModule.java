package top.infra.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CommonsLangModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    @Override
    public void setupModule(final SetupContext context) {
        final ObjectMapper mapper = context.getOwner();
        final DeserializerFactory deserializerFactory = mapper.getDeserializationContext().getFactory();
        context.addDeserializers(new CommonsLangDeserializers(deserializerFactory));
        context.addSerializers(new CommonsLangSerializers());
    }

    // yes, will try to avoid duplicate registrations (if MapperFeature enabled)
    @Override
    public String getModuleName() {
        return getClass().getSimpleName();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        return this == o;
    }
}
