package com.example.aoc_2020;

import com.example.aoc_2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.List;

public class Day25 {

    public static void main(String[] args) throws Exception {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day25.txt");

        int cursor = 0;
        List<List<Integer>> locks = new ArrayList<>();
        List<List<Integer>> keys = new ArrayList<>();
        while (cursor < lines.size()) {
            var line = lines.get(cursor);
            cursor++;
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("#")) {
                List<Integer> initialLock = new ArrayList<>(List.of(0, 0, 0, 0, 0));
                cursor = fillArray(lines, cursor, locks, initialLock);
            }
            if (line.startsWith(".")) {
                List<Integer> initialKey = new ArrayList<>(List.of(-1, -1, -1, -1, -1));
                cursor = fillArray(lines, cursor, keys, initialKey);
            }
        }

        int match = 0;
        for (List<Integer> lock : locks) {
            for (List<Integer> key : keys) {
                boolean goodFit = true;
                for (int col = 0; col < key.size(); col++) {
                    Integer keyValue = key.get(col);
                    Integer lockValue = lock.get(col);
                    if (keyValue >= (6 - lockValue)) {
                        goodFit = false;
                        break;
                    }
                }
                if (goodFit) {
                    match++;
                }
            }
        }
        System.out.println(match);
    }

    private static int fillArray(List<String> lines, int cursor, List<List<Integer>> items, List<Integer> item) {
        var line = lines.get(cursor);
        while (!line.isEmpty()) {
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == '#') {
                    item.set(col, item.get(col) + 1);
                }
            }
            cursor++;
            if (cursor == lines.size()) {
                break;
            }
            line = lines.get(cursor);
        }
        items.add(item);
        return cursor;
    }
}
