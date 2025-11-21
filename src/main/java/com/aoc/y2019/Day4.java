package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.List;

public class Day4 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day4.txt");
        List<Integer> range = ParseUtils.splitByDelimiter(lines.get(0), "-").stream()
                .map(Integer::parseInt)
                .toList();
        List<Integer> matching = new ArrayList<>();
        for (int i = range.get(0); i <= range.get(1); i++) {
            String numbVal = String.valueOf(i);
            char[] charArray = numbVal.toCharArray();
            boolean isDouble = false;
            boolean isIncreasing = true;
            for (int j = 0, charArrayLength = charArray.length - 1; j < charArrayLength; j++) {
                int currentDig = Character.getNumericValue(charArray[j]);
                int nextDig = Character.getNumericValue(charArray[j + 1]);
                if (nextDig < currentDig) {
                    isIncreasing = false;
                    break;
                }
                if (currentDig == nextDig) {
                    isDouble = true;
                }
            }
            if (isDouble && isIncreasing) {
                matching.add(i);
            }
        }
        System.out.println("task1: " + matching.size());
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day4.txt");
        List<Integer> range = ParseUtils.splitByDelimiter(lines.get(0), "-").stream()
                .map(Integer::parseInt)
                .toList();
        List<Integer> matching = new ArrayList<>();
        for (int i = range.get(0); i <= range.get(1); i++) {
            String numbVal = String.valueOf(i);
            char[] charArray = numbVal.toCharArray();
            boolean isDouble = false;
            boolean isIncreasing = true;
            int sameSeqLength = 1;
            for (int j = 0, charArrayLength = charArray.length - 1; j < charArrayLength; j++) {
                int currentDig = Character.getNumericValue(charArray[j]);
                int nextDig = Character.getNumericValue(charArray[j + 1]);
                if (nextDig < currentDig) {
                    isIncreasing = false;
                    break;
                }
                if (currentDig == nextDig) {
                    sameSeqLength++;
                } else {
                    if (sameSeqLength == 2) {
                        isDouble = true;
                    }
                    sameSeqLength = 1;
                }

            }
            if (sameSeqLength == 2) {
                isDouble = true;
            }
            if (isDouble && isIncreasing) {
                matching.add(i);
            }
        }
        System.out.println("task2: " + matching.size());
    }
}
