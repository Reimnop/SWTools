package com.reimnop.swtools.util;

import com.reimnop.swtools.SWTools;
import net.minecraft.util.Identifier;

public class SWTUtil {
    public static Identifier id(String path) {
        return Identifier.of(SWTools.MOD_ID, path);
    }
}
