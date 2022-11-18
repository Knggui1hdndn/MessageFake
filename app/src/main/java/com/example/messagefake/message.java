package com.example.messagefake;

import java.io.Serializable;

public class message implements Serializable {
    private String sendBy, text, time, chatAll, email;
    private boolean change;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public message(String sendBy, String text, String time, boolean change) {
        this.sendBy = sendBy;
        this.text = text;
        this.time = time;
        this.change = change;
    }

    public message(String sendBy, String text, String time) {
        this.sendBy = sendBy;
        this.text = text;
        this.time = time;
    }

    public String getChatAll() {
        return chatAll;
    }

    public void setChatAll(String chatAll) {
        this.chatAll = chatAll;
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    @Override
    public String toString() {
        return "message{" +
                "sendBy='" + sendBy + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public message() {
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
