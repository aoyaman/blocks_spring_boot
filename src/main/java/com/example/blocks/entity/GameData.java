package com.example.blocks.entity;

import java.util.List;

import lombok.Data;

@Data
public class GameData {
  private String error;
  private String[][] cells;
  private Cell[][] nexts;
  private int nowPlayer;
  private String nowPlayerColor;
  private boolean isLoginUserNow;
  private boolean isLoginUserPass;
  private String nowPlayerName;
  private List<PlayerInfo> playersInfo;
  private boolean isAllPass;
  private PlayerInfo minPlayer;
}
