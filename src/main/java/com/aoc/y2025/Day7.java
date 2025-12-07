package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day7 {

    public static void main(String[] args) {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day7.txt");
        Map<Coordinate, Character> coordsToSign = new HashMap<>();
        Coordinate beamStart = null;
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coordsToSign.put(new Coordinate(row, col), c);
                if (c == 'S') {
                    beamStart = new Coordinate(row, col);
                }
            }
        }
        Set<Coordinate> cursors = new LinkedHashSet<>();
        cursors.add(beamStart);
        int currentLine = 0;
        int splitCounter = 0;
        while (currentLine <= lines.size()) {
            Set<Coordinate> nextCursors = new LinkedHashSet<>();
            for (Coordinate cursor : cursors) {
                var nextCursor = new Coordinate(cursor.row + 1, cursor.col);
                if (coordsToSign.getOrDefault(nextCursor, '?') == '^') {
                    splitCounter++;
                    var nextCursor1 = new Coordinate(cursor.row + 1, cursor.col + 1);
                    nextCursors.add(nextCursor1);
                    var nextCursor2 = new Coordinate(cursor.row + 1, cursor.col - 1);
                    nextCursors.add(nextCursor2);
                } else {
                    nextCursors.add(nextCursor);
                }
            }
            cursors = nextCursors;
            currentLine++;
        }
        System.out.println("task1: " + splitCounter);
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day7.txt");
        Map<Coordinate, Character> coordsToSign = new HashMap<>();
        Coordinate beamStart = null;
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coordsToSign.put(new Coordinate(row, col), c);
                if (c == 'S') {
                    beamStart = new Coordinate(row, col);
                }
            }
        }
        BigInteger result = calcAllStates(lines, beamStart, coordsToSign, new HashMap<>());

        System.out.println("task2: " + result);

    }

    private static BigInteger calcAllStates(List<String> lines, Coordinate cursor, Map<Coordinate,
            Character> coordsToSign, Map<Coordinate, BigInteger> cache) {
        if (cursor.row >= lines.size()) {
            return BigInteger.ONE;
        }
        BigInteger total = BigInteger.ZERO;
        var nextCursor = new Coordinate(cursor.row + 1, cursor.col);
        if (coordsToSign.getOrDefault(nextCursor, '?') == '^') {
            var nextCursor1 = new Coordinate(cursor.row + 1, cursor.col + 1);
            BigInteger res1;
            if (cache.containsKey(nextCursor1)) {
                res1 = cache.get(nextCursor1);
            } else {
                res1 = calcAllStates(lines, nextCursor1, coordsToSign, cache);
                cache.put(nextCursor1, res1);
            }
            total = total.add(res1);
            var nextCursor2 = new Coordinate(cursor.row + 1, cursor.col - 1);
            BigInteger res2;
            if (cache.containsKey(nextCursor2)) {
                res2 = cache.get(nextCursor2);
            } else {
                res2 = calcAllStates(lines, nextCursor2, coordsToSign, cache);
                cache.put(nextCursor2, res2);
            }
            total = total.add(res2);
        } else {
            BigInteger res;
            if (cache.containsKey(nextCursor)) {
                res = cache.get(nextCursor);
            } else {
                res = calcAllStates(lines, nextCursor, coordsToSign, cache);
                cache.put(nextCursor, res);
            }
            total = total.add(res);
        }
        return total;
    }
}

