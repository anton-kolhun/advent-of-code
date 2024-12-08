package com.example.aoc_2024;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day7.txt");
        Map<Long, List<Long>> equations = new HashMap<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, ":").stream().map(String::trim).toList();
            List<Long> numbers = ParseUtils.splitByDelimiter(parts.get(1), " ").stream().map(Long::parseLong).toList();
            equations.put(Long.parseLong(parts.get(0)), numbers);
        }

        long sum = 0;
        for (Map.Entry<Long, List<Long>> entry : equations.entrySet()) {
            List<Long> values = new ArrayList<>(entry.getValue());
            Long first = values.removeFirst();
            List<Character> operators = findOperators(entry.getKey(), values, first, new ArrayList<>());
            if (!operators.isEmpty()) {
                sum = sum + entry.getKey();
            }
        }

        System.out.println(sum);


    }

    private static List<Character> findOperators(long result, List<Long> remainingValues, long currentResult, List<Character> currentOperators) {
        if (remainingValues.isEmpty()) {
            if (currentResult == result) {
                return currentOperators;
            } else {
                return Collections.emptyList();
            }
        }

        List<Character> nextOperators = new ArrayList<>(currentOperators);
        nextOperators.add('+');
        List<Long> nextRem = new ArrayList<>(remainingValues);
        long removed = nextRem.removeFirst();
        var res1 = findOperators(result, nextRem, currentResult + removed, nextOperators);
        if (!res1.isEmpty()) {
            return res1;
        }

        List<Character> nextOperators2 = new ArrayList<>(currentOperators);
        nextOperators2.add('*');
        List<Long> nextRem2 = new ArrayList<>(remainingValues);
        long removed2 = nextRem2.removeFirst();
        var res2 = findOperators(result, nextRem2, currentResult * removed2, nextOperators2);
        if (!res2.isEmpty()) {
            return res2;
        }
        return Collections.emptyList();
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day7.txt");
        Map<Long, List<Long>> equations = new HashMap<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, ":").stream().map(String::trim).toList();
            List<Long> numbers = ParseUtils.splitByDelimiter(parts.get(1), " ").stream().map(Long::parseLong).toList();
            equations.put(Long.parseLong(parts.get(0)), numbers);
        }

        long sum = 0;
        for (Map.Entry<Long, List<Long>> entry : equations.entrySet()) {
            List<Long> values = new ArrayList<>(entry.getValue());
            Long first = values.removeFirst();
            List<Character> operators = findOperators2(entry.getKey(), values, first, new ArrayList<>());
            if (!operators.isEmpty()) {
                sum = sum + entry.getKey();
            }
        }

        System.out.println(sum);


    }

    private static List<Character> findOperators2(long result, List<Long> remainingValues, long currentResult, List<Character> currentOperators) {
        if (remainingValues.isEmpty()) {
            if (currentResult == result) {
                return currentOperators;
            } else {
                return Collections.emptyList();
            }
        }

        List<Character> nextOperators = new ArrayList<>(currentOperators);
        nextOperators.add('+');
        List<Long> nextRem = new ArrayList<>(remainingValues);
        long removed = nextRem.removeFirst();
        var res1 = findOperators2(result, nextRem, currentResult + removed, nextOperators);
        if (!res1.isEmpty()) {
            return res1;
        }

        List<Character> nextOperators2 = new ArrayList<>(currentOperators);
        nextOperators2.add('*');
        List<Long> nextRem2 = new ArrayList<>(remainingValues);
        long removed2 = nextRem2.removeFirst();
        var res2 = findOperators2(result, nextRem2, currentResult * removed2, nextOperators2);
        if (!res2.isEmpty()) {
            return res2;
        }

        List<Character> nextOperators3 = new ArrayList<>(currentOperators);
        nextOperators3.add('|');
        List<Long> nextRem3 = new ArrayList<>(remainingValues);
        long removed3 = nextRem3.removeFirst();
        var res3 = findOperators2(result, nextRem3, concat(currentResult, removed3), nextOperators3);
        if (!res3.isEmpty()) {
            return res3;
        }

        return Collections.emptyList();
    }

    private static long concat(long expr1, long expr2) {
        String concatStr =  String.valueOf(expr1) + String.valueOf(expr2);
        return Long.parseLong(concatStr);
    }
}
