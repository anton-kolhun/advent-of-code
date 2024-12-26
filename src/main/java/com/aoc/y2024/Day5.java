package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day5 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day5.txt");
        Map<Integer, List<Integer>> deps = new HashMap<>();
        int lineNumber;
        for (lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isEmpty()) {
                lineNumber++;
                break;
            }
            List<Integer> parts = ParseUtils.splitByDelimiter(line, "\\|").stream().map(Integer::parseInt).toList();
            List<Integer> rightSide = new ArrayList<>();
            rightSide.add(parts.get(1));
            deps.merge(parts.get(0), rightSide, (integers, integers2) -> {
                integers.addAll(integers2);
                return integers;
            });
        }
        List<List<Integer>> correctLines = new ArrayList<>();
        for (int cursor = lineNumber; cursor < lines.size(); cursor++) {
            String line = lines.get(cursor);
            List<Integer> values = ParseUtils.splitByDelimiter(line, ",").stream().map(Integer::parseInt).toList();
            boolean isLineCorrect = true;
            for (int i = 0; i < values.size() - 1; i++) {
                int left = values.get(i);
                List<Integer> rightSide = deps.getOrDefault(left, new ArrayList<>());
                boolean isCorrect = true;
                for (int j = i + 1; j < values.size(); j++) {
                    int right = values.get(j);
                    if (!rightSide.contains(right)) {
                       // System.out.println("incorrect for line: " + cursor + "  ; " + left + " :  " + right);
                        isCorrect = false;
                        break;
                    }
                }
                if (!isCorrect) {
                    isLineCorrect = false;
                    break;
                }
            }
            if (isLineCorrect) {
                correctLines.add(values);
            }
        }
       // System.out.println(correctLines);
        int sum = 0;
        for (List<Integer> correctLine : correctLines) {
            sum = sum + correctLine.get(correctLine.size() / 2);
        }
        System.out.println(sum);
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day5.txt");
        Map<Integer, List<Integer>> deps = new HashMap<>();
        int lineNumber;
        for (lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isEmpty()) {
                lineNumber++;
                break;
            }
            List<Integer> parts = ParseUtils.splitByDelimiter(line, "\\|").stream().map(Integer::parseInt).toList();
            List<Integer> rightSide = new ArrayList<>();
            rightSide.add(parts.get(1));
            deps.merge(parts.get(0), rightSide, (integers, integers2) -> {
                integers.addAll(integers2);
                return integers;
            });
        }

        List<List<Integer>> inCorrectLines = new ArrayList<>();
        for (int cursor = lineNumber; cursor < lines.size(); cursor++) {
            String line = lines.get(cursor);
            List<Integer> values = ParseUtils.splitByDelimiter(line, ",").stream().map(Integer::parseInt).toList();
            boolean isLineCorrect = true;
            for (int i = 0; i < values.size() - 1; i++) {
                int left = values.get(i);
                List<Integer> rightSide = deps.getOrDefault(left, new ArrayList<>());
                boolean isCorrect = true;
                for (int j = i + 1; j < values.size(); j++) {
                    int right = values.get(j);
                    if (!rightSide.contains(right)) {
                        //System.out.println("incorrect for line: " + cursor + "  ; " + left + " :  " + right);
                        isCorrect = false;
                        break;
                    }
                }
                if (!isCorrect) {
                    isLineCorrect = false;
                    break;
                }
            }
            if (!isLineCorrect) {
                inCorrectLines.add(values);
            }
        }
       // System.out.println(inCorrectLines);

        List<ArrayList<Integer>> incorrectRows = inCorrectLines.stream().map(ArrayList::new).toList();

        List<List<Integer>> fixed = new ArrayList<>();
        for (List<Integer> incorrectLine : incorrectRows) {
            List<Integer> updated = incorrectLine;
            for (int j = 0; j < incorrectLine.size(); j++) {
                updated = fixAtCursor(incorrectLine, j, deps);
            }
            fixed.add(updated);
        }
       // System.out.println(fixed);

        int sum = 0;
        for (List<Integer> fixedL : fixed) {
            sum = sum + fixedL.get(fixedL.size() / 2);
        }
        System.out.println(sum);
    }

    private static List<Integer> fixAtCursor(List<Integer> incorrectLine, int cursor, Map<Integer, List<Integer>> deps) {
        int toCheck = incorrectLine.get(cursor);
        List<Integer> fixed = incorrectLine;
        for (int i = cursor + 1; i < incorrectLine.size(); i++) {
            List<Integer> rightSideForRight = deps.getOrDefault(incorrectLine.get(i), new ArrayList<>());
            if (rightSideForRight.contains(toCheck)) {
                incorrectLine.set(cursor, incorrectLine.get(i));
                incorrectLine.set(i, toCheck);
                fixed = fixAtCursor(incorrectLine, cursor, deps);
                break;
            }
        }
        return fixed;
    }
}

