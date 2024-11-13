package com.reimnop.swtools.module.dm_gui;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.OwoUIDrawContext;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.PlayerSkinDrawer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DmPlayerHeadComponent extends BaseComponent {
    @Nullable
    private final String playerName;

    public DmPlayerHeadComponent(@Nullable String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        if (playerName == null) {
            return;
        }

        // we're gonna use SkullBlockEntity to get profile
        // since that already has a fetcher
        var profilePromise = SkullBlockEntity.fetchProfileByName(playerName);
        var profileOptional = profilePromise.getNow(Optional.empty());
        var profile = profileOptional.orElse(null);

        if (profile == null) {
            return;
        }

        // get skin textures
        var client = MinecraftClient.getInstance();
        var skinTextures = client.getSkinProvider().getSkinTextures(profile);

        // draw head
        PlayerSkinDrawer.draw(context, skinTextures, x, y, Math.min(width, height));
    }
}
