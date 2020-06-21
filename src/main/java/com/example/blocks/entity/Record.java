package com.example.blocks.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Record {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  // ゲームID
  @Column(nullable = false)
  private Integer gameId;

  // 順番
  @Column(nullable = false)
  private Integer number;

  // ブロック
  @Column(nullable = true)
  private Integer blockType;

  // X
  @Column(nullable = true)
  private Integer x;

  // Y
  @Column(nullable = true)
  private Integer y;

  // 角度
  @Column(nullable = true)
  private Integer angle;

  // パス
  @Column(columnDefinition = "tinyint(1) default 0")
  private boolean pass;
}