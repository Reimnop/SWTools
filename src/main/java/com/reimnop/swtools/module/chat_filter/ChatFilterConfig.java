package com.reimnop.swtools.module.chat_filter;

import io.wispforest.owo.config.annotation.Hook;

public class ChatFilterConfig {
    @Hook
    public String filterRegex = ".*";
    public FilterMode filterMode = FilterMode.ALLOW;

    public enum FilterMode {
        ALLOW,
        BLOCK
    }
}
