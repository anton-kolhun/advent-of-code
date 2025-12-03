package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Day3 {

    public static void main(String[] args) {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day3.txt");
        long total = 0;
        for (String line : lines) {
            int maxRow = Integer.MIN_VALUE;
            for (int i = 0; i < line.length() - 1; i++) {
                for (int j = i + 1; j < line.length(); j++) {
                    String voltageStr = String.valueOf(line.charAt(i)) + line.charAt(j);
                    Integer voltage = Integer.parseInt(voltageStr);
                    maxRow = Math.max(maxRow, voltage);
                }
            }
            total += maxRow;
        }
        System.out.println("task1: " + total);
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day3.txt");
        BigInteger total = BigInteger.ZERO;
        for (String line : lines) {
            List<Integer> voltage = new ArrayList<>();
            int lineCursor = 0;
            for (int remainingDigits = 12; remainingDigits > 0; remainingDigits--) {
                lineCursor = findBestDigit(remainingDigits, lineCursor, line, voltage);
            }
            StringBuilder voltageConcat = new StringBuilder();
            for (Integer i : voltage) {
                voltageConcat.append(i);
            }
            total = total.add(new BigInteger(voltageConcat.toString()));
        }
        System.out.println("task2: " + total);
    }

    private static int findBestDigit(int remainingDigits, int lineCursor, String line, List<Integer> voltage) {
        int optionLength = line.length() - lineCursor - remainingDigits;
        int max = Character.getNumericValue(line.charAt(lineCursor));
        int lineCursorMax = lineCursor;
        for (int i = 0; i < optionLength; i++) {
            lineCursor++;
            int value = Character.getNumericValue(line.charAt(lineCursor));
            if (value > max) {
                max = value;
                lineCursorMax = lineCursor;
            }
        }
        voltage.add(max);
        return lineCursorMax + 1;
    }

}
