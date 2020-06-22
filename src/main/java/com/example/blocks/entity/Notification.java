package com.example.blocks.entity;

import lombok.Data;

@Data
public class Notification {
    private String content;

    public Notification(String content) {
        this.content = content;
    }
}