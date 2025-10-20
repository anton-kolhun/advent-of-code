package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;

import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day1.txt");
        int counter = 0;
        for (int i = 1; i < lines.size(); i++) {
            int current = Integer.parseInt(lines.get(i));
            int previous = Integer.parseInt(lines.get(i - 1));
            if (current > previous) {
                counter++;
            }
        }
        System.out.println("task1: " + counter);
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day1.txt");
        int counter = 0;
        int currentSUm = Integer.MAX_VALUE;
        for (int i = 2; i < lines.size(); i++) {
            int current = Integer.parseInt(lines.get(i));
            int previous = Integer.parseInt(lines.get(i - 1));
            int beforePrevious = Integer.parseInt(lines.get(i - 2));
            int sum = current + previous + beforePrevious;
            if (sum > currentSUm) {
                counter++;
            }
            currentSUm = sum;
        }
        System.out.println("task2: " + counter);
    }
}
