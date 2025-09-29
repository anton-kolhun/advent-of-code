package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day9 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day9.txt");
        List<Integer> saved = new ArrayList<>();
        Map<Integer, Integer> valueToIndex = new HashMap<>();
        int line = 0;
        for (line = 0; line < 25; line++) {
            int value = Integer.parseInt(lines.get(line));
            saved.add(value);
            valueToIndex.put(value, line);
        }
        int incorrectValue = findIncorrectValue(line, lines, saved, valueToIndex);
        System.out.println("task1: " + incorrectValue);
    }

    private static int findIncorrectValue(int line, List<String> lines, List<Integer> saved, Map<Integer, Integer> valueToIndex) {
        int cursorValue = 0;
        while (line < lines.size()) {
            cursorValue = Integer.parseInt(lines.get(line));
            boolean isCorrect = false;
            for (Integer savedValue : saved) {
                if (valueToIndex.containsKey(cursorValue - savedValue)) {
                    isCorrect = true;
                    break;
                }
            }
            if (!isCorrect) {
                return cursorValue;
            }
            line++;
            int removedValue = saved.removeFirst();
            saved.add(cursorValue);
            valueToIndex.remove(removedValue);
            valueToIndex.put(cursorValue, line);
        }
        return cursorValue;
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day9.txt");
        int sumUpTo = 36845998;
        int startIndex = 0;
        int endIndex = 0;
        int sum = Integer.parseInt(lines.get(startIndex));
        while (sum != sumUpTo) {
            if (sum < sumUpTo) {
                endIndex = endIndex + 1;
                sum = sum + Integer.parseInt(lines.get(endIndex));
            } else {
                sum = sum - Integer.parseInt(lines.get(startIndex));
                startIndex = startIndex + 1;
            }
        }
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = startIndex; i <= endIndex; i++) {
            int value = Integer.parseInt(lines.get(i));
            if (value < min) {
                min = value;
            } else if (value > max) {
                max = value;
            }
        }
        int result = max + min;
        System.out.println("task2: " + result);
    }
}
