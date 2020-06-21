package com.example.blocks.entity;

import lombok.Data;

/**
 * プレイヤー情報（表示用)
 */
@Data
public class PlayerInfo {
  private String name;
  private String color;
  private int blockZansu;
  private boolean pass;
  public boolean isPass() {
    return this.pass;
  }
}