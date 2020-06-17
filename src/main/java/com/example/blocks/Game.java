package com.example.blocks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String date;        // 日時
  private Integer nowPlayer;  // 現在操作中のプレイヤー

}
