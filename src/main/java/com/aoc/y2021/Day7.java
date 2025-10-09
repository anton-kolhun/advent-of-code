package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.List;

public class Day7 {

    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day7.txt");
        List<Integer> elements = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(Integer::parseInt).toList();
        int minTask1 = Integer.MAX_VALUE;
        int minTask2 = Integer.MAX_VALUE;
        int minValue = elements.stream().min(Integer::compareTo).get();
        int maxValue = elements.stream().max(Integer::compareTo).get();
        for (int level = minValue; level <= maxValue; level++) {
            int sumTask1 = 0;
            int sumTask2 = 0;
            for (int j = 0; j < elements.size(); j++) {
                sumTask1 += Math.abs(level - elements.get(j));
                sumTask2 += calcDiff(level, elements.get(j));
            }
            if (sumTask1 < minTask1) {
                minTask1 = sumTask1;
            }
            if (sumTask2 < minTask2) {
                minTask2 = sumTask2;
            }
        }
        System.out.println("task1: " + minTask1);
        System.out.println("task2: " + minTask2);
    }

    private static int calcDiff(Integer level, Integer value) {
        int diff = Math.abs(level - value);
        int sum = 0;
        for (int step = 1; step <= diff; step++) {
            sum += step;
        }
        return sum;
    }
}
