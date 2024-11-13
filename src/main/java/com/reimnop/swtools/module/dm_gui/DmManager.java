package com.reimnop.swtools.module.dm_gui;

import com.google.gson.Gson;
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
        if (recipient == currentRecipient) {
            setCurrentRecipient(null);
        }
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

    public String save() {
        var dataModel = new DmDataModel(
                recipients.values().stream()
                        .map(recipient -> new DmDataModel.Recipient(
                                recipient.getName(),
                                recipient.getMessages().stream()
                                        .map(message -> new DmDataModel.Recipient.Message(
                                                message.content(),
                                                message.sender() == DmMessage.Sender.THIS
                                                        ? DmDataModel.Recipient.Message.Sender.THIS
                                                        : DmDataModel.Recipient.Message.Sender.OTHER
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
        var gson = new Gson();
        return gson.toJson(dataModel);
    }

    public void load(String value) {
        for (var recipient : recipients.values()) {
            recipientRemoved.invoke(this, recipient);
        }
        recipients.clear();

        var gson = new Gson();
        var dataModel = gson.fromJson(value, DmDataModel.class);
        for (var recipient : dataModel.getRecipients()) {
            recipients.put(
                    recipient.getName(),
                    new DmRecipient(
                            recipient.getName(),
                            recipient.getMessages().stream()
                                    .map(message -> new DmMessage(
                                            message.getContent(),
                                            message.getSender() == DmDataModel.Recipient.Message.Sender.THIS
                                                    ? DmMessage.Sender.THIS
                                                    : DmMessage.Sender.OTHER))
                                    .toList()
                    )
            );
        }

        for (var recipient : recipients.values()) {
            recipientAdded.invoke(this, recipient);
        }
    }
}
