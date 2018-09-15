package top.infra.test;

import com.google.common.base.Ascii;

import org.apache.commons.lang3.StringUtils;

import top.infra.test.classloader.ClassUtils;

public class GuavaAndCommonUser {

    public static String toUpperCase(String str) {
        ClassUtils.printClassLoader(GuavaAndCommonUser.class);
        ClassUtils.printClassLoader(Ascii.class);
        ClassUtils.printClassLoader(StringUtils.class);

        if (!StringUtils.isAllUpperCase(str)) {
            return Ascii.toUpperCase(str);
        }
        return str;
    }
}
