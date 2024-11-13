package com.reimnop.swtools.module.chat_filter;

import io.wispforest.owo.config.annotation.Hook;

import java.util.List;

public class ChatFilterConfig {
    public boolean enable = true;
    @Hook
    public List<String> filterPatterns = List.of(
            "Transaria.*",
            "Nation ZilatraXR.*",
            " \\[L\\].*",
            "✉⬇ MSG.*",
            "✉⬆ MSG.*"
    );
    public FilterMode filterMode = FilterMode.ALLOW;

    public enum FilterMode {
        ALLOW,
        BLOCK
    }
}
