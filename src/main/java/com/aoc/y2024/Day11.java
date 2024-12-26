package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Day11 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day11.txt");
        List<BigInteger> numbers = ParseUtils.splitByDelimiter(lines.get(0), " ").stream()
                .map(Integer::parseInt)
                .map(BigInteger::valueOf)
                .toList();

        int steps = 25;

        List<BigInteger> updated = new ArrayList<>(numbers);
        for (int step = 0; step < steps; step++) {
            List<BigInteger> nextUpdated = new ArrayList<>();
            for (BigInteger bigInteger : updated) {
                if (bigInteger.equals(BigInteger.ZERO)) {
                    nextUpdated.add(BigInteger.ONE);
                } else if ((bigInteger.toString().length() % 2) == 0) {
                    String part1 = bigInteger.toString().substring(0, bigInteger.toString().length() / 2);
                    String part2 = bigInteger.toString().substring(bigInteger.toString().length() / 2);
                    nextUpdated.add(new BigInteger(part1));
                    nextUpdated.add(new BigInteger(part2));
                } else {
                    var value = bigInteger.multiply(BigInteger.valueOf(2024));
                    nextUpdated.add(value);

                }
            }
            updated = nextUpdated;
        }

        System.out.println(updated.size());
    }


    public static void task2() throws ExecutionException, InterruptedException {
        List<String> lines = FilesUtils.readFile("aoc_2024/day11.txt");
        List<Long> numbers = ParseUtils.splitByDelimiter(lines.get(0), " ").stream()
                .map(Long::parseLong)
                .toList();
        int numbfOfSteps = 75;
        Long res = calculateSum(numbers, numbfOfSteps, new HashMap<>());
        System.out.println(res);
    }

    private static long calculateSum(List<Long> numbers, int stepsLeft, Map<CacheKey, Long> cache) {
        if (stepsLeft == 0) {
            return numbers.size();
        }
        long total = 0;
        for (Long value : numbers) {
            var cacheKey = new CacheKey(value, stepsLeft);
            if (cache.containsKey(cacheKey)) {
                Long res = cache.get(cacheKey);
                total = total + res;
            } else {
                List<Long> updatedVal = applyRule(value);
                long res = calculateSum(updatedVal, stepsLeft - 1, cache);
                total = total + res;
                cache.put(cacheKey, res);
            }
        }
        return total;
    }


    private static List<Long> applyRule(Long value) {
        var result = new ArrayList<Long>();
        if (value == 0) {
            result.add(1l);
        } else if ((value.toString().length() % 2) == 0) {
            String part1 = value.toString().substring(0, value.toString().length() / 2);
            String part2 = value.toString().substring(value.toString().length() / 2);
            result.add(Long.parseLong(part1));
            result.add(Long.parseLong(part2));
        } else {
            var newVal = value * 2024;
            result.add(newVal);
        }
        return result;
    }


    @Data
    @AllArgsConstructor
    static class CacheKey {
        Long value;
        int inSteps;
    }
}


