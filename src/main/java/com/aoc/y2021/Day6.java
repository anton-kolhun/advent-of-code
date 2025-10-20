package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day6.txt");
        List<Integer> fishes = ParseUtils.splitByDelimiter(lines.get(0), ",")
                .stream()
                .map(Integer::parseInt).toList();

        for (int day = 0; day < 80; day++) {
            List<Integer> nextFishes = new ArrayList<>();
            List<Integer> tail = new ArrayList<>();
            for (Integer fish : fishes) {
                var nextFish = fish - 1;
                if (nextFish < 0) {
                    nextFish = 6;
                    tail.add(8);
                }
                nextFishes.add(nextFish);
            }

            nextFishes.addAll(tail);
            fishes = nextFishes;
//            System.out.println(fishes);
        }
        System.out.println("task1: " + fishes.size());
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day6.txt");
        List<Integer> fishes = ParseUtils.splitByDelimiter(lines.get(0), ",")
                .stream()
                .map(Integer::parseInt).toList();

        BigInteger total = BigInteger.ZERO;
        for (Integer fish : fishes) {
            BigInteger res = calcSize(fish, 256, new HashMap<>());
            total = total.add(res);
        }
        System.out.println("task2: " + total);
    }

    private static BigInteger calcSize(Integer value, int remainingSteps, Map<CacheKey, BigInteger> cache) {
        if (remainingSteps == 0) {
            return BigInteger.ONE;
        }
        var key = new CacheKey(value, remainingSteps);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        List<Integer> nextValues = new ArrayList<>();
        int nextValue = value - 1;
        if (nextValue < 0) {
            nextValues.add(6);
            nextValues.add(8);
        } else {
            nextValues.add(nextValue);
        }
        BigInteger total = BigInteger.ZERO;
        for (Integer val : nextValues) {
            total = total.add(calcSize(val, remainingSteps - 1, cache));
        }
        cache.put(key, total);
        return total;
    }

    private static class CacheKey {
        int stepRem;
        int value;

        public CacheKey(int stepRem, int value) {
            this.stepRem = stepRem;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;
            return stepRem == cacheKey.stepRem && value == cacheKey.value;
        }

        @Override
        public int hashCode() {
            int result = stepRem;
            result = 31 * result + value;
            return result;
        }
    }
}


