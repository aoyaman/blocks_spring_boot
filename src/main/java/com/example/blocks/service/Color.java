package com.example.blocks.service;

public class Color {

  private static final String[][] COLOR = {
    // 赤
    {"ff0000", "ffffff", "赤", "ff7f50", "dc143c", "c71585", "ff1493", "db7093", "ffc0cb", "ee82ee", "ffb6c1", "8b008b", "8a2be2"},

    // 青
    {"0000ff", "ffffff", "青", "b0c4de", "4682b4", "4169e1", "87cefa", "00bfff", "afeeee", "e0ffff", "00ffff", "20b2aa", "40e0d0"},

    // 緑
    {"008000", "ffffff", "緑", "008b8b", "3cb371", "66cdaa", "8fbc8f", "7fffd4", "98fb98", "00ff7f", "32cd32", "9acd32", "808000"},

    // 黄
    {"ffff00", "ffffff", "黄", "f5deb3", "f0e68c", "ffd700", "ffa500", "a0522b", "d2691e", "e9967a", "fa8072", "b8860b", "cd853f"}
  };

  private static String getColor(int player, int index) {
    return COLOR[player - 1][index];
  }
  public static String getColor(int player) {
    return getColor(player, 0);
  }
  public static String getKouhoColor(int player) {
    return getColor(player, 1);
  }

  public static String getColorKanji(int player) {
    return getColor(player, 2);
  }

  public static final String DEFAULT = "d3d3d3";

}
