package com.reimnop.swtools.module.chat_filter;

import com.reimnop.swtools.SWTConfig;
import com.reimnop.swtools.module.BaseModule;
import net.minecraft.text.Text;

import java.util.List;
import java.util.regex.Pattern;

public class ChatFilterModule extends BaseModule {
    private final SWTConfig.ChatFilter config;
    private List<Pattern> filterPatterns;

    public ChatFilterModule(SWTConfig.ChatFilter config) {
        this.config = config;

        filterPatterns = config.filterPatterns().stream()
                .map(Pattern::compile)
                .toList();
    }

    @Override
    public void initialize() {
        super.initialize();

        config.subscribeToFilterPatterns(filterPatterns ->
                this.filterPatterns = filterPatterns.stream()
                        .map(Pattern::compile)
                        .toList());
    }

    @Override
    public boolean getActive() {
        return config.enable();
    }

    @Override
    public String getName() {
        return "Chat Filter";
    }

    @Override
    public boolean allowChatMessage(Text message) {
        var string = message.getString();
        if (matchEither(string, filterPatterns)) {
            return config.filterMode() == ChatFilterConfig.FilterMode.ALLOW;
        } else {
            return config.filterMode() == ChatFilterConfig.FilterMode.BLOCK;
        }
    }

    private static boolean matchEither(String string, List<Pattern> patterns) {
        return patterns.stream().anyMatch(pattern -> pattern.matcher(string).matches());
    }
}
