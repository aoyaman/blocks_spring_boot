package com.example.blocks.entity;

import lombok.Data;

@Data
public class Hand {
  private int x;
  private int y;
  private int blockType;
  private int angle;
  private boolean pass;

  public Hand() {
    this.x = 0;
    this.y = 0;
    this.blockType = 0;
    this.angle = 0;
    this.pass = false;
  }

  // なぜbooleanはgetter, setter自動生成されない？
  public void setPass(boolean isPass) {
    this.pass = isPass;
  }
  public boolean isPass() {
    return this.pass;
  }
}