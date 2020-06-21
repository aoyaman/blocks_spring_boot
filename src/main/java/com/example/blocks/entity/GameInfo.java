package com.example.blocks.entity;

import java.util.List;

import lombok.Data;

/**
 * ゲームの情報（表示用）
 */
@Data
public class GameInfo {
  private Integer id;
  private String date;
  private String author;
  private List<Player> players;
}