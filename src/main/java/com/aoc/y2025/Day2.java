package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day2 {

    private static List<String> digits = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    public static void main(String[] args) {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day2.txt");
        List<String> ranges = ParseUtils.splitByDelimiter(lines.get(0), ",");
        List<BigInteger> incorrect = new ArrayList<>();
        for (String range : ranges) {
            List<String> rangeVals = ParseUtils.splitByDelimiter(range, "-")
                    .stream()
                    .toList();
            List<List<String>> totalNumbs = new ArrayList<>();
            int start = rangeVals.getFirst().length() / 2;
            int end = rangeVals.getLast().length() / 2;
            for (int i = start; i <= end; i++) {
                List<List<String>> total = findNumbs(i);
                totalNumbs.addAll(total);
            }
            for (List<String> numb : totalNumbs) {
                if (numb.size() > 0 && !numb.getFirst().equals("0")) {
                    BigInteger concat = getConcat(numb);
                    if (concat.compareTo(new BigInteger(rangeVals.getFirst())) >= 0
                            && concat.compareTo(new BigInteger(rangeVals.getLast())) <= 0) {
                        incorrect.add(concat);
                    }
                }

            }
        }
        BigInteger sum = incorrect.stream().reduce(BigInteger::add).get();
        System.out.println("task1: " + sum);

    }

    private static BigInteger getConcat(List<String> numb) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numb.size(); i++) {
            sb.append(numb.get(i));
        }
        for (int i = 0; i < numb.size(); i++) {
            sb.append(numb.get(i));
        }
        return new BigInteger(sb.toString());
    }


    private static List<List<String>> findNumbs(int size) {
        return findNumbsRecursively(size, new ArrayList<>());
    }

    private static List<List<String>> findNumbsRecursively(int rem, List<String> current) {
        if (rem == 0) {
            return List.of(current);
        }
        List<List<String>> total = new ArrayList<>();
        for (String digit : digits) {
            List<String> mext = new ArrayList<>(current);
            mext.add(digit);
            List<List<String>> res = findNumbsRecursively(rem - 1, mext);
            total.addAll(res);
        }
        return total;
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day2.txt");
        List<String> ranges = ParseUtils.splitByDelimiter(lines.get(0), ",");
        Set<BigInteger> totalMatching = new HashSet<>();
        for (String range : ranges) {
            List<String> rangeVals = ParseUtils.splitByDelimiter(range, "-")
                    .stream()
                    .toList();
            List<List<String>> totalNumbs = new ArrayList<>();
            int start = rangeVals.getFirst().length() / 2;
            int end = rangeVals.getLast().length() / 2;
            for (int i = 1; i <= Math.max(start, end); i++) {
                List<List<String>> total = findNumbs(i);
                totalNumbs.addAll(total);
            }
            for (List<String> numb : totalNumbs) {
                if (!numb.isEmpty() && !numb.getFirst().equals("0")) {
                    Set<BigInteger> matchingNumbs = getConcatTask2(numb, new BigInteger(rangeVals.getFirst()), new BigInteger(rangeVals.getLast()));
                    totalMatching.addAll(matchingNumbs);
                }
            }
        }
        BigInteger sum = totalMatching.stream().reduce(BigInteger::add).get();
        System.out.println("task2: " + sum);
    }

    private static Set<BigInteger> getConcatTask2(List<String> numb, BigInteger minVal, BigInteger maxVal) {
        Set<BigInteger> matchingNumbs = new HashSet<>();
        StringBuilder step = new StringBuilder();
        numb.forEach(digit -> step.append(new BigInteger(digit)));
        StringBuilder cursor = new StringBuilder(step);
        cursor.append(step);

        while (new BigInteger(cursor.toString()).compareTo(maxVal) <= 0) {
            if ((new BigInteger(cursor.toString()).compareTo(minVal) >= 0)
                    && (new BigInteger(cursor.toString()).compareTo(maxVal) <= 0)) {
                matchingNumbs.add(new BigInteger(cursor.toString()));
            }
            cursor.append(step);
        }
        return matchingNumbs;
    }

}
