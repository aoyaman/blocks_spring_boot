package com.example.blocks;

import lombok.Data;

@Data
public class Block {
    private Position[] positions;

    public Block(int[][] list) {
        positions = new Position[list.length];
        for (int i = 0; i < list.length; i++) {
            Position p = new Position();
            p.setX(list[i][0]);
            p.setY(list[i][1]);
            positions[i] = p;
        }
    }
}