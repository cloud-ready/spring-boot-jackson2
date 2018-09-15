package top.infra.jackson2;

import static com.google.common.collect.Lists.newLinkedList;

import com.fasterxml.jackson.databind.Module;

import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by zhanghaolun on 16/7/28.
 */
public interface Jackson2BuilderCustomizer extends Ordered {

    void customize(Jackson2Properties properties, Jackson2ObjectMapperBuilder builder);

    default boolean createXmlMapper(final Jackson2ObjectMapperBuilder builder) {
        try {
            final boolean result;
            final Field fieldModules = builder.getClass().getDeclaredField("createXmlMapper");
            fieldModules.setAccessible(true);
            result = (boolean) fieldModules.get(builder);
            return result;
        } catch (final NoSuchFieldException | IllegalAccessException | IllegalArgumentException ex) {
            return false;
        }
    }


    default Boolean isXmlMapper(final Jackson2ObjectMapperBuilder builder) {
        return this.createXmlMapper(builder);
    }

    @SuppressWarnings("unchecked")
    default List<Module> modules(final Jackson2ObjectMapperBuilder builder) {
        try {
            final List<Module> result;
            final Field fieldModules = builder.getClass().getDeclaredField("modules");
            fieldModules.setAccessible(true);
            result = (List<Module>) fieldModules.get(builder);
            return newLinkedList(result);
        } catch (final NoSuchFieldException | IllegalAccessException | IllegalArgumentException ex) {
            return newLinkedList();
        }
    }
}
