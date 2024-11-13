package com.reimnop.swtools.module.dm_gui;

import com.reimnop.swtools.util.SWTUtil;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.ParentComponent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class DmGuiScreen extends BaseUIModelScreen<FlowLayout> {
    private record UIRecipient(DmRecipient recipient, Component component) {}

    private final DmManager dmManager;
    private final Map<String, UIRecipient> uiRecipients = new HashMap<>();

    private String currentRecipientSearch = "";
    private String currentMessageBoxText = "";

    private FlowLayout recipientContainer;
    private FlowLayout messageContainer;
    private TextBoxComponent messageBox;
    private ButtonComponent sendButton;

    @Nullable
    private DmRecipient currentRecipient;

    public DmGuiScreen(DmManager dmManager) {
        super(FlowLayout.class, DataSource.asset(SWTUtil.id("dm_gui")));
        this.dmManager = dmManager;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        // initialize UI
        recipientContainer = rootComponent.childById(FlowLayout.class, "recipient-container");
        for (var recipient : dmManager.getRecipients()) {
            var component = createRecipientComponent(recipient.getName());
            recipientContainer.child(component);
            uiRecipients.put(recipient.getName(), new UIRecipient(recipient, component));

            var parentComponent = (ParentComponent) component;
            var switchTo = parentComponent.childById(ButtonComponent.class, "switch-to");
            switchTo.onPress((button) -> dmManager.setCurrentRecipient(recipient));
        }

        messageContainer = rootComponent.childById(FlowLayout.class, "message-container");
        messageBox = rootComponent.childById(TextBoxComponent.class, "message-box");
        sendButton = rootComponent.childById(ButtonComponent.class, "send-button");

        messageBox.keyPress().subscribe((keyCode, scanCode, modifiers) -> {
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                submitMessage();
                return true;
            }
            return false;
        });

        sendButton.onPress(button -> submitMessage());

        // subscribe to UI events
        var recipientSearch = rootComponent.childById(TextBoxComponent.class, "recipient-search");
        recipientSearch.onChanged().subscribe(value -> currentRecipientSearch = value);

        var recipientAdd = rootComponent.childById(ButtonComponent.class, "recipient-add");
        recipientAdd.onPress((button) -> {
            if (currentRecipientSearch.isBlank()) {
                return;
            }

            // check if we have a legal name
            if (!currentRecipientSearch.matches("[a-zA-Z0-9_]{2,16}")) {
                return;
            }

            // TODO: notify user about duplicate name
            dmManager.addRecipient(new DmRecipient(currentRecipientSearch));
        });

        messageBox.onChanged().subscribe(value -> currentMessageBoxText = value);

        // subscribe to events
        dmManager.recipientAdded.addListener(this::onRecipientAdded);
        dmManager.recipientRemoved.addListener(this::onRecipientRemoved);
        dmManager.currentRecipientChanged.addListener(this::onCurrentRecipientChanged);

        // switch to current recipient
        switchToRecipient(dmManager.getCurrentRecipient());
    }

    @Override
    public void close() {
        super.close();
        dmManager.recipientAdded.removeListener(this::onRecipientAdded);
        dmManager.recipientRemoved.removeListener(this::onRecipientRemoved);
        dmManager.currentRecipientChanged.removeListener(this::onCurrentRecipientChanged);

        if (currentRecipient != null) {
            currentRecipient.messageAdded.removeListener(this::onMessageAdded);
            currentRecipient.messageRemoved.removeListener(this::onMessageRemoved);
        }
    }

    private void submitMessage() {
        if (currentMessageBoxText.isBlank()) {
            return;
        }

        if (currentRecipient == null) {
            return;
        }

        dmManager.sendMessageTo(currentRecipient, currentMessageBoxText);
        messageBox.text("");
    }

    private void onCurrentRecipientChanged(Object sender, @Nullable DmRecipient recipient) {
        switchToRecipient(recipient);
    }

    private void switchToRecipient(@Nullable DmRecipient recipient) {
        if (recipient != currentRecipient) {
            messageBox.text("");
        }

        if (currentRecipient != null) {
            currentRecipient.messageAdded.removeListener(this::onMessageAdded);
            currentRecipient.messageRemoved.removeListener(this::onMessageRemoved);
        }

        messageContainer.clearChildren();
        currentRecipient = recipient;

        if (recipient != null) {
            for (var message : recipient.getMessages()) {
                messageContainer.child(createMessageComponent(message.content(), message.sender() == DmMessage.Sender.THIS ? "right" : "left"));
            }
            recipient.messageAdded.addListener(this::onMessageAdded);
            recipient.messageRemoved.addListener(this::onMessageRemoved);
            messageBox.active = true;
            sendButton.active = true;
        } else {
            messageBox.active = false;
            sendButton.active = false;
        }
    }

    private void onMessageAdded(Object sender, DmMessage message) {
        var recipient = (DmRecipient) sender;
        if (recipient != currentRecipient) {
            return;
        }
        messageContainer.child(createMessageComponent(message.content(), message.sender() == DmMessage.Sender.THIS ? "right" : "left"));
    }

    private void onMessageRemoved(Object sender, Integer index) {
        var recipient = (DmRecipient) sender;
        if (recipient != currentRecipient) {
            return;
        }
        // TODO: Implement this if message removal will be a thing
    }

    private void onRecipientAdded(Object sender, DmRecipient recipient) {
        var component = createRecipientComponent(recipient.getName());
        recipientContainer.child(component);
        uiRecipients.put(recipient.getName(), new UIRecipient(recipient, component));

        var parentComponent = (ParentComponent) component;
        var switchTo = parentComponent.childById(ButtonComponent.class, "switch-to");
        switchTo.onPress((button) -> dmManager.setCurrentRecipient(recipient));
    }

    private void onRecipientRemoved(Object sender, DmRecipient recipient) {
        var uiRecipient = uiRecipients.getOrDefault(recipient.getName(), null);
        if (uiRecipient == null) {
            return;
        }
        uiRecipient.component.remove();
        uiRecipients.remove(recipient.getName());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private Component createMessageComponent(String text, String align) {
        return model.expandTemplate(FlowLayout.class, "message", Map.of(
                "text", text,
                "align", align
        ));
    }

    private Component createRecipientComponent(String name) {
        return model.expandTemplate(FlowLayout.class, "recipient", Map.of(
                "name", name
        ));
    }
}
