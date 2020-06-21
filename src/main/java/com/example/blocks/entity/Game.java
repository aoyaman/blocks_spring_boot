package com.example.blocks.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Game {
  public static final int PLAYER_NUM = 4;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String date;        // 日時
  private Integer nowPlayer;  // 現在操作中のプレイヤー
  private Integer counter;    // 手順のカウンター

  private String author;      // ゲーム開始者

  public void goNextPlayer() {
    nowPlayer++;
    if (nowPlayer > PLAYER_NUM) {
      nowPlayer = 1;
    }
    counter++;
  }

}
