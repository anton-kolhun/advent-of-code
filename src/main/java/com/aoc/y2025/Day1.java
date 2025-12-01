package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;

import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        task();
    }


    public static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day1.txt");
        int position = 50;
        long counterTask2 = 0;
        long counterTask1 = 0;
        for (String line : lines) {
            char direction = line.charAt(0);
            int koef = 1;
            if (direction == 'L') {
                koef = -1;
            }
            int numb = Integer.parseInt(line.substring(1));
            for (int i = 1; i <= Math.abs(numb); i++) {
                position = position + koef;
                if (position == 100) {
                    position = 0;
                }
                if (position == -1) {
                    position = 99;
                }
                if (position == 0) {
                    counterTask2++;
                }
            }
            if (position == 0) {
                counterTask1++;
            }
        }
        System.out.println("task1: " + counterTask1);
        System.out.println("task2: " + counterTask2);
    }
}
