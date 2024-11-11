package com.reimnop.swtools.module;

import net.minecraft.text.Text;

public abstract class BaseModule {
    private boolean active;

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract String getName();

    public boolean allowChatMessage(Text message) {
        return true;
    }
}
