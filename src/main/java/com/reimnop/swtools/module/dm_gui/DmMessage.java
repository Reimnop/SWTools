package com.reimnop.swtools.module.dm_gui;

public record DmMessage(String content, Sender sender) {
    public enum Sender {
        THIS,
        OTHER
    }
}
