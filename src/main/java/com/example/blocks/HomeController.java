package com.example.blocks;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String index(Model model) {
        int [][] cells = new int[20][20];
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                cells[y][x] = 0;
            }
        }

        cells[10][10] = 4;
        cells[11][11] = 3;
        cells[8][8] = 2;
        model.addAttribute("cells", cells);
		return "home";
	}

}