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
        block.setColor(p + 1);                    // ブロックの色は、プレイヤーのindex+1
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

  @RequestMapping("/play")
  public String play(Model model, @RequestParam(name = "block", defaultValue = "0") int selectBlock, @RequestParam(name = "id",required = true, defaultValue = "0") int id, @RequestParam(name = "x", defaultValue = "-1") int kouhoX, @RequestParam(name = "y", defaultValue = "-1") int kouhoY) {

    // idでgameテーブルを検索する
    Optional<Game> ret = gameRepository.findById(id);
    if (!ret.isPresent()) {
      System.out.println("ERROR! game id is not found! id=" + id);
      return "redirect:/";
    }

    // 表示用のセル配置を作成
    int[][] cells = new int[20][20];
    for (int y = 0; y < cells.length; y++) {
      for (int x = 0; x < cells[y].length; x++) {
        cells[y][x] = 0;
      }
    }

    // セット済みのブロック情報を取得
    List<Block> settedBlocks = blockRepository.findByGameIdAndStatus(id, Block.STATUS_SETTED);
    for (Block block : settedBlocks) {
      drawBlock(block.getBlockType(), block.getX(), block.getY(), cells, block.getColor());
    }


    // テスト用コード
    drawBlock(10, 10, 10, cells, 1);

    // 候補表示用の配列を作成
    int[][] nexts = new int[12][23];
    for (int y = 0; y < nexts.length; y++) {
      for (int x = 0; x < nexts[y].length; x++) {
        nexts[y][x] = 0;
      }
    }
    List<Block> notSetBlocks = blockRepository.findByGameIdAndStatusAndColor(id, Block.STATUS_NOT_SETTED, ret.get().getNowPlayer());
    for (Block block : notSetBlocks) {
      drawBlock(block.getBlockType(), NEXT_POSITIONS[block.getBlockType()][0], NEXT_POSITIONS[block.getBlockType()][1], nexts, block.getBlockType() + 1);
    }


    List<List<Integer>> kouhoList = new ArrayList<List<Integer>>();
    int[][] selectCells = new int[5][5];
    if (selectBlock > 0) {
      drawBlock(selectBlock - 1, 0, 0, selectCells, selectBlock);

      for (int y = 0; y < cells.length; y++) {
        for (int x = 0; x < cells[y].length; x++) {
          if (checkBlock(selectBlock - 1, x, y, cells, ret.get().getNowPlayer())) {
            List<Integer> xy = new ArrayList<Integer>();
            xy.add(x);
            xy.add(y);
            kouhoList.add(xy);
          }
        }
      }

      // 選択している候補の色を変更する
      if (kouhoX >= 0 && kouhoY >= 0 && checkBlock(selectBlock - 1, kouhoX, kouhoY, cells, ret.get().getNowPlayer())) {
        for (int[] position : BLOCK_SHAPE[selectBlock - 1]) {
          int newY = kouhoY + position[1];
          int newX = kouhoX + position[0];
          cells[newY][newX] = ret.get().getNowPlayer() + 10;  // 候補なので+10して区別をつける
        }
      }




    }

    model.addAttribute("cells", cells);
    model.addAttribute("nexts", nexts);
    model.addAttribute("selectCells", selectCells);
    model.addAttribute("selectBlock", selectBlock);
    model.addAttribute("kouhoX", kouhoX);
    model.addAttribute("kouhoY", kouhoY);
    model.addAttribute("kouhoList", kouhoList);
    model.addAttribute("nowPlayer", ret.get().getNowPlayer());
    model.addAttribute("id", ret.get().getId());

    return "play";
  }

  private void drawBlock(int index, int x, int y, int[][] cells, int value) {
    int[][] block = BLOCK_SHAPE[index];
    for (int[] position : block) {
      cells[y + position[1]][x + position[0]] = value;
    }
  }

  private boolean checkBlock(int index, int x, int y, int[][] cells, int nowPlayer) {
    int[][] block = BLOCK_SHAPE[index];
    boolean isCheck = false;

    if (x == 17 && y == 18) {
      System.out.println("test");
    }

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
      if (cells[newY][newX] > 0) {
        return false;
      }

      // 右隣が同じ色ならダメ
      if (newX < cells[newY].length - 1 && cells[newY][newX + 1] == nowPlayer) {
        return false;
      }
      // 左隣が同じ色ならダメ
      if (newX > 0 && cells[newY][newX - 1] == nowPlayer) {
        return false;
      }
      // 下隣が同じ色ならダメ
      if (newY < cells.length - 1 && cells[newY + 1][newX] == nowPlayer) {
        return false;
      }
      // 上隣が同じ色ならダメ
      if (newY > 0 && cells[newY - 1][newX] == nowPlayer) {
        return false;
      }

      // 右上が同じ色ならOK
      if (newY > 0 && newX < cells[newY].length - 1 && cells[newY - 1][newX + 1] == nowPlayer) {
        isCheck = true;
      }

      // 左上が同じ色ならOK
      if (newY > 0 && newX > 0 && cells[newY - 1][newX - 1] == nowPlayer) {
        isCheck = true;
      }

      // 右下が同じ色ならOK
      if (newY < cells.length - 1 && newX < cells[newY].length - 1 && cells[newY + 1][newX + 1] == nowPlayer) {
        isCheck = true;
      }

      // 左下が同じ色ならOK
      if (newY < cells.length - 1 && newX > 0 && cells[newY + 1][newX - 1] == nowPlayer) {
        isCheck = true;
      }

      // 四角を踏んでてらOK
      if ((newX == 0 && newY == 0) || (newX == 0 && newY == cells[newY].length - 1)
          || (newX == cells.length - 1 && newY == 0) || (newX == cells.length - 1 && newY == cells[newY].length - 1)) {
        isCheck = true;
      }
    }
    /*
    if (isCheck) {
      for (int[] position : block) {
        int newY = y + position[1];
        int newX = x + position[0];
        cells[newY][newX] = nowPlayer + 10;
      }
    }
    */
    return isCheck;
  }

}
