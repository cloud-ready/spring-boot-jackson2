package top.infra.jackson2;

import static java.lang.Boolean.FALSE;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.Ordered;

import java.util.Optional;
import java.util.TimeZone;

import top.infra.common.ClassUtils;

/**
 * Created by zhanghaolun on 16/7/28.
 */
public interface Jackson2MapperCustomizer extends Ordered {

    String CLASS_XML_MAPPER = "com.fasterxml.jackson.dataformat.xml.XmlMapper";

    String PATTERN_JAVA_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    void customize(Jackson2Properties properties, ObjectMapper mapper);

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    default String dateFormat(final Jackson2Properties properties) {
        final String dateFormat;
        if (properties.getDateFormat() == null) {
            dateFormat = PATTERN_JAVA_ISO8601;
        } else {
            dateFormat = properties.getDateFormat();
        }
        return dateFormat;
    }

    default Boolean isXmlMapper(final ObjectMapper mapper) {
        final Optional<Class<?>> classOptional = ClassUtils.forName(CLASS_XML_MAPPER);
        return mapper != null && classOptional.map(xmlMapperClass -> xmlMapperClass.isAssignableFrom(mapper.getClass())).orElse(FALSE);
    }

    default TimeZone timeZone(final Jackson2Properties properties) {
        // Since Jackson 2.6.3 we always need to set a TimeZone (see
        // gh-4170). If none in our properties fallback to the Jackson's
        // default
        final TimeZone timeZone;
        if (properties.getTimeZone() == null) {
            //timeZone = DefaultTimeZone.DEFAULT_TIME_ZONE;
            timeZone = new ObjectMapper().getSerializationConfig().getTimeZone();
        } else {
            timeZone = properties.getTimeZone();
        }
        return timeZone;
    }
}
