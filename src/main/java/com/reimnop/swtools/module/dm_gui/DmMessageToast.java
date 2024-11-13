package com.reimnop.swtools.module.dm_gui;

import com.reimnop.swtools.util.SWTTimeout;
import io.wispforest.owo.ui.base.BaseOwoToast;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@SuppressWarnings("UnstableApiUsage")
public class DmMessageToast extends BaseOwoToast<FlowLayout> {
    public DmMessageToast(String playerName, String message) {
        super(() -> DmMessageToast.buildUi(playerName, message), new SWTTimeout<>(5000));
    }

    private static FlowLayout buildUi(String playerName, String message) {
        return (FlowLayout) (Containers.horizontalFlow(
                    Sizing.fixed(160),
                    Sizing.fixed(32)
                )
                .child(
                        new DmPlayerHeadComponent(playerName)
                                .sizing(
                                        Sizing.fixed(20),
                                        Sizing.fixed(20)
                                )
                )
                .child(
                        Containers
                                .verticalFlow(
                                        Sizing.expand(100),
                                        Sizing.fill(100)
                                )
                                .child(
                                        Components
                                                .label(Text.of(playerName))
                                                .color(Color.BLACK)
                                                .sizing(
                                                    Sizing.expand(100),
                                                    Sizing.content()
                                                )
                                )
                                .child(
                                        Components
                                                .label(Text.of(message))
                                                .color(Color.ofFormatting(Formatting.DARK_GRAY))
                                                .sizing(
                                                        Sizing.expand(100),
                                                        Sizing.content()
                                                )
                                )
                )
                .gap(4)
                .padding(Insets.of(6))
                .surface(Surface.PANEL));
    }
}
