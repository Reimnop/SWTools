package com.reimnop.swtools;

import com.reimnop.swtools.module.chat_filter.ChatFilterConfig;
import com.reimnop.swtools.module.dm_gui.DmGuiConfig;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = SWTools.MOD_ID)
@Config(name = SWTools.MOD_ID, wrapperName = "SWTConfig")
public class SWTConfigModel {
    @Nest
    public ChatFilterConfig chatFilter = new ChatFilterConfig();

    @Nest
    public DmGuiConfig dmGui = new DmGuiConfig();
}
