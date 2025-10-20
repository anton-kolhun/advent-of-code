package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day3 {


    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day3.txt");
        List<String> rows = new ArrayList<>();
        StringBuffer res1 = new StringBuffer();
        StringBuffer res2 = new StringBuffer();
        for (String line : lines) {
            rows.add(line);
        }
        int width = rows.getFirst().length();
        int height = rows.size();
        for (int index = 0; index < width; index++) {
            int zeroCounter = 0;
            for (String row : rows) {
                if (row.charAt(index) == '0') {
                    zeroCounter++;
                }
            }
            if (zeroCounter > height / 2) {
                res1.append('0');
                res2.append('1');
            } else {
                res1.append('1');
                res2.append('0');
            }
        }
        int value1 = Integer.parseInt(res1.toString(), 2);
        int value2 = Integer.parseInt(res2.toString(), 2);
        System.out.println(value1 * value2);
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day3.txt");
        List<String> rows = new ArrayList<>();
        for (String line : lines) {
            rows.add(line);
        }
        List<String> res1 = new ArrayList<>(rows);
        List<String> res2 = new ArrayList<>(rows);
        for (int index = 0; index < rows.getFirst().length(); index++) {
            updateRows(res1, index, '0', '1');
            updateRows(res2, index, '1', '0');
        }
        long value1 = Integer.parseInt(res1.getFirst(), 2);
        long value2 = Integer.parseInt(res2.getFirst(), 2);
        System.out.println(value1 * value2);
    }

    private static void updateRows(List<String> res2, int index, char c1, char c2) {
        int zeroCounter = 0;
        for (String row : res2) {
            if (row.charAt(index) == '0') {
                zeroCounter++;
            }
        }
        if (res2.size() > 1) {
            if (zeroCounter > res2.size() / 2) {
                keepChars(res2, index, c1);
            } else {
                keepChars(res2, index, c2);
            }
        }
    }

    private static void keepChars(List<String> current, int index, char c) {
        for (Iterator<String> iterator = current.iterator(); iterator.hasNext(); ) {
            String s = iterator.next();
            if (s.charAt(index) != c) {
                iterator.remove();
            }
        }
    }
}
