package com.reimnop.swtools.util;

import com.reimnop.swtools.SWTools;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.regex.Pattern;

public class SWTUtil {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([a-z]\\.[a-z]+)}");

    public static Identifier id(String path) {
        return Identifier.of(SWTools.MOD_ID, path);
    }

    public static String replacePlaceholder(String value, Function<String, String> replacer) {
        return PLACEHOLDER_PATTERN.matcher(value).replaceAll(mr -> replacer.apply(mr.group(1)));
    }
}
