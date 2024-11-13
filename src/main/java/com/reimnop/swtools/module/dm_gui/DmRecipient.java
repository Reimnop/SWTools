package com.reimnop.swtools.module.dm_gui;

import com.reimnop.swtools.util.SWTEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DmRecipient {
    public final SWTEvent<DmMessage> messageAdded = new SWTEvent<>();
    public final SWTEvent<Integer> messageRemoved = new SWTEvent<>();
    public final SWTEvent<Integer> unreadMessagesCountChanged = new SWTEvent<>();

    private final String name;
    private final List<DmMessage> messages;
    private int unreadMessagesCount;

    public DmRecipient(String name) {
        this.name = name;
        messages = new ArrayList<>();
    }

    public DmRecipient(String name, Collection<DmMessage> messages) {
        this.name = name;
        this.messages = List.copyOf(messages);
    }

    public String getName() {
        return name;
    }

    public List<DmMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public void addMessage(DmMessage message) {
        messages.add(message);
        messageAdded.invoke(this, message);
    }

    public void removeMessage(int index) {
        messages.remove(index);
        messageRemoved.invoke(this, index);
    }

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int value) {
        if (value == unreadMessagesCount) {
            return;
        }
        unreadMessagesCount = value;
        unreadMessagesCountChanged.invoke(this, value);
    }
}
