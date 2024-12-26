package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1 {

    public static void main(String[] args) {
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day1.txt");
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        for (String line : lines) {
            List<Integer> pair = ParseUtils.splitByDelimiter(line, " ").stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt).toList();
            l1.add(pair.get(0));
            l2.add(pair.get(1));
        }
        l1.sort(Integer::compareTo);
        l2.sort(Integer::compareTo);
        int sum = 0;
        for (int j = 0; j < l1.size(); j++) {
            sum = sum + Math.abs(l1.get(j) - l2.get(j));
        }
        System.out.println(sum);

    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day1.txt");
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        Map<Integer, Integer> valToOccur2 = new HashMap<>();
        for (String line : lines) {
            List<Integer> pair = ParseUtils.splitByDelimiter(line, " ").stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt).toList();
            l1.add(pair.get(0));
            l2.add(pair.get(1));
            List<Integer> values = new ArrayList<>();
            values.add(pair.get(1));
            valToOccur2.merge(pair.get(1), 1, Integer::sum);
        }
        int sum = 0;
        for (int j = 0; j < l1.size(); j++) {
            sum = sum + (valToOccur2.getOrDefault(l1.get(j), 0) * l1.get(j));
        }
        System.out.println(sum);

    }
}
