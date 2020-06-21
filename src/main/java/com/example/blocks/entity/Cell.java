package com.example.blocks.entity;

import com.example.blocks.service.Color;

import lombok.Data;

@Data
public class Cell {
  String color;
  int blockType;
  public Cell(String color, int blockType) {
    this.color = color;
    this.blockType = blockType;
  }
  public boolean isDefault() {
    return this.color.equals(Color.DEFAULT);
  }
}
