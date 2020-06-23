package com.example.blocks.entity;

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
    private Integer player;     // プレイヤー番号(1始まりs)

    private Integer x;          // 配置先のX座標
    private Integer y;          // 配置先のY座標
    private Integer angle;      // ブロックの向き

    private Integer gameId;     // ゲームID
    private Integer status;     // 状態

    private Integer flip;     // 反転させるかどうか
    public boolean isFlip() {
        return flip != null && flip == 1;
    }
    public void setFlip(boolean flip) {
        this.flip = flip ? 1 : 0;
    }

}
