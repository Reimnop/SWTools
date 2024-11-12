package com.reimnop.swtools.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public abstract class BaseModule {
    public abstract String getName();
    public abstract boolean getActive();

    public void initialize() {}
    public boolean allowChatMessage(Text message) { return true; }
    public void onTick(MinecraftClient client) {}
}
