package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.List;

public class Day15 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day15_t.txt");
        System.out.println(lines);
        List<String> items = ParseUtils.splitByDelimiter(lines.get(0), ",");
        long sum = 0;
        for (String line : items) {
            long hash = applyHash(line);
            sum += hash;
        }
        System.out.println(sum);

    }

    private static long applyHash(String str) {
        long current = 0;
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            int val = (int) c;
            current = current + val;
            current = current * 17;
            current = current % 256;
        }
        return current;
    }
}
