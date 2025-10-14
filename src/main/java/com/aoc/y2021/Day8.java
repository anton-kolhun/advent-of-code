package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day8 {

    //   1
    // 2   3
    //   4
    // 5   6
    //   7
    private static Map<Set<Integer>, String> digitToChars = Map.of(
            Set.of(1, 2, 3, 5, 6, 7), "0",
            Set.of(3, 6), "1",
            Set.of(1, 3, 4, 5, 7), "2",
            Set.of(1, 3, 4, 6, 7), "3",
            Set.of(2, 3, 4, 6), "4",
            Set.of(1, 2, 4, 6, 7), "5",
            Set.of(1, 2, 4, 5, 6, 7), "6",
            Set.of(1, 3, 6), "7",
            Set.of(1, 2, 3, 4, 5, 6, 7), "8",
            Set.of(1, 2, 3, 4, 6, 7), "9");

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day8.txt");
        int counter = 0;
        Set<Integer> matchingPatterns = Set.of(2, 4, 3, 7);
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, "\\|");
            String output = parts.get(1);
            List<String> outputItems = ParseUtils.splitByDelimiter(output, " ");
            for (String outputItem : outputItems) {
                outputItem = outputItem.trim();
                if (matchingPatterns.contains(outputItem.length())) {
                    counter++;
                }
            }
        }
        System.out.println("task1: " + counter);
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day8.txt");
        List<List<Character>> combinations = findCombinations(List.of('a', 'b', 'c', 'd', 'e', 'f', 'g'), Collections.emptyList());
        List<Map<Character, Integer>> transformed = new ArrayList<>();
        for (List<Character> characters : combinations) {
            Map<Character, Integer> map = new HashMap<>();
            for (int i = 0; i < characters.size(); i++) {
                Character character = characters.get(i);
                map.put(character, i);
            }
            transformed.add(map);
        }
        long total = 0;
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, "\\|");
            String input = parts.get(0);
            List<String> inputItems = ParseUtils.splitByDelimiter(input, " ");
            Map<Character, Integer> mapping = findMapping(transformed, inputItems);
            String output = parts.get(1);
            List<String> outputItems = ParseUtils.splitByDelimiter(output, " ").stream().filter(s -> !s.isEmpty()).toList();
            StringBuilder totalSb = new StringBuilder();
            for (String outputItem : outputItems) {
                Set<Integer> code = new HashSet<>();
                for (char c : outputItem.toCharArray()) {
                    code.add(mapping.get(c) + 1);
                }
                totalSb.append(digitToChars.get(code));
            }
            total = total + Integer.parseInt(totalSb.toString());
        }
        System.out.println("task2: " + total);
    }

    private static Map<Character, Integer> findMapping(List<Map<Character, Integer>> mappings, List<String> inputItems) {
        var m = Map.of('a', 2, 'b', 5, 'c', 6, 'd', 0, 'e', 1, 'f', 3, 'g', 4);
        for (Map<Character, Integer> mapping : mappings) {
            boolean correctMapping = true;
            var digitToCharsCopy = new HashMap<>(digitToChars);
            for (String inputItem : inputItems) {
                Set<Integer> code = new HashSet<>();
                for (char c : inputItem.toCharArray()) {
                    int intValue = mapping.get(c) + 1;
                    code.add(intValue);
                }
                if (digitToCharsCopy.remove(code) == null) {
                    correctMapping = false;
                    break;
                }
            }
            if (correctMapping) {
                return mapping;
            }
        }
        return Collections.emptyMap();
    }


    private static List<List<Character>> findCombinations(List<Character> remChars, List<Character> current) {
        if (remChars.isEmpty()) {
            return List.of(current);
        }
        List<List<Character>> total = new ArrayList<>();
        for (Character remChar : remChars) {
            List<Character> nextCurrent = new ArrayList<>(current);
            nextCurrent.add(remChar);
            List<Character> nextRem = new ArrayList<>(remChars);
            nextRem.remove(remChar);
            List<List<Character>> combins = findCombinations(nextRem, nextCurrent);
            total.addAll(combins);
        }
        return total;
    }

}
