package com.reimnop.swtools.module.dm_gui;

import io.wispforest.owo.config.annotation.Hook;

public class DmGuiConfig {
    public boolean enable = true;
    public boolean consumeMessages = true;

    @Hook
    public String incomingMessagePattern = "✉⬇ MSG \\((?<name>[a-zA-Z0-9_]{2,16}) ➺ Me\\): (?<message>.*)";

    @Hook
    public String outgoingMessagePattern = "✉⬆ MSG \\(Me ➺ (?<name>[a-zA-Z0-9_]{2,16})\\): (?<message>.*)";

    public String commandPattern = "msg ${name} ${message}";
}
