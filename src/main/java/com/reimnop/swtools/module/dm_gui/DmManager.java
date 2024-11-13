package com.reimnop.swtools.module.dm_gui;

import com.reimnop.swtools.util.SWTEvent;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DmManager {
    public final SWTEvent<DmRecipient> recipientAdded = new SWTEvent<>();
    public final SWTEvent<DmRecipient> recipientRemoved = new SWTEvent<>();
    public final SWTEvent<DmRecipient> currentRecipientChanged = new SWTEvent<>();

    private final Map<String, DmRecipient> recipients = new HashMap<>();

    @Nullable
    private DmRecipient currentRecipient;

    public Collection<DmRecipient> getRecipients() {
        return recipients.values();
    }

    public @Nullable DmRecipient getRecipient(String name) {
        return recipients.getOrDefault(name, null);
    }

    public boolean addRecipient(DmRecipient recipient) {
        if (recipients.containsKey(recipient.getName())) {
            return false;
        }
        recipients.put(recipient.getName(), recipient);
        recipientAdded.invoke(this, recipient);
        return true;
    }

    public boolean removeRecipient(String name) {
        var recipient = recipients.getOrDefault(name, null);
        if (recipient == null) {
            return false;
        }
        recipients.remove(name);
        recipientRemoved.invoke(this, recipient);
        return true;
    }

    public @Nullable DmRecipient getCurrentRecipient() {
        return currentRecipient;
    }

    public void setCurrentRecipient(@Nullable DmRecipient recipient) {
        if (recipient == currentRecipient) {
            return;
        }
        currentRecipient = recipient;
        currentRecipientChanged.invoke(this, currentRecipient);
    }

    public void sendMessageTo(DmRecipient recipient, String message) {
        var client = MinecraftClient.getInstance();
        var player = client.player;
        if (player == null) {
            return;
        }
        player.networkHandler.sendChatCommand(String.format("msg %s %s", recipient.getName(), message));
    }
}
