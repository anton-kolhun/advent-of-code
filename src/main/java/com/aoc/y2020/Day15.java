package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day15 {

    public static void main(String[] args) {
        task1();

    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day15.txt");
        List<Integer> values = ParseUtils.splitByDelimiter(lines.get(0), " ").stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        System.out.println(values);
        Map<Integer, List<Integer>> valueToIndex = new HashMap<>();
        int cursor;
        List<Integer> seq = new ArrayList<>();
        Integer lastValue = -1;
        for (cursor = 0; cursor < values.size(); cursor++) {
            Integer value = values.get(cursor);
            List<Integer> vals = new ArrayList<>();
            vals.add(cursor);
            valueToIndex.put(value, vals);
            seq.add(value);
            lastValue = value;
        }
        while (cursor < 30000000) {
            List<Integer> data = valueToIndex.get(lastValue);
            if (data.size() < 2) {
                lastValue = 0;
                seq.add(lastValue);
            } else {
                lastValue = Math.abs(data.get(1) - data.get(0));
                seq.add(lastValue);
            }
            updateMap(valueToIndex, lastValue, cursor);
            cursor++;
        }
        System.out.println("task1 result = " + seq.get(2019));
        System.out.println("task2 result = " + seq.get(30000000 - 1));
    }

    private static void updateMap(Map<Integer, List<Integer>> valueToIndex, Integer lastValue, int cursor) {
        List<Integer> newVals = new ArrayList<>();
        newVals.add(cursor);
        valueToIndex.merge(lastValue, newVals, (old, newValue) -> {
            if (old.size() < 2) {
                old.add(cursor);
                return old;
            }
            int maxIndex = Math.max(old.get(0), old.get(1));
            List<Integer> updated = new ArrayList<>();
            updated.add(maxIndex);
            updated.add(cursor);
            return updated;
        });
    }
}
