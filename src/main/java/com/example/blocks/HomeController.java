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

        cells[10][10] = 4;
        cells[11][11] = 3;
        cells[8][8] = 2;

        int [][] nexts = new int[12][23];
        for (int y = 0; y < nexts.length; y++) {
            for (int x = 0; x < nexts[y].length; x++) {
                nexts[y][x] = 0;
            }
        }


        // next blocks
        drawBlock(0, 0, 2, nexts); // 四角
        drawBlock(1, 3, 0, nexts); // １小竹の
        drawBlock(2, 4, 2, nexts); // 逆T(短かい方)
        drawBlock(3, 7, 0, nexts); // ２連続
        drawBlock(4, 9, 3, nexts); // ４連続
        drawBlock(5, 12, 0, nexts); // 「の３個
        drawBlock(6, 15, 2, nexts); // 「の４個
        drawBlock(7, 17, 0, nexts); // ３連続
        drawBlock(8, 20, 2, nexts); // 半分卍(４個)

        drawBlock(9, 0, 6, nexts);   // 寝てるやつ
        drawBlock(10, 5, 5, nexts);  // 逆T(長い方)
        drawBlock(11, 9, 5, nexts);  // L(５個)
        drawBlock(12, 13, 6, nexts); // 半分卍(４個)
        drawBlock(13, 18, 5, nexts); // Z
        drawBlock(14, 22, 5, nexts); // ５連続


        drawBlock(15, 0, 9, nexts); // 下半身太
        drawBlock(16, 3, 9, nexts); // Wみたいなの
        drawBlock(17, 7, 9, nexts); // コの逆
        drawBlock(18, 11, 9, nexts); // 手裏剣風
        drawBlock(19, 15, 9, nexts); // 十字架
        drawBlock(20, 19, 10, nexts); // トンファー

        int[][] selectCells = new int[5][5];
        if (selectBlock > 0) {
            drawBlock(selectBlock - 1, 0, 0, selectCells);
        }

        int nowPlayer = 1;
        model.addAttribute("cells", cells);
        model.addAttribute("nexts", nexts);
        model.addAttribute("selectCells", selectCells);
        model.addAttribute("nowPlayer", nowPlayer);

		return "home";
    }

    private void drawBlock(int index, int x, int y, int[][] cells) {
        int[][] block = BLOCK_SHAPE[index];
        for (int[] position : block) {
            cells[y + position[1]][x + position[0]] = index + 1;
        }
    }

}