package com.example.aoc_2020;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {

    public static void main(String[] args) throws Exception {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day14.txt");
        List<Action> actions = new ArrayList<>();
        String mask = "";
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("mask")) {
                mask = ParseUtils.splitByDelimiter(line, "=").stream().map(String::trim).toList().get(1);
                continue;
            }
            List<String> actionParts = ParseUtils.splitByDelimiter(line, "=").stream().map(String::trim).toList();
            var indexView = actionParts.get(0);
            int startInd = indexView.indexOf("[");
            int endInd = indexView.indexOf("]");
            String indexStr = indexView.substring(startInd + 1, endInd);
            var action = new Action(Integer.parseInt(indexStr), Long.parseLong(actionParts.get(1)), mask);
            actions.add(action);
        }
        Map<Integer, Long> indexToVal = new HashMap<>();
        for (Action action : actions) {
            long value = resolve(action);
            indexToVal.put(action.index, value);
        }
        long res = indexToVal.values().stream().mapToLong(value -> value).sum();
        System.out.println(res);
    }

    private static long resolve(Action action) {
        String mask = action.mask;
        String res = Long.toBinaryString(action.value);
        int prefix = mask.length() - res.length();
        String pref = "";
        for (int i = 0; i < prefix; i++) {
            pref = pref + "0";
        }
        res = pref + res;

        StringBuffer merged = new StringBuffer();
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == 'X') {
                merged.append(res.charAt(i));
            } else {
                merged.append(mask.charAt(i));
            }
        }
        return Long.parseLong(merged.toString(), 2);
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day14.txt");
        List<Action> actions = new ArrayList<>();
        String mask = "";
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("mask")) {
                mask = ParseUtils.splitByDelimiter(line, "=").stream().map(String::trim).toList().get(1);
                continue;
            }
            List<String> actionParts = ParseUtils.splitByDelimiter(line, "=").stream().map(String::trim).toList();
            var indexView = actionParts.get(0);
            int startInd = indexView.indexOf("[");
            int endInd = indexView.indexOf("]");
            String indexStr = indexView.substring(startInd + 1, endInd);
            var action = new Action(Integer.parseInt(indexStr), Long.parseLong(actionParts.get(1)), mask);
            actions.add(action);
        }
        Map<Long, Long> indexToVal = new HashMap<>();
        for (Action action : actions) {
            List<Long> indexes = resolveIndexes(action);
            for (Long index : indexes) {
                indexToVal.put(index, action.value);
            }
        }
        long res = indexToVal.values().stream().mapToLong(value -> value).sum();
        System.out.println(res);
    }

    private static List<Long> resolveIndexes(Action action) {
        List<Long> indexes = new ArrayList<>();
        String mask = action.mask;
        String res = Long.toBinaryString(action.index);
        int prefix = mask.length() - res.length();
        String pref = "";
        for (int i = 0; i < prefix; i++) {
            pref = pref + "0";
        }
        res = pref + res;

        StringBuffer merged = new StringBuffer();
        int xCounter = 0;
        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == 'X') {
                merged.append("X");
                xCounter++;
            } else if (mask.charAt(i) == '1') {
                merged.append("1");
            } else {
                merged.append(res.charAt(i));
            }
        }

        List<List<Character>> combinations = getCombinations(xCounter);
        for (List<Character> combination : combinations) {
            var result = replaceX(merged.toString(), combination);
            Long longRes = Long.parseLong(result, 2);
            indexes.add(longRes);
        }
        return indexes;
    }

    private static String replaceX(String value, List<Character> combination) {
        StringBuffer updated = new StringBuffer();
        int cursor = 0;
        for (char c : value.toCharArray()) {
            if (c == 'X') {
                updated.append(combination.get(cursor));
                cursor++;
            } else {
                updated.append(c);
            }
        }

        return updated.toString();
    }

    private static List<List<Character>> getCombinations(int xCounter) {
        List<List<Character>> res = getCombinationsRecursively(xCounter, new ArrayList<>());
        return res;
    }

    private static List<List<Character>> getCombinationsRecursively(int xCounter, List<Character> current) {
        if (current.size() == xCounter) {
            return Arrays.asList(current);
        }
        List<List<Character>> total = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Character> next = new ArrayList<>(current);
            next.add(Character.forDigit(i, 10));
            total.addAll(getCombinationsRecursively(xCounter, next));
        }
        return total;
    }


    @AllArgsConstructor
    static class Action {
        int index;
        long value;
        String mask;
    }
}
