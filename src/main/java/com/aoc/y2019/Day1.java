package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;

import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day1.txt");
        List<Integer> values = lines.stream().map(Integer::parseInt).toList();
        long sum = 0;
        for (Integer value : values) {
            int res = value / 3 - 2;
            sum += res;
        }
        System.out.println("task1: " + sum);
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day1.txt");
        List<Integer> values = lines.stream().map(Integer::parseInt).toList();
        long sum = 0;
        for (Integer value : values) {
            int res = calculateFuel(value);
            sum += res;
        }
        System.out.println("task2: " + sum);
    }

    private static int calculateFuel(Integer value) {
        int res = value / 3 - 2;
        if (res <= 0) {
            return 0;
        }
        return res + calculateFuel(res);
    }
}
