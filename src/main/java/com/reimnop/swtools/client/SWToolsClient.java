package com.reimnop.swtools.client;

import com.reimnop.swtools.SWTConfig;
import com.reimnop.swtools.util.SWTStreamUtil;
import com.reimnop.swtools.SWTools;
import com.reimnop.swtools.module.BaseModule;
import com.reimnop.swtools.module.chat_filter.ChatFilterModule;
import com.reimnop.swtools.module.dm_gui.DmGuiModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SWToolsClient implements ClientModInitializer {

    private List<BaseModule> modules;

    @Override
    public void onInitializeClient() {
        modules = loadModules(SWTools.CONFIG);

        SWTools.LOGGER.info("Loaded {} modules:", modules.size());
        for (var module : modules) {
            SWTools.LOGGER.info(" - {}, active: {}", module.getName(), module.getActive());
        }

        ClientReceiveMessageEvents.ALLOW_CHAT.register(((text, signedMessage, gameProfile, parameters, instant) -> {
            for (var module : SWTStreamUtil.wrapStream(streamActiveModules())) {
                if (!module.allowChatMessage(text)) {
                    return false;
                }
            }
            return true;
        }));

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if (overlay) {
                return true;
            }
            for (var module : SWTStreamUtil.wrapStream(streamActiveModules())) {
                if (!module.allowChatMessage(message)) {
                    return false;
                }
            }
            return true;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (var module : SWTStreamUtil.wrapStream(streamActiveModules())) {
                module.onTick(client);
            }
        });
    }

    private Stream<BaseModule> streamActiveModules() {
        return modules.stream().filter(BaseModule::getActive);
    }

    private static List<BaseModule> loadModules(SWTConfig config) {
        var modules = new ArrayList<BaseModule>();
        modules.add(new ChatFilterModule(config.chatFilter));
        modules.add(new DmGuiModule(config.dmGui));

        // initialize modules
        modules.forEach(BaseModule::initialize);

        return modules;
    }
}
