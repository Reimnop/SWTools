package com.reimnop.swtools;

import com.reimnop.swtools.module.chat_filter.ChatFilterConfig;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = SWTools.MOD_ID)
@Config(name = SWTools.MOD_ID, wrapperName = "SWTConfig")
public class SWTConfigModel {
    @Nest
    public ChatFilterConfig chatFilter = new ChatFilterConfig();
}
