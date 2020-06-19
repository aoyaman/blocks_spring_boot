package com.example.blocks;

import lombok.Data;

@Data
public class Kouho {
  private int x;
  private int y;
  private String color;
  public Kouho(int x, int y, String color) {
    this.x = x;
    this.y = y;
    this.color = color;
  }
}
