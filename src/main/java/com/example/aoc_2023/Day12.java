package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day12 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("day12_t.txt");
        BigInteger total = BigInteger.ZERO;
        for (String line : lines) {
            BigInteger sum = processLineV2(line);
            total = total.add(sum);
        }
        System.out.println("");
        System.out.println(total);
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("day12.txt");
        long total = 0;
        for (String line : lines) {
            long sum = processLine(line);
            total += sum;
        }
        System.out.println("");
        System.out.println(total);
    }

    private static long processLine(String line) {
        List<String> data = ParseUtils.splitByDelimiter(line, " ");
        List<String> groupsStr = ParseUtils.splitByDelimiter(data.get(1), ",");
        List<Integer> groups = groupsStr.stream().map(Integer::parseInt).toList();
        List<List<Character>> res = doCheck(data.get(0), 0, groups, 0, new ArrayList<>());
        for (List<Character> re : res) {
            for (Character character : re) {
                System.out.print(character);
            }

        }
        return res.size();
    }

    private static BigInteger processLineV2(String line) {
        List<String> data = ParseUtils.splitByDelimiter(line, " ");
        List<String> groupsStr = ParseUtils.splitByDelimiter(data.get(1), ",");
        List<Integer> groups = groupsStr.stream().map(Integer::parseInt).toList();
        List<List<Character>> res1 = doCheck(data.get(0), 0, groups, 0, new ArrayList<>());
        String c = "?";
//        if (data.get(0).charAt(0) == '#' || data.get(0).charAt(0) == '?') {
//            c = ".";
//        }
        List<List<Character>> res2 = doCheck(c + data.get(0), 0, groups, 0, new ArrayList<>());

//        for (List<Character> re : res) {
//            for (Character character : re) {
//                System.out.print(character);
//            }
//
//        }

//        int counterStart = 0;
//        int counterEnd = 0;
//        for (List<Character> characters : res1) {
//            if (characters.get(characters.size() - 1) == '#') {
//                counterEnd++;
//            }
//        }

        int counterStart = 0;
        int counterEnd = 0;
        int counter = 0;
        List<List<Character>> res = new ArrayList<>();
        for (List<Character> chars1 : res1) {
            for (List<Character> chars2 : res2) {
                if (chars1.get(chars1.size() - 1) != '#' || chars2.get(0) != '#') {
                    counter++;
                    res.add(chars2);
                }
            }
        }
        List<List<Character>> currentRes = new ArrayList<>(res);
        for (int i = 0; i < 3; i++) {
            List<List<Character>> nextRes = new ArrayList<>();
            for (List<Character> chars1 : currentRes) {
                for (List<Character> chars2 : res2) {
                    if (chars1.get(chars1.size() - 1) != '#' || chars2.get(0) != '#') {
                        nextRes.add(chars2);
                    }
                }
            }
            currentRes = nextRes;
        }


        BigInteger v1 = BigInteger.valueOf(res1.size());
        BigInteger v2 = BigInteger.valueOf(res2.size());
        BigInteger total = v1.multiply(v2).multiply(v2).multiply(v2).multiply(v2);

        return total;
    }

    private static List<List<Character>> doCheck(String line, int cursorLine, List<Integer> groups, int cursorGroup, List<Character> current) {

        if (cursorLine > line.length()) {
            return Collections.emptyList();
        } else if (cursorLine == line.length()) {
            if (cursorGroup == groups.size()) {
                return Arrays.asList(current);
            } else {
                return Collections.emptyList();
            }
        }
//        if (cursorGroup >= groups.size()) {
//            return Collections.emptyList();
//        }

        char cur = line.charAt(cursorLine);
        List<List<Character>> total = new ArrayList<>();
        if (cur == '?') {
            List<Character> next = new ArrayList<>(current);
            next.add('.');
            var res1 = doCheck(line, cursorLine + 1, groups, cursorGroup, next);
            total.addAll(res1);
            List<List<Character>> res = procDash(line, cursorLine, groups, cursorGroup, current);
            total.addAll(res);
        } else if (cur == '.') {
            List<Character> next = new ArrayList<>(current);
            next.add('.');
            var res = doCheck(line, cursorLine + 1, groups, cursorGroup, next);
            total.addAll(res);
        } else if (cur == '#') {
            List<List<Character>> res = procDash(line, cursorLine, groups, cursorGroup, current);
            total.addAll(res);
        }
        return total;
    }

    private static List<List<Character>> procDash(String line, int cursorLine, List<Integer> groups, int cursorGroup, List<Character> current) {
        if (cursorGroup >= groups.size()) {
            return Collections.emptyList();
        }
        List<List<Character>> total = new ArrayList<>();
        char cur;
        int groupSize = groups.get(cursorGroup);
        var next = new ArrayList<>(current);
        if (current.size() + groupSize > line.length()) {
            return total;
        }
        for (int i = 0; i < groupSize; i++) {
            cur = line.charAt(cursorLine + i);
            if (!(cur == '?' || cur == '#')) {
                return total;
            }
            next.add('#');
        }

        List<List<Character>> res2;
        if (cursorGroup < groups.size() - 1) {
            if (current.size() + groupSize + 1 > line.length()) {
                return total;
            }
            cur = line.charAt(cursorLine + groupSize);
            if (!(cur == '?' || cur == '.')) {
                return total;
            }
            next.add('.');
            res2 = doCheck(line, cursorLine + groupSize + 1, groups, cursorGroup + 1, next);
        } else {
            res2 = doCheck(line, cursorLine + groupSize, groups, cursorGroup + 1, next);
        }
        total.addAll(res2);
        return total;
    }
}

