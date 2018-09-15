package top.infra.jackson2;

import com.google.common.collect.Lists;

import java.util.Optional;

public class JodaTimeJackson2MapperCustomizerFactory implements Jackson2MapperCustomizerFactory {

    static final String CLASS_DATE_TIME = "org.joda.time.DateTime";
    static final String CLASS_DATE_TIME_SERIALIZER = "com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer";
    static final String CLASS_JACKSON_JODA_DATE_FORMAT = "com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat";

    @Override
    public Optional<Jackson2MapperCustomizer> getObject() {
        return this.newInstanceIfPresent(
            "top.infra.jackson2.JodaTimeJackson2Customizer",
            Lists.newArrayList(
                CLASS_DATE_TIME,
                CLASS_DATE_TIME_SERIALIZER,
                CLASS_JACKSON_JODA_DATE_FORMAT,
                CLASS_JACKSON2_OBJECT_MAPPER_BUILDER
            ));
    }
}
