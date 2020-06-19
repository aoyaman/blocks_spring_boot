package com.example.blocks;

import lombok.Data;

@Data
public class Kouho {
  private int x;
  private int y;
  private String[][] cells;
  public Kouho(int x, int y, String[][] cells) {
    this.x = x;
    this.y = y;
    this.cells = cells;
  }
}
