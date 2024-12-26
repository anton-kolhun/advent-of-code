package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;

import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day1.txt");
        for (int i = 0; i < lines.size() - 1; i++) {
            String line = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                String line2 = lines.get(j);
                int sum = Integer.parseInt(line) + Integer.parseInt(line2);
                if (sum == 2020) {
                    int res = Integer.parseInt(line) * Integer.parseInt(line2);
                    System.out.println(res);
                    return;
                }
            }
        }

    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day1.txt");
        for (int i = 0; i < lines.size() - 2; i++) {
            String line1 = lines.get(i);
            for (int j = i + 1; j < lines.size() - 1; j++) {
                String line2 = lines.get(j);
                for (int z = j + 1; z < lines.size(); z++) {
                    String line3 = lines.get(z);
                    int sum = Integer.parseInt(line1) + Integer.parseInt(line2) + Integer.parseInt(line3);
                    if (sum == 2020) {
                        int res = Integer.parseInt(line1) * Integer.parseInt(line2) * Integer.parseInt(line3);
                        System.out.println(res);
                        return;
                    }
                }

            }
        }
    }
}
