package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day10 {

    private static Set<Character> startChars = Set.of('(', '[', '{', '<');
    private static Map<Character, Integer> charToPointsTask2 = Map.of('(', 1, '[', 2, '{', 3, '<', 4);
    private static Map<Character, Integer> charToPointsTask1 = Map.of(')', 3, ']', 57, '}', 1197, '>', 25137);

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day10.txt");
        List<Character> illegalChars = new ArrayList<>();
        for (String line : lines) {
            LinkedList<Character> chars = new LinkedList<>();
            for (char c : line.toCharArray()) {
                if (startChars.contains(c)) {
                    chars.add(c);
                } else {
                    char opposite = getOpposite(c);
                    Character currentOpen = chars.removeLast();
                    if (opposite != currentOpen) {
                        illegalChars.add(c);
                        break;
                    }
                }
            }
        }
        int score = 0;
        for (Character illegalChar : illegalChars) {
            score = score + charToPointsTask1.get(illegalChar);
        }
        System.out.println("task1: " + score);
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day10.txt");
        List<String> legalLines = new ArrayList<>();
        for (String line : lines) {
            boolean validLine = true;
            LinkedList<Character> chars = new LinkedList<>();
            for (char c : line.toCharArray()) {
                if (startChars.contains(c)) {
                    chars.add(c);
                } else {
                    char opposite = getOpposite(c);
                    Character currentOpen = chars.removeLast();
                    if (opposite != currentOpen) {
                        validLine = false;
                        break;
                    }
                }
            }
            if (validLine) {
                legalLines.add(line);
            }
        }
        List<Long> scores = new ArrayList<>();
        for (String legalLine : legalLines) {
            LinkedList<Character> chars = new LinkedList<>();
            for (char c : legalLine.toCharArray()) {
                if (startChars.contains(c)) {
                    chars.add(c);
                } else {
                    chars.removeLast();
                }
            }
            List<Character> missing = new ArrayList<>(chars.reversed());
            long score = 0;
            for (Character c : missing) {
                score = 5 * score + charToPointsTask2.get(c);
            }
            scores.add(score);
        }
        scores.sort(Comparator.comparingLong(o -> o));
        System.out.println("task2: " + scores.get(scores.size() / 2));
    }

    private static char getOpposite(char c) {
        if (c == ']') {
            return '[';
        }
        if (c == '}') {
            return '{';
        }
        if (c == '>') {
            return '<';
        }
        if (c == ')') {
            return '(';
        }
        return '?';
    }
}
