package com.eduardowanderley.model;

import java.time.LocalDateTime;

public class Message {
    private String sender;
    private String content;
    private String room;
    private LocalDateTime timestamp;
    private MessageType type;
    private String filePath;

    public enum MessageType {
        TEXT,
        FILE
    }

    public Message(String sender, String content, String room, MessageType type) {
        this.sender = sender;
        this.content = content;
        this.room = room;
        this.timestamp = LocalDateTime.now();
        this.type = type;
    }

    // Getters e Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
} 