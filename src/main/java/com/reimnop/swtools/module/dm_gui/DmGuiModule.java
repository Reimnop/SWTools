package com.reimnop.swtools.module.dm_gui;

import com.reimnop.swtools.module.BaseModule;
import com.reimnop.swtools.SWTConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class DmGuiModule extends BaseModule {
    private final SWTConfig.DmGui config;
    private final KeyBinding openDmGuiKey;

    public DmGuiModule(SWTConfig.DmGui config) {
        this.config = config;
        this.openDmGuiKey = new KeyBinding(
                "key.swtools.open_dm_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.swtools");
    }

    @Override
    public void initialize() {
        KeyBindingHelper.registerKeyBinding(openDmGuiKey);
    }

    @Override
    public String getName() {
        return "DM GUI";
    }

    @Override
    public boolean getActive() {
        return config.enable();
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        if (openDmGuiKey.wasPressed() && client.currentScreen == null) {
            client.setScreen(new DmGuiScreen());
        }
    }
}
