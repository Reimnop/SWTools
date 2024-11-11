package com.reimnop.swtools.module;

import net.minecraft.text.Text;

public abstract class BaseModule {
    public abstract String getName();
    public abstract boolean getActive();

    public boolean allowChatMessage(Text message) {
        return true;
    }
}
