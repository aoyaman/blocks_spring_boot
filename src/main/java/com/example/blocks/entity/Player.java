package com.example.blocks.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Player {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  // ゲームID
  @Column(nullable = false)
  private Integer gameId;

  // プレイヤー番号(何番目か)
  @Column(nullable = false)
  private Integer number;

  // アカウントのユーザ名(コンピュータの時はnullが入る)
  @Column(nullable = true)
  private String accountName;

  // コンピュータ番号(ユーザの時はnullが入る)
  @Column(nullable = true)
  private String cpu;

  // 残ブロック数
  @Column(nullable = false)
  private Integer zanBlockCount;

  // 手詰まりかどうか
  @Column(columnDefinition = "tinyint(1) default 0")
  private boolean pass;

  /**
   * 名前を返す
   */
  public String getName() {
    if (accountName != null) {
      return accountName;
    }
    return cpu;
  }

}