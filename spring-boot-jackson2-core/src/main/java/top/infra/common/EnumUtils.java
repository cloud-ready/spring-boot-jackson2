package top.infra.common;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public abstract class EnumUtils {

    private static Method methodEnumConstantDirectory;

    static {
        try {
            //enumConstantDirectory is much faster than for-loop
            methodEnumConstantDirectory = Class.class.getDeclaredMethod("enumConstantDirectory");
            methodEnumConstantDirectory.setAccessible(true);
        } catch (final ReflectiveOperationException ex) {
            log.error("can not find Class.enumConstantDirectory, fall back to for-loop");
        }
    }

    private EnumUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E getEnumValue(final Class<E> enumClass, final String enumName) {
        try {
            final E result;
            if (methodEnumConstantDirectory != null) {
                final Map<String, E> constantDic = (Map<String, E>) methodEnumConstantDirectory.invoke(enumClass);
                result = constantDic.get(enumName);
            } else {
                result = Arrays.stream(enumClass.getEnumConstants())
                    .filter(constant -> constant.name().equals(enumName))
                    .findAny()
                    .orElse(null);
            }
            return result;
        } catch (final ReflectiveOperationException ignored) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E valueOf(final Class<E> enumClass, final String enumName) {
        try {
            final E result;
            final Method method = enumClass.getDeclaredMethod("valueOf", String.class);
            result = (E) method.invoke(enumClass, enumName);
            return result;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    public static Object objectValueOf(final Class<?> enumClass, final String enumName) {
        try {
            final Object result;
            final Method method = enumClass.getDeclaredMethod("valueOf", String.class);
            result = method.invoke(enumClass, enumName);
            return result;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
