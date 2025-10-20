package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.List;

public class Day2 {
    public static void main(String[] args) {
        task2();
    }

    private static void task1() {
        int x = 0;
        int y = 0;
        List<String> lines = FilesUtils.readFile("aoc_2021/day2.txt");
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " ");
            if (parts.get(0).contains("forward")) {
                x = x + Integer.parseInt(parts.get(1));
            } else if (parts.get(0).contains("down")) {
                y = y + Integer.parseInt(parts.get(1));
            } else {
                y = y - Integer.parseInt(parts.get(1));
            }
        }
        System.out.println(x * y);
    }

    private static void task2() {
        int x = 0;
        int y = 0;
        int aim = 0;
        List<String> lines = FilesUtils.readFile("aoc_2021/day2.txt");
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " ");
            if (parts.get(0).contains("forward")) {
                x = x + Integer.parseInt(parts.get(1));
                y = y + aim * Integer.parseInt(parts.get(1));
            } else if (parts.get(0).contains("down")) {
                aim = aim + Integer.parseInt(parts.get(1));
            } else {
                aim = aim - Integer.parseInt(parts.get(1));
            }
        }
        System.out.println(x * y);
    }
}
