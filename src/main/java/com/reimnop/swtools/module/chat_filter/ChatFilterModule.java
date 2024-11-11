package com.reimnop.swtools.module.chat_filter;

import com.reimnop.swtools.SWTConfig;
import com.reimnop.swtools.module.BaseModule;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ChatFilterModule extends BaseModule {

    private final SWTConfig.ChatFilter config;
    private Pattern filterPattern;

    public ChatFilterModule(SWTConfig.ChatFilter config) {
        this.config = config;

        filterPattern = Pattern.compile(config.filterRegex());
        config.subscribeToFilterRegex(regex -> filterPattern = Pattern.compile(regex));
    }

    @Override
    public String getName() {
        return "Chat Filter";
    }

    @Override
    public boolean allowChatMessage(Text message) {
        var string = message.getString();
        var matcher = filterPattern.matcher(string);
        if (matcher.matches()) {
            return config.filterMode() == ChatFilterConfig.FilterMode.ALLOW;
        } else {
            return config.filterMode() == ChatFilterConfig.FilterMode.BLOCK;
        }
    }
}
