package com.example.blocks;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    // private final static Block[] BLOCK_SHAPE = {
    //     new Block()
    // };

    private final static int[][][] BLOCK_SHAPE = {
        {{0, 0}, {0, 1}, {1, 0}, {1, 1}},   //  0:四角
        {{0, 0}},                           //  1:１小竹の
        {{1, 0}, {0, 1}, {1, 1}, {2, 1}},   //  2:逆T(短かい方)
        {{0, 0}, {1, 0}},                   //  3:２連続
        {{0, 0}, {1, 0}, {2, 0}, {3, 0}},   //  4:４連続
        {{0, 0}, {1, 0}, {1, 1}},           //  5:「の３個
        {{0, 1}, {1, 1}, {2, 1}, {2, 0}},   //  6:「の４個
        {{0, 0}, {1, 0}, {2, 0}},           //  7:３連続
        {{0, 1}, {1, 0}, {1, 1}, {2, 0}},   //  8:半分卍(４個)

        {{0, 0}, {0, 1}, {1, 1}, {2, 1}, {3, 1}},   //  9: 寝てる
        {{0, 2}, {1, 0}, {1, 1}, {1, 2}, {2, 2}},   // 10: 逆T(長い方)
        {{0, 0}, {0, 1}, {0, 2}, {1, 2}, {2, 2}},   // 11: L(５個)
        {{0, 1}, {1, 0}, {1, 1}, {2, 0}, {3, 0}},   // 12: 半分卍(４個)
        {{0, 1}, {0, 2}, {1, 1}, {2, 0}, {2, 1}},   // 13: Z
        {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}},   // 14:５連続

        {{0, 0}, {0, 1}, {0, 2}, {1, 1}, {1, 2}},   // 15: 下半身太
        {{1, 0}, {2, 0}, {0, 1}, {1, 1}, {0, 2}},   // 16: Wみたいなの
        {{0, 0}, {1, 0}, {0, 1}, {0, 2}, {1, 2}},   // 17: コの逆
        {{1, 0}, {2, 0}, {0, 1}, {1, 1}, {1, 2}},   // 18: 手裏剣風
        {{1, 0}, {2, 1}, {0, 1}, {1, 1}, {1, 2}},   // 19: 十字架
        {{1, 0}, {0, 1}, {1, 1}, {2, 1}, {3, 1}},   // 20: トンファー


    };

	@RequestMapping("/")
	public String index(Model model, @RequestParam(name="block", defaultValue="0")int selectBlock) {
        int [][] cells = new int[20][20];
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                cells[y][x] = 0;
            }
        }

        /*
        cells[10][10] = 4;
        cells[11][11] = 3;
        cells[8][8] = 2;
        */

        drawBlock(10, 10, 10, cells, 1);

        int [][] nexts = new int[12][23];
        for (int y = 0; y < nexts.length; y++) {
            for (int x = 0; x < nexts[y].length; x++) {
                nexts[y][x] = 0;
            }
        }


        // next blocks
        drawBlock(0, 0, 2, nexts, 1); // 四角
        drawBlock(1, 3, 0, nexts, 2); // １小竹の
        drawBlock(2, 4, 2, nexts, 3); // 逆T(短かい方)
        drawBlock(3, 7, 0, nexts, 4); // ２連続
        drawBlock(4, 9, 3, nexts, 5); // ４連続
        drawBlock(5, 12, 0, nexts, 6); // 「の３個
        drawBlock(6, 15, 2, nexts, 7); // 「の４個
        drawBlock(7, 17, 0, nexts, 8); // ３連続
        drawBlock(8, 20, 2, nexts, 9); // 半分卍(４個)

        drawBlock(9, 0, 6, nexts, 10);   // 寝てるやつ
        drawBlock(10, 5, 5, nexts, 11);  // 逆T(長い方)
        drawBlock(11, 9, 5, nexts, 12);  // L(５個)
        drawBlock(12, 13, 6, nexts, 13); // 半分卍(４個)
        drawBlock(13, 18, 5, nexts, 14); // Z
        drawBlock(14, 22, 5, nexts, 15); // ５連続


        drawBlock(15, 0, 9, nexts, 16); // 下半身太
        drawBlock(16, 3, 9, nexts, 17); // Wみたいなの
        drawBlock(17, 7, 9, nexts, 18); // コの逆
        drawBlock(18, 11, 9, nexts, 19); // 手裏剣風
        drawBlock(19, 15, 9, nexts, 20); // 十字架
        drawBlock(20, 19, 10, nexts, 21); // トンファー

        int nowPlayer = 1;

        int[][] selectCells = new int[5][5];
        if (selectBlock > 0) {
            drawBlock(selectBlock - 1, 0, 0, selectCells, selectBlock);

            for (int y = 0; y < cells.length; y++) {
                for (int x = 0; x < cells[y].length; x++) {
                    checkBlock(selectBlock - 1, y, x, cells, nowPlayer);
                }
            }

        }

        model.addAttribute("cells", cells);
        model.addAttribute("nexts", nexts);
        model.addAttribute("selectCells", selectCells);
        model.addAttribute("nowPlayer", nowPlayer);

		return "home";
    }

    private void drawBlock(int index, int x, int y, int[][] cells, int value) {
        int[][] block = BLOCK_SHAPE[index];
        for (int[] position : block) {
            cells[y + position[1]][x + position[0]] = value;
        }
    }

    private void checkBlock(int index, int x, int y, int[][] cells, int nowPlayer) {
        int[][] block = BLOCK_SHAPE[index];
        boolean isCheck = false;
        for (int[] position : block) {
            int newY = y + position[1];
            int newX = x + position[0];

            // はみ出てたらダメ！
            if (cells.length <= newY) {
                return;
            }
            if (cells[newY].length <= newX) {
                return;
            }

            // すでにあってもダメ！
            if (cells[newY][newX] > 0) {
                return;
            }

            // 右隣が同じ色ならダメ
            if (newX < cells[newY].length - 1 && cells[newY][newX + 1] == nowPlayer) {
                return;
            }
            // 左隣が同じ色ならダメ
            if (newX > 0 && cells[newY][newX - 1] == nowPlayer) {
                return;
            }
            // 下隣が同じ色ならダメ
            if (newY < cells.length - 1 && cells[newY + 1][newX] == nowPlayer) {
                return;
            }
            // 上隣が同じ色ならダメ
            if (newY > 0 && cells[newY - 1][newX] == nowPlayer) {
                return;
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
            if (newY < cells.length -1 && newX < cells[newY].length - 1 && cells[newY + 1][newX + 1] == nowPlayer) {
                isCheck = true;
            }

            // 左下が同じ色ならOK
            if (newY < cells.length -1 && newX > 0 && cells[newY + 1][newX - 1] == nowPlayer) {
                isCheck = true;
            }

            // 四角を踏んでてらOK
            if ((newX == 0 && newY == 0) ||
                (newX == 0 && newY == cells[newY].length - 1) ||
                (newX == cells.length -1 && newY == 0) ||
                (newX == cells.length -1 && newY == cells[newY].length - 1) ) {
                isCheck = true;
            }
        }
        if (isCheck) {
            for (int[] position : block) {
                int newY = y + position[1];
                int newX = x + position[0];
                cells[newY][newX] = nowPlayer + 10;
            }
        }
    }

}