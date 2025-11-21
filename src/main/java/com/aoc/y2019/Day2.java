package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.List;

public class Day2 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day2.txt");
        List<Integer> items = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(Integer::parseInt)
                .toList();
        items = new ArrayList<>(items);
        int cursor = 0;
        items.set(1, 12);
        items.set(2, 2);
        while (cursor < items.size()) {
            int code = items.get(cursor);
            int addrFrom1 = items.get(cursor + 1);
            int addrFrom2 = items.get(cursor + 2);
            int addrTo = items.get(cursor + 3);
            if (code == 1) {
                Integer result = items.get(addrFrom1) + items.get(addrFrom2);
                items.set(addrTo, result);
            } else if (code == 2) {
                Integer result = items.get(addrFrom1) * items.get(addrFrom2);
                items.set(addrTo, result);
            } else if (code == 99) {
                break;
            }
            cursor = cursor + 4;
        }
        System.out.println( "task1: " + items.get(0));
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day2.txt");
        List<Integer> initialItems = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(Integer::parseInt)
                .toList();
        for (int noun = 0; noun <= 99; noun++){
            for (int verb = 0; verb <= 99; verb++){
                int cursor = 0;
                List<Integer> items = new ArrayList<>(initialItems);
                items.set(1, noun);
                items.set(2, verb);
                while (cursor < items.size()) {
                    int code = items.get(cursor);
                    int addrFrom1 = items.get(cursor + 1);
                    int addrFrom2 = items.get(cursor + 2);
                    int addrTo = items.get(cursor + 3);
                    if (code == 1) {
                        Integer result = items.get(addrFrom1) + items.get(addrFrom2);
                        items.set(addrTo, result);
                    } else if (code == 2) {
                        Integer result = items.get(addrFrom1) * items.get(addrFrom2);
                        items.set(addrTo, result);
                    } else if (code == 99) {
                        break;
                    }
                    cursor = cursor + 4;
                }
                if (items.get(0).equals(19690720)) {
                    int res = 100 * noun + verb;
                    System.out.println("task2: " + res);
                    break;
                }
            }
        }
    }

}
