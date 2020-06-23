package com.example.blocks.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.example.blocks.entity.Block;
import com.example.blocks.entity.Cell;
import com.example.blocks.entity.Game;
import com.example.blocks.entity.GameData;
import com.example.blocks.entity.Hand;
import com.example.blocks.entity.Player;
import com.example.blocks.entity.PlayerInfo;
import com.example.blocks.repository.AccountRepository;
import com.example.blocks.repository.BlockRepository;
import com.example.blocks.repository.GameRepository;
import com.example.blocks.repository.PlayerRepository;
import com.example.blocks.repository.RecordRepository;
import com.example.blocks.service.Color;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*") // ★変更点
@RequestMapping("api/sample")
public class RestApiController {


  private final static int[][][] BLOCK_SHAPE = { { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }, // 0:四角
      { { 0, 0 } }, // 1:１小竹の
      { { 1, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 } }, // 2:逆T(短かい方)
      { { 0, 0 }, { 1, 0 } }, // 3:２連続
      { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 } }, // 4:４連続
      { { 0, 0 }, { 1, 0 }, { 1, 1 } }, // 5:「の３個
      { { 0, 1 }, { 1, 1 }, { 2, 1 }, { 2, 0 } }, // 6:「の４個
      { { 0, 0 }, { 1, 0 }, { 2, 0 } }, // 7:３連続
      { { 0, 1 }, { 1, 0 }, { 1, 1 }, { 2, 0 } }, // 8:半分卍(４個)

      { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 }, { 3, 1 } }, // 9: 寝てる
      { { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 2 } }, // 10: 逆T(長い方)
      { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 } }, // 11: L(５個)
      { { 0, 1 }, { 1, 0 }, { 1, 1 }, { 2, 0 }, { 3, 0 } }, // 12: 半分卍(４個)
      { { 0, 1 }, { 0, 2 }, { 1, 1 }, { 2, 0 }, { 2, 1 } }, // 13: Z
      { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 } }, // 14:５連続

      { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 }, { 1, 2 } }, // 15: 下半身太
      { { 1, 0 }, { 2, 0 }, { 0, 1 }, { 1, 1 }, { 0, 2 } }, // 16: Wみたいなの
      { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 0, 2 }, { 1, 2 } }, // 17: コの逆
      { { 1, 0 }, { 2, 0 }, { 0, 1 }, { 1, 1 }, { 1, 2 } }, // 18: 手裏剣風
      { { 1, 0 }, { 2, 1 }, { 0, 1 }, { 1, 1 }, { 1, 2 } }, // 19: 十字架
      { { 1, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 }, { 3, 1 } }, // 20: トンファー

  };

  private static final int[][] NEXT_POSITIONS = {

      { 0, 2 }, // 四角
      { 3, 0 }, // １小竹の
      { 4, 2 }, // 逆T(短かい方)
      { 7, 0 }, // ２連続
      { 9, 3 }, // ４連続
      { 12, 0 }, // 「の３個
      { 15, 2 }, // 「の４個
      { 17, 0 }, // ３連続
      { 20, 2 }, // 半分卍(４個)

      { 0, 6 }, // 寝てるやつ
      { 5, 5 }, // 逆T(長い方)
      { 9, 5 }, // L(５個)
      { 13, 6 }, // 半分卍(４個)
      { 18, 5 }, // Z
      { 22, 5 }, // ５連続

      { 0, 9 }, // 下半身太
      { 3, 9 }, // Wみたいなの
      { 7, 9 }, // コの逆
      { 11, 9 }, // 手裏剣風
      { 15, 9 }, // 十字架
      { 19, 10 }, // トンファー
  };

  // --- Autowired --------------------------------



  @Autowired
  GameRepository gameRepository;

  @Autowired
  PlayerRepository playerRepository;

  @Autowired
  BlockRepository blockRepository;

  @Autowired
  RecordRepository recordRepository;

  @Autowired
  AccountRepository accountRepository;

  @RequestMapping(value = "/getGameInfo", method = RequestMethod.GET)
  @ResponseBody
  public GameData getGameInfo(int id, Principal principal) {
    GameData data = new GameData();

    String loginUserName = principal == null ? "admin" : principal.getName();

    // idでgameテーブルを検索する
    Optional<Game> ret = gameRepository.findById(id);
    if (!ret.isPresent()) {
      System.out.println("ERROR! game id is not found! id=" + id);
      data.setError("id is not found");
      return data;
    }
    int nowPlayer = ret.get().getNowPlayer();
    String nowPlayerColor = Color.getColor(nowPlayer);


    // 表示用のセル配置を作成
    String[][] cells = new String[20][20];
    for (int y = 0; y < cells.length; y++) {
      for (int x = 0; x < cells[y].length; x++) {
        cells[y][x] = Color.DEFAULT;
      }
    }

    // セット済みのブロック情報を取得
    List<Block> settedBlocks = blockRepository.findByGameIdAndStatus(id, Block.STATUS_SETTED);
    for (Block block : settedBlocks) {
      drawBlock(block.getBlockType(), block.getX(), block.getY(), cells, Color.getColor(block.getPlayer()),
          block.getAngle() == null ? 0 : block.getAngle(), block.isFlip());
    }

    // まだ置いていないブロックの配列を作成
    Cell[][] nexts = new Cell[12][23];
    for (int y = 0; y < nexts.length; y++) {
      for (int x = 0; x < nexts[y].length; x++) {
        nexts[y][x] = new Cell(Color.DEFAULT, 0);
      }
    }



    // プレイヤー情報を取得
    // List<Player> players = playerRepository.findByGameIdAndNumber(ret.get().getId(), nowPlayer);
    List<Player> players = playerRepository.findByGameId(ret.get().getId());
    if (players == null || players.size() <= 0) {
      System.out.println("ERROR! player number is not found! id=" + ret.get().getId() );
      data.setError("player number is not found");
      return data;
    }

    boolean isLoginUserPass = false;
    boolean isAllPass = true;
    PlayerInfo minPlayer = null;
    int minZansu = 99;
    Player p = null;
    List<PlayerInfo> playersInfo = new ArrayList<PlayerInfo>();
    for (Player player : players) {

      PlayerInfo playerInfo = new PlayerInfo();

      if (player.getAccountName() != null) {
        playerInfo.setName(player.getAccountName());
      } else {
        playerInfo.setName(player.getCpu());
      }

      boolean isOkeru = false;

      // ログインユーザだったら
      if (player.getAccountName() != null &&  player.getAccountName().equals(loginUserName)) {
        // 未セットのブロックを描画する
        List<Block> notSetBlocks = blockRepository.findByGameIdAndStatusAndPlayer(id, Block.STATUS_NOT_SETTED, player.getNumber());
        for (Block block : notSetBlocks) {
          drawNextBlock(block, nexts, Color.getColor(block.getPlayer()));
        }

      }

      // 現在のプレイヤー
      if (player.getNumber() == nowPlayer) {
        List<Block> notSetBlocks2 = blockRepository.findByGameIdAndStatusAndPlayer(id, Block.STATUS_NOT_SETTED, nowPlayer);

        p = player;
        playerInfo.setBlockZansu(notSetBlocks2.size());
        isOkeru = checkOkeru(notSetBlocks2, cells, nowPlayerColor);
        isLoginUserPass = isOkeru == false;

      // 現在のプレイヤー意外
      } else {

        List<Block> tempBlocks = blockRepository.findByGameIdAndStatusAndPlayer(id, Block.STATUS_NOT_SETTED,
            player.getNumber());
        playerInfo.setBlockZansu(tempBlocks.size());
        isOkeru = checkOkeru(tempBlocks, cells, Color.getColor(player.getNumber()));
      }
      playerInfo.setColor(Color.getColorKanji(player.getNumber()));
      playerInfo.setPass(isOkeru == false);
      playerInfo.setPoint(player.getPoint());
      playersInfo.add(playerInfo);

      // 全員パスかどうかのチェック
      if (playerInfo.isPass() == false) {
        isAllPass = false;
      }

      // 残数の最小値
      if (minZansu > playerInfo.getBlockZansu()) {
        minZansu = playerInfo.getBlockZansu();
        minPlayer = playerInfo;
      }
    }

    // ログインユーザの手番かどうか
    boolean isLoginUserNow = p.getAccountName() != null && p.getAccountName().equals(loginUserName);


    // プレイヤーの名前
    String nowPlayerName = p.getAccountName();
    if (nowPlayerName == null) {
      nowPlayerName = p.getCpu();
    }

    data.setCells(cells);
    data.setNexts(nexts);
    data.setNowPlayer(nowPlayer);
    data.setNowPlayerColor(nowPlayerColor);
    data.setLoginUserNow(isLoginUserNow);
    data.setLoginUserPass(isLoginUserPass);
    data.setNowPlayerName(nowPlayerName);
    data.setPlayersInfo(playersInfo);
    data.setAllPass(isAllPass);
    data.setMinPlayer(minPlayer);

    return data;
  }

  // --- Private Methods --------------------------------

  /***
   * ブロックを90度回転、反転させるメソッド
   *
   * @param oldShape 選択されたブロック
   * @param angle 角度
   * @param flip 反転するかどうか
   * @return 90度回転、もしくは反転したブロックの形
   */
  private int[][] calcBlockShape(int[][] oldShape, int angle, boolean flip) {
    if (angle == 0 && flip == false) {
      return oldShape;
    }
    String[][] cells = new String[5][5];
    String[][] cells2 = new String[5][5];

    // まず、左上を始点として角度なしで描く
    drawBlock(oldShape, 0, 0, cells, "ZZZ");

    for (int a = 0; a < angle; a++) {
      // 90度回転
      for (int x = 0; x < 5; x++) {
        for (int y = 0; y < 5; y++) {
          cells2[y][x] = cells[5 - 1 - x][y];
        }
      }



      // cells2 -> cells
      for (int x = 0; x < 5; x++) {
        cells[x] = cells2[x].clone();
      }

    }

    // 反転flgがあれば、反転させる
    if (flip) {
      for (int x = 0; x < 5; x++) {
        for (int y = 0; y < 5; y++) {
          cells2[y][x] = cells[y][4 - x];
        }
      }
       // cells2 -> cells
       for (int x = 0; x < 5; x++) {
        cells[x] = cells2[x].clone();
      }
    }

    int[][] shape = new int[oldShape.length][2];

    // ZZZ が入っている座標だけを抜き出す
    int i = 0;
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        if (cells2[y][x] != null && cells2[y][x].equals("ZZZ")) {
          shape[i][0] = x;
          shape[i][1] = y;
          i++;
        }
      }
    }

    // x、yの最小値を調べる
    int minX = 99, minY = 99;
    for (i = 0; i < oldShape.length; i++) {
      if (shape[i][0] < minX) {
        minX = shape[i][0];
      }
      if (shape[i][1] < minY) {
        minY = shape[i][1];
      }
    }
    // 最小値が０じゃない場合はずれてるので、最小値が０になるようずらす
    for (i = 0; i < oldShape.length; i++) {
      if (minX > 0) {
        shape[i][0] -= minX;
      }
      if (minY > 0) {
        shape[i][1] -= minY;
      }
    }


    return shape;
  }

  /**
   * ブロックの色表示
   */
  private void drawBlock(int index, int x, int y, String[][] cells, String color, int angle, boolean flip) {
    int[][] block = BLOCK_SHAPE[index];
    block = calcBlockShape(block, angle, flip);

    drawBlock(block, x, y, cells, color);
  }

  private void drawBlock(int[][] block, int x, int y, String[][] cells, String color) {
    for (int[] position : block) {
      cells[y + position[1]][x + position[0]] = color;
    }
  }

  /**
   * ブロック候補の表示
   */
  private void drawNextBlock(Block block, Cell[][] cells, String color) {
    int index = block.getBlockType();
    int x = NEXT_POSITIONS[block.getBlockType()][0];
    int y = NEXT_POSITIONS[block.getBlockType()][1];
    Cell cell = new Cell(color, block.getBlockType());
    for (int[] position : BLOCK_SHAPE[index]) {
      cells[y + position[1]][x + position[0]] = cell;
    }
  }

  /**
   * ブロックを置けるかどうかのチェック
   */
  private boolean checkBlock(int index, int x, int y, String[][] cells, String color, int angle, boolean flip) {
    int[][] block = BLOCK_SHAPE[index];
    block = calcBlockShape(block, angle, flip);
    boolean isCheck = false;

    for (int[] position : block) {
      int newY = y + position[1];
      int newX = x + position[0];

      // はみ出てたらダメ！
      if (cells.length <= newY) {
        return false;
      }
      if (cells[newY].length <= newX) {
        return false;
      }

      // すでにあってもダメ！
      if (cells[newY][newX].equals(Color.DEFAULT) == false) {
        return false;
      }

      // 右隣が同じ色ならダメ
      if (newX < cells[newY].length - 1 && cells[newY][newX + 1].equals(color)) {
        return false;
      }
      // 左隣が同じ色ならダメ
      if (newX > 0 && cells[newY][newX - 1].equals(color)) {
        return false;
      }
      // 下隣が同じ色ならダメ
      if (newY < cells.length - 1 && cells[newY + 1][newX].equals(color)) {
        return false;
      }
      // 上隣が同じ色ならダメ
      if (newY > 0 && cells[newY - 1][newX].equals(color)) {
        return false;
      }

      // 右上が同じ色ならOK
      if (newY > 0 && newX < cells[newY].length - 1 && cells[newY - 1][newX + 1].equals(color)) {
        isCheck = true;
      }

      // 左上が同じ色ならOK
      if (newY > 0 && newX > 0 && cells[newY - 1][newX - 1].equals(color)) {
        isCheck = true;
      }

      // 右下が同じ色ならOK
      if (newY < cells.length - 1 && newX < cells[newY].length - 1 && cells[newY + 1][newX + 1].equals(color)) {
        isCheck = true;
      }

      // 左下が同じ色ならOK
      if (newY < cells.length - 1 && newX > 0 && cells[newY + 1][newX - 1].equals(color)) {
        isCheck = true;
      }

      // 四角を踏んでてらOK
      if ((newX == 0 && newY == 0) || (newX == 0 && newY == cells[newY].length - 1)
          || (newX == cells.length - 1 && newY == 0) || (newX == cells.length - 1 && newY == cells[newY].length - 1)) {
        isCheck = true;
      }
    }
    return isCheck;
  }

  /**
   * CPUの手を作る
   */
  private Hand makeCpuHand(String[][] cells, List<Block> notSetBlocks, String cpuName, String color) {
    Hand hand = new Hand();

    // まずブロックのセル数が多い順に並べる
    Collections.sort(
      notSetBlocks,
      new Comparator<Block>() {
        @Override
        public int compare(Block b1, Block b2) {
          return BLOCK_SHAPE[b2.getBlockType()].length - BLOCK_SHAPE[b1.getBlockType()].length;
        }
      }
    );

    for (Block block : notSetBlocks) {

      // 全部のセルをチェックしていく
      for (int y = 0; y < cells.length; y++) {
        for (int x = 0; x < cells[y].length; x++) {

          // 回転させてチェックする
          for (int angle = 0; angle < 4; angle++) {
            for (int i = 0; i < 2; i++) {
              // チェック
              if (checkBlock(block.getBlockType(), x, y, cells, color, angle, (i == 1))) {
                hand.setX(x);
                hand.setY(y);
                hand.setBlockType(block.getBlockType());
                hand.setFlip(i == 1);
                hand.setAngle((angle));
                return hand;
              }
            }
          }
        }
      }
    }

    // 打つ手なし
    hand.setPass(true);

    return hand;
  }

  /**
   * 置けるブロックがあるかどうか
   */
  private boolean checkOkeru(List<Block> notSetBlocks, String[][] cells, String color) {
    for (int b = 0; b < notSetBlocks.size(); b++) {
      for (int y = 0; y < cells.length; y++) {
        for (int x = 0; x < cells[y].length; x++) {
          for (int angle = 0; angle < 4; angle++) {
            for (int i = 0; i < 2; i++) {
              if (checkBlock(notSetBlocks.get(b).getBlockType(), x, y, cells, color, angle, (i == 1))) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  private void setBlock(int gameId, Player player, int selectBlock, int x, int y, int angle, boolean flip) {
    List<Block> blocks = blockRepository.findByGameIdAndPlayerAndBlockType(gameId, player.getNumber(), selectBlock);
      if (blocks == null || blocks.size() <= 0) {
        System.out.println("ERROR! selectBlock is not found! selectBlock=" + selectBlock);
        // return new ModelAndView("redirect:/");
      }
      Block block = blocks.get(0);
      if (block.getStatus() == Block.STATUS_NOT_SETTED) { // まだセットされていない場合のみ
        block.setStatus(Block.STATUS_SETTED);
        block.setX(x);
        block.setY(y);
        block.setAngle(angle);
        block.setFlip(flip);
        blockRepository.save(block);

        // ポイント(置いたブロックのセル数)を加算する
        int point = BLOCK_SHAPE[selectBlock].length + player.getPoint();

        // 残数を減らす、ポイントを増やす
        player.setZanBlockCount(player.getZanBlockCount()  - 1);
        player.setPoint(point);
        player.setPass(player.getZanBlockCount() == 0);
        playerRepository.save(player);
      }
  }
}
