package com.example.blocks.entity;

import lombok.Data;

@Data
public class Message {
    private int selectBlock;
    private int id;
    private int x;
    private int y;
    private int angle;
    private boolean pass;
    private boolean flip;
}