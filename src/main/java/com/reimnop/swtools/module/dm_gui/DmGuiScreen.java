package com.reimnop.swtools.module.dm_gui;

import com.reimnop.swtools.util.SWTUtil;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;

import java.util.Map;

public class DmGuiScreen extends BaseUIModelScreen<FlowLayout> {

    public DmGuiScreen() {
        super(FlowLayout.class, DataSource.asset(SWTUtil.id("dm_gui")));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        var messageContainer = rootComponent.childById(FlowLayout.class, "message-container");
        messageContainer.child(createMessageComponent("Lorem ipsum dolor sit amet,", "left"));
        messageContainer.child(createMessageComponent("consectetur adipiscing elit.", "right"));
    }

    private Component createMessageComponent(String text, String align) {
        return model.expandTemplate(FlowLayout.class, "message", Map.of(
                "text", text,
                "align", align));
    }
}
