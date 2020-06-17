package com.example.blocks;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Block {
    public static final int STATUS_NOT_SETTED = 0;  // 未セット
    public static final int STATUS_SETTED = 1;      // セット済み
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer blockType;  // ブロックの種類
    private Integer color;      // 色

    private Integer x;          // 配置先のX座標
    private Integer y;          // 配置先のY座標
    private Integer angle;      // ブロックの向き

    private Integer gameId;     // ゲームID
    private Integer status;     // 状態

}
