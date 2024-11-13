package com.reimnop.swtools.module.dm_gui;

import com.reimnop.swtools.module.BaseModule;
import com.reimnop.swtools.SWTConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.regex.Pattern;

public class DmGuiModule extends BaseModule {
    private final SWTConfig.DmGui config;
    private final KeyBinding openDmGuiKey;
    private final DmManager dmManager;

    private Pattern incomingMessagePattern;
    private Pattern outgoingMessagePattern;

    public DmGuiModule(SWTConfig.DmGui config) {
        this.config = config;
        this.openDmGuiKey = new KeyBinding(
                "key.swtools.open_dm_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.swtools");
        this.dmManager = new DmManager();

        incomingMessagePattern = Pattern.compile(config.incomingMessagePattern());
        outgoingMessagePattern = Pattern.compile(config.outgoingMessagePattern());
    }

    @Override
    public void initialize() {
        super.initialize();

        KeyBindingHelper.registerKeyBinding(openDmGuiKey);

        config.subscribeToIncomingMessagePattern(pattern -> incomingMessagePattern = Pattern.compile(pattern));
        config.subscribeToOutgoingMessagePattern(pattern -> outgoingMessagePattern = Pattern.compile(pattern));
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
    public boolean allowChatMessage(Text message) {
        var text = message.getString();

        var incomingMatcher = incomingMessagePattern.matcher(text);
        if (incomingMatcher.matches()) {
            var name = incomingMatcher.group("name");
            var messageContent = incomingMatcher.group("message");

            var recipient = dmManager.getRecipient(name);

            // create new recipient if we don't have it yet
            if (recipient == null) {
                recipient = new DmRecipient(name);
                dmManager.addRecipient(recipient);
            }

            recipient.addMessage(new DmMessage(messageContent, DmMessage.Sender.OTHER));
            return !config.consumeMessages();
        }

        var outgoingMatcher = outgoingMessagePattern.matcher(text);
        if (outgoingMatcher.matches()) {
            var name = outgoingMatcher.group("name");
            var messageContent = outgoingMatcher.group("message");

            var recipient = dmManager.getRecipient(name);

            // create new recipient if we don't have it yet
            if (recipient == null) {
                recipient = new DmRecipient(name);
                dmManager.addRecipient(recipient);
            }

            recipient.addMessage(new DmMessage(messageContent, DmMessage.Sender.THIS));
            return !config.consumeMessages();
        }

        return true;
    }

    @Override
    public void onTick(MinecraftClient client) {
        super.onTick(client);

        if (client.player == null) {
            return;
        }

        if (openDmGuiKey.wasPressed() && client.currentScreen == null) {
            client.setScreen(new DmGuiScreen(dmManager));
        }
    }
}
