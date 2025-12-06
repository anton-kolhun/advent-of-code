package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Day6 {

    public static void main(String[] args) {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day6.txt");
        Map<Integer, List<Integer>> columns = new HashMap<>();
        for (int j = 0; j < lines.size() - 1; j++) {
            String line = lines.get(j);
            List<Integer> values = ParseUtils.splitByDelimiter(line, " ").stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt).toList();
            for (int i = 0; i < values.size(); i++) {
                List<Integer> colValues = new ArrayList<>();
                colValues.add(values.get(i));
                columns.merge(i, colValues, (oldV, newV) -> {
                    oldV.addAll(newV);
                    return oldV;
                });

            }
        }
        String operationsLine = lines.get(lines.size() - 1);
        List<String> operations = ParseUtils.splitByDelimiter(operationsLine, " ").stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        BigInteger res = calcOperations(operations, columns);
        System.out.println("task1: " + res);
    }

    private static BigInteger calcOperations(List<String> operations, Map<Integer, List<Integer>> columns) {
        BigInteger sum = BigInteger.ZERO;
        for (int column = 0; column < operations.size(); column++) {
            String operation = operations.get(column);
            BiFunction<BigInteger, BigInteger, BigInteger> operator;
            if (operation.equals("+")) {
                operator = BigInteger::add;
            } else {
                operator = BigInteger::multiply;
            }
            List<Integer> colValues = columns.get(column);
            BigInteger cursor = BigInteger.valueOf(colValues.getFirst());
            for (int i = 1; i < colValues.size(); i++) {
                cursor = operator.apply(cursor, BigInteger.valueOf(colValues.get(i)));
            }
            sum = sum.add(cursor);
        }
        return sum;
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day6.txt");
        Map<Integer, List<String>> columns = new HashMap<>();
        List<Integer> currentCursors = new ArrayList<>();
        for (int j = 0; j < lines.size() - 1; j++) {
            currentCursors.add(0);
        }
        int currentCol = 0;
        boolean notEnd = true;
        while (notEnd) {
            List<Integer> nextCursors = new ArrayList<>(currentCursors);
            for (int lineNum = 0; lineNum < lines.size() - 1; lineNum++) {
                String currentLine = lines.get(lineNum);
                int cursor = currentCursors.get(lineNum);
                StringBuilder val = new StringBuilder();
                while (cursor < currentLine.length() && currentLine.charAt(cursor) != ' ') {
                    val.append(currentLine.charAt(cursor));
                    cursor++;
                }
                nextCursors.set(lineNum, cursor);
                if (cursor == currentLine.length()) {
                    notEnd = false;
                }
            }

            int alignedLength = 0;
            for (int i = 0; i < currentCursors.size(); i++) {
                alignedLength = Math.max(alignedLength, nextCursors.get(i) - currentCursors.get(i));
            }

            List<String> colValues = new ArrayList<>();
            for (int lineNum = 0; lineNum < lines.size() - 1; lineNum++) {

                int cursor = currentCursors.get(lineNum);
                StringBuilder val = new StringBuilder();
                for (int i = 0; i < alignedLength; i++) {
                    if ((cursor + i) >= lines.get(lineNum).length()) {
                        val.append(" ");
                    } else {
                        val.append(lines.get(lineNum), cursor + i, cursor + i + 1);
                    }
                }
                colValues.add(val.toString());
            }
            columns.put(currentCol, colValues);

            for (int i = 0; i < nextCursors.size(); i++) {
                currentCursors.set(i, currentCursors.get(i) + alignedLength + 1);
            }
            currentCol++;
        }

        Map<Integer, List<Integer>> transformed = new HashMap<>();
        for (Map.Entry<Integer, List<String>> entry : columns.entrySet()) {
            int column = entry.getKey();
            List<String> values = entry.getValue();
            int maxLength = 0;
            for (String value : values) {
                maxLength = Math.max(maxLength, value.length());
            }
            for (int i = 0; i < maxLength; i++) {
                StringBuffer sb = new StringBuffer();
                for (String value : values) {
                    sb.append(value.charAt(i));
                }
                Integer transValue = Integer.parseInt(sb.toString().replace(" ", ""));
                List<Integer> transValues = new ArrayList<>();
                transValues.add(transValue);
                transformed.merge(column, transValues, (oldV, newV) -> {
                    oldV.addAll(newV);
                    return oldV;
                });
            }
        }
        String operationsLine = lines.get(lines.size() - 1);
        List<String> operations = ParseUtils.splitByDelimiter(operationsLine, " ").stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        BigInteger res = calcOperations(operations, transformed);
        System.out.println("task2: " + res);
    }
}
