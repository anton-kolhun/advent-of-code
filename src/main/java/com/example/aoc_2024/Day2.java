package com.example.aoc_2024;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.List;

public class Day2 {

    public static void main(String[] args) {
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day2.txt");
        List<List<Integer>> reports = new ArrayList<>();
        for (String line : lines) {
            List<Integer> row = ParseUtils.splitByDelimiter(line, " ").stream().map(Integer::parseInt).toList();
            reports.add(row);
        }

        int validNum = 0;
        for (List<Integer> report : reports) {
            if (isValid(report)) {
                validNum++;
            }
        }
        System.out.println(validNum);
    }

    private static boolean isValid(List<Integer> report) {
        if ((report.get(1) - report.get(0)) == 0) {
            return false;
        }
        if (Math.abs(report.get(1) - report.get(0)) < 1 || Math.abs(report.get(1) - report.get(0)) > 3) {
            return false;
        }


        boolean isIncreasing = (report.get(1) - report.get(0)) > 0;
        for (int j = 2; j < report.size(); j++) {
            Integer value = report.get(j);
            Integer previous = report.get(j - 1);
            if (isIncreasing & (value < previous)) {
                return false;
            }
            if (!isIncreasing & (value > previous)) {
                return false;
            }
            if (Math.abs(value - previous) < 1 || Math.abs(value - previous) > 3) {
                return false;
            }
        }
        return true;
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day2.txt");
        List<List<Integer>> reports = new ArrayList<>();
        for (String line : lines) {
            List<Integer> row = ParseUtils.splitByDelimiter(line, " ").stream().map(Integer::parseInt).toList();
            reports.add(row);
        }

        int validNum = 0;
        for (List<Integer> report : reports) {
            if (isValid2(report)) {
                validNum++;
            }
        }
        System.out.println(validNum);
    }

    private static boolean isValid2(List<Integer> report) {

        List<List<Integer>> comb = findAllMinus1(report, new ArrayList<>(), false);
        for (List<Integer> values : comb) {
            if (isValid(values)) {
                return true;
            }
        }

        return false;

    }

    private static List<List<Integer>> findAllMinus1(List<Integer> remaining, List<Integer> current, boolean isSkipped) {
        if (remaining.isEmpty()) {
            return List.of(current);
        }
        List<List<Integer>> total = new ArrayList<>();
        List<Integer> nextRem = new ArrayList<>(remaining);
        Integer removed = nextRem.remove(0);
        var nextCur = new ArrayList<>(current);
        nextCur.add(removed);
        var nextRes = findAllMinus1(nextRem, nextCur, isSkipped);
        total.addAll(nextRes);
        if (!isSkipped) {
            var nextRes2 = findAllMinus1(nextRem, current, true);
            total.addAll(nextRes2);
        }
        return total;
    }
}
