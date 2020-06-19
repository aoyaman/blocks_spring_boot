package com.example.blocks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @Autowired
  GameRepository gameRepository;

  @Autowired
  BlockRepository blockRepository;

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

    { 0, 2}, // 四角
    { 3, 0}, // １小竹の
    { 4, 2}, // 逆T(短かい方)
    { 7, 0}, // ２連続
    { 9, 3}, // ４連続
    { 12, 0}, // 「の３個
    { 15, 2}, // 「の４個
    { 17, 0}, // ３連続
    { 20, 2}, // 半分卍(４個)

    { 0, 6}, // 寝てるやつ
    { 5, 5}, // 逆T(長い方)
    { 9, 5}, // L(５個)
    { 13, 6}, // 半分卍(４個)
    { 18, 5}, // Z
    { 22, 5}, // ５連続

    { 0, 9}, // 下半身太
    { 3, 9}, // Wみたいなの
    { 7, 9}, // コの逆
    { 11, 9}, // 手裏剣風
    { 15, 9}, // 十字架
    { 19, 10}, // トンファー
  };

  @RequestMapping("/")
  public String home() {
    return "home";
  }

  @PostMapping(path = "/start")
  public ModelAndView start() {

    // 現在の日時を取得
    LocalDateTime date1 = LocalDateTime.now();
    DateTimeFormatter dtformat1 =
				DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS E");
    String fdate1 = dtformat1.format(date1);

    // gameテーブルにデータを新規作成
    Game g = new Game();
    g.setNowPlayer(1);    // 最初はPlayer0
    g.setDate(fdate1);    // 今の日時
    g = gameRepository.save(g);

    // 人数分ループ
    for (int p = 0; p < 4; p++) {
      // ブロックの数の分だけループ
      for (int i = 0; i < BLOCK_SHAPE.length; i++) {
        // ブロックのデータをDBに保存
        Block block = new Block();
        block.setBlockType(i);                    // ブロックの種類
        block.setPlayer(p + 1);                   // プレイヤー番号(１始まり)
        block.setGameId(g.getId());               // gameテーブルのid
        block.setStatus(Block.STATUS_NOT_SETTED); // 状態=未セット
        blockRepository.save(block);
      }
    }

    // play画面へリダイレクト
    ModelAndView modelAndView = new ModelAndView("redirect:/play");
    modelAndView.addObject("id", g.getId());
    return modelAndView;
  }

  /**
   * ゲーム盤を表示する
   */
  @RequestMapping("/play")
  public String play(Model model, @RequestParam(name = "block", defaultValue = "0") int selectBlock, @RequestParam(name = "id",required = true, defaultValue = "0") int id, @RequestParam(name = "x", defaultValue = "-1") int kouhoX, @RequestParam(name = "y", defaultValue = "-1") int kouhoY) {

    // idでgameテーブルを検索する
    Optional<Game> ret = gameRepository.findById(id);
    if (!ret.isPresent()) {
      System.out.println("ERROR! game id is not found! id=" + id);
      return "redirect:/";
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
      drawBlock(block.getBlockType(), block.getX(), block.getY(), cells, Color.getColor(block.getPlayer()), block.getAngle() == null ? 0 : block.getAngle());
    }

    // まだ置いていないブロックの配列を作成
    Cell[][] nexts = new Cell[12][23];
    for (int y = 0; y < nexts.length; y++) {
      for (int x = 0; x < nexts[y].length; x++) {
        nexts[y][x] = new Cell(Color.DEFAULT, 0);
      }
    }
    List<Block> notSetBlocks = blockRepository.findByGameIdAndStatusAndPlayer(id, Block.STATUS_NOT_SETTED, nowPlayer);
    for (Block block : notSetBlocks) {
      drawNextBlock(block, nexts, Color.getColor(block.getPlayer()));
    }


    model.addAttribute("cells", cells);
    model.addAttribute("nexts", nexts);
    model.addAttribute("selectBlock", selectBlock);
    model.addAttribute("nowPlayer", nowPlayer);
    model.addAttribute("nowPlayerColor", nowPlayerColor);
    model.addAttribute("id", ret.get().getId());

    return "play";
  }


  /**
   * 配置の候補を表示する
   */
  @RequestMapping("/kouho")
  public String kouho(Model model, @RequestParam(name = "id",required = true, defaultValue = "0") int id, @RequestParam(name = "block", required = true, defaultValue = "0") int selectBlock, @RequestParam(name = "angle",required = false, defaultValue = "0") int angle) {

    // idでgameテーブルを検索する
    Optional<Game> ret = gameRepository.findById(id);
    if (!ret.isPresent()) {
      System.out.println("ERROR! game id is not found! id=" + id);
      return "redirect:/";
    }
    int nowPlayer = ret.get().getNowPlayer();
    String nowPlayerColor = Color.getColor(nowPlayer);


    // まだ置いていないブロックの配列を作成
    Cell[][] nexts = new Cell[12][23];
    for (int y = 0; y < nexts.length; y++) {
      for (int x = 0; x < nexts[y].length; x++) {
        nexts[y][x] = new Cell(Color.DEFAULT, 0);
      }
    }
    List<Block> notSetBlocks = blockRepository.findByGameIdAndStatusAndPlayer(id, Block.STATUS_NOT_SETTED, nowPlayer);
    for (Block block : notSetBlocks) {
      String color = Color.getColor(block.getPlayer());
      if (block.getBlockType() == selectBlock) {
        color = Color.getKouhoColor(block.getPlayer());
      }
      drawNextBlock(block, nexts, color);
    }

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
      drawBlock(block.getBlockType(), block.getX(), block.getY(), cells, Color.getColor(block.getPlayer()), block.getAngle() == null ? 0 : block.getAngle());
    }

    // 置ける場所の候補リスト
    List<Kouho> kouhoList = new ArrayList<Kouho>(); // 置ける場所の候補リスト
    for (int y = 0; y < cells.length; y++) {
      for (int x = 0; x < cells[y].length; x++) {
        if (checkBlock(selectBlock, x, y, cells, nowPlayerColor, angle)) {

          // 候補用の色を取得
          String color = Color.getKouhoColor(nowPlayer);

          // ここに置いた場合の絵を書く
          String[][] cells2 = new String[20][20];
          for (int i = 0; i < cells.length; i++) {
            cells2[i] = cells[i].clone();
          }
          drawBlock(selectBlock, x, y, cells2, color, angle);

          // 候補をリストに追加
          Kouho kouho = new Kouho(x, y, cells2);
          kouhoList.add(kouho);
        }
      }
    }

    model.addAttribute("nexts", nexts);
    model.addAttribute("selectBlock", selectBlock);
    model.addAttribute("kouhoList", kouhoList);
    model.addAttribute("angle", angle);
    model.addAttribute("nowPlayer", nowPlayer);
    model.addAttribute("nowPlayerColor", nowPlayerColor);
    model.addAttribute("id", ret.get().getId());

    return "kouho";
  }

  /**
   * ブロックを置いたアクション
   */
  @RequestMapping("/oku")
  public ModelAndView oku(Model model, @RequestParam(name = "block", required = true, defaultValue = "0") int selectBlock, @RequestParam(name = "id",required = true, defaultValue = "0") int id, @RequestParam(name = "x", required = true, defaultValue = "-1") int x, @RequestParam(name = "y", required = true, defaultValue = "-1") int y, @RequestParam(name = "angle", required = true, defaultValue = "angle") int angle) {

    // idでgameテーブルを検索する
    Optional<Game> ret = gameRepository.findById(id);
    if (!ret.isPresent()) {
      System.out.println("ERROR! game id is not found! id=" + id);
      return new ModelAndView("redirect:/");
    }
    Game game = ret.get();

    // 現在操作中のプレイヤーの選択ブロックを取得
    List<Block> blocks = blockRepository.findByGameIdAndPlayerAndBlockType(id, game.getNowPlayer(), selectBlock);
    if (blocks == null || blocks.size() <= 0) {
      System.out.println("ERROR! selectBlock is not found! selectBlock=" + selectBlock);
      return new ModelAndView("redirect:/");
    }
    Block block = blocks.get(0);
    if (block.getStatus() == Block.STATUS_NOT_SETTED) { // まだセットされていない場合のみ
      block.setStatus(Block.STATUS_SETTED);
      block.setX(x);
      block.setY(y);
      block.setAngle(angle);
      blockRepository.save(block);
    }

    // 次のプレイヤーに移動
    game.goNextPlayer();
    gameRepository.save(game);

    // play画面へリダイレクト
    ModelAndView modelAndView = new ModelAndView("redirect:/play");
    modelAndView.addObject("id", id);
    return modelAndView;
  }

  /**
   * ブロックの形を計算する
   */
  private int[][] calcBlockShape(int[][] oldShape, int angle) {
    if (angle == 0) {
      return oldShape;
    }
    String [][] cells = new String[5][5];
    String [][] cells2 = new String[5][5];

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

    return shape;
  }

  /**
   * ブロックの色表示
   */
  private void drawBlock(int index, int x, int y, String[][] cells, String color, int angle) {
    int[][] block = BLOCK_SHAPE[index];
    block = calcBlockShape(block, angle);
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
    int x =  NEXT_POSITIONS[block.getBlockType()][0];
    int y =  NEXT_POSITIONS[block.getBlockType()][1];
    Cell cell = new Cell(color, block.getBlockType());
    for (int[] position : BLOCK_SHAPE[index]) {
      cells[y + position[1]][x + position[0]] = cell;
    }
  }

  /**
   * ブロックを置けるかどうかのチェック
   */
  private boolean checkBlock(int index, int x, int y, String[][] cells, String color, int angle) {
    int[][] block = BLOCK_SHAPE[index];
    block = calcBlockShape(block, angle);
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

}
