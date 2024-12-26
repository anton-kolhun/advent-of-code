package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day10 {


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
        List<List<Integer>> arrangements = findAllWays(initial, 1, jolts, new HashMap<>());
        System.out.println(arrangements.size());
    }

    private static List<List<Integer>> findAllWays(List<Integer> current, int cursor, List<Integer> jolts,
                                                   Map<CacheKey, List<List<Integer>>> cache) {
        if (Objects.equals(current.getLast(), jolts.getLast())) {
            return Arrays.asList(current);
        }
        if (cursor >= jolts.size()) {
            return Collections.emptyList();
        }
        var nextJolt = jolts.get(cursor);
        if (nextJolt - current.getLast() > 3) {
            return Collections.emptyList();
        }
        List<Integer> nextSet = new ArrayList<>(current);
        nextSet.add(nextJolt);
        List<List<Integer>> total = new ArrayList<>();
        List<List<Integer>> res1 = new ArrayList<>();
        if (cache.containsKey(new CacheKey(nextSet.getLast(), cursor + 1))) {
            List<List<Integer>> tails = cache.get(new CacheKey(nextSet.getLast(), cursor + 1));
            for (List<Integer> tail : tails) {
                List<Integer> x = new ArrayList<>(nextSet);
                x.addAll(tail);
                res1.add(x);
            }

        } else {
            res1 = findAllWays(nextSet, cursor + 1, jolts, cache);
            List<List<Integer>> tails = new ArrayList<>();
            for (List<Integer> result1 : res1) {
                var tail = result1.subList(cursor + 1, result1.size());
                tails.add(tail);
            }
            cache.put(new CacheKey(nextSet.getLast(), cursor + 1), tails);
        }
        total.addAll(res1);

        List<List<Integer>> res2 = new ArrayList<>();
        if (cache.containsKey(new CacheKey(current.getLast(), cursor + 1))) {
            List<List<Integer>> tails = cache.get(new CacheKey(current.getLast(), cursor + 1));
            for (List<Integer> tail : tails) {
                List<Integer> x = new ArrayList<>(current);
                x.addAll(tail);
                res2.add(x);
            }
        } else {
            res2 = findAllWays(current, cursor + 1, jolts, cache);
            List<List<Integer>> tails = new ArrayList<>();
            for (List<Integer> result1 : res2) {
                var tail = result1.subList(cursor + 1, result1.size());
                tails.add(tail);
            }
            cache.put(new CacheKey(current.getLast(), cursor + 1), tails);
        }
        total.addAll(res2);
        return total;

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
