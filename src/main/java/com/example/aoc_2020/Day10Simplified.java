package com.example.aoc_2020;

import com.example.aoc_2023.helper.FilesUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day10Simplified {


    public static void main(String[] args) {
//        task1();
        task2();
    }

    public static void task1() {
        List<Integer> jolts = new ArrayList<>();
        List<String> lines = FilesUtils.readFile("aoc_2020/day10.txt");
        for (String line : lines) {
            jolts.add(Integer.parseInt(line));
        }
        jolts.add(0);
        jolts.sort(Comparator.comparingInt(o -> o));
        int val1 = calculateDiff(jolts, 1, 0);
        int val2 = calculateDiff(jolts, 3, 1);
        System.out.println(val1 * val2);
    }

    private static int calculateDiff(List<Integer> jolts, int diff, int extra) {
        Integer jolt = jolts.get(0);
        int diffNumber = 0;
        for (int i = 1; i < jolts.size(); i++) {
            Integer joltNext = jolts.get(i);
            if (joltNext - jolt == diff) {
                diffNumber++;
            }
            jolt = joltNext;
        }
        return diffNumber + extra;
    }

    public static void task2() {
        List<Integer> jolts = new ArrayList<>();
        List<String> lines = FilesUtils.readFile("aoc_2020/day10.txt");
        for (String line : lines) {
            jolts.add(Integer.parseInt(line));
        }
        jolts.add(0);
        jolts.sort(Comparator.comparingInt(o -> o));
        jolts.add(jolts.getLast() + 3);
        List<Integer> initial = new ArrayList<>();
        initial.add(0);
        Long arrangements = findAllWays(initial, 1, jolts, new HashMap<>());
        System.out.println(arrangements);
    }

    private static long findAllWays(List<Integer> current, int cursor, List<Integer> jolts,
                                    Map<CacheKey, Long> cache) {
        if (Objects.equals(current.getLast(), jolts.getLast())) {
            return 1;
        }
        if (cursor >= jolts.size()) {
            return 0;
        }
        var nextJolt = jolts.get(cursor);
        if (nextJolt - current.getLast() > 3) {
            return 0;
        }
        List<Integer> nextSet = new ArrayList<>(current);
        nextSet.add(nextJolt);
        long total = 0;
        var res1 = calculateOptions(cursor, jolts, cache, nextSet);
        total += res1;
        var res2 = calculateOptions(cursor, jolts, cache, current);
        total += res2;
        return total;

    }

    private static long calculateOptions(int cursor, List<Integer> jolts, Map<CacheKey, Long> cache, List<Integer> nextSet) {
        long result;
        if (cache.containsKey(new CacheKey(nextSet.getLast(), cursor + 1))) {
            result = cache.get(new CacheKey(nextSet.getLast(), cursor + 1));
        } else {
            result = findAllWays(nextSet, cursor + 1, jolts, cache);
            cache.put(new CacheKey(nextSet.getLast(), cursor + 1), result);
        }
        return result;
    }

    @Data
    private static class CacheKey {

        public CacheKey(Integer lastElement, Integer cursor) {
            this.lastElement = lastElement;
            this.cursor = cursor;
        }

        private Integer lastElement;
        private Integer cursor;

    }
}
