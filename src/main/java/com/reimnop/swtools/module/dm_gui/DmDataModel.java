package com.reimnop.swtools.module.dm_gui;

import java.util.List;

public class DmDataModel {
    private List<Recipient> recipients;

    public DmDataModel(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public static class Recipient {
        private String name;
        private List<Message> messages;

        public Recipient(String name, List<Message> messages) {
            this.name = name;
            this.messages = messages;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

        public static class Message {
            private String content;
            private Sender sender;

            public Message(String content, Sender sender) {
                this.content = content;
                this.sender = sender;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Sender getSender() {
                return sender;
            }

            public void setSender(Sender sender) {
                this.sender = sender;
            }

            public enum Sender {
                THIS,
                OTHER
            }
        }
    }
}
