package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils;
import com.example.aoc_2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Day23 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("day23.txt");
        Map<Coordinate, Character> coords = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coords.put(new Coordinate(row, col), c);
            }
        }

        //cache cannot be used in such a solution
        findPath(coords, new Coordinate(0, 1), new Coordinate(lines.size() - 1, lines.get(0).length() - 2), lines);


    }

    private static void findPath(Map<Coordinate, Character> coords, Coordinate start, Coordinate end, List<String> lines) {
        List<List<Coordinate>> paths = doFindPath(coords, end, List.of(start), lines);
        int maxPathSize = Integer.MIN_VALUE;
        for (List<Coordinate> path : paths) {
            if (path.size() > maxPathSize) {
                maxPathSize = path.size();
            }
        }
        System.out.println(maxPathSize - 1);

    }

    private static List<List<Coordinate>> doFindPath(Map<Coordinate, Character> coords, Coordinate end,
                                                     List<Coordinate> currentPath, List<String> lines) {
        var cursor = currentPath.get(currentPath.size() - 1);
        if (cursor.equals(end)) {
            //print(currentPath, coords, lines);
            return List.of(currentPath);
        }
        if (coords.getOrDefault(cursor, 'x') == '#') {
            return Collections.emptyList();
        }
        if (cursor.row < 0 || cursor.row >= lines.size() || cursor.col < 0 || cursor.col >= lines.get(0).length()) {
            return Collections.emptyList();
        }
        if (new HashSet<>(currentPath).size() < currentPath.size()) {
            return Collections.emptyList();
        }
        //print(currentPath, coords, lines);
        List<List<Coordinate>> total = new ArrayList<>();
        if (coords.get(cursor) == '>') {
            var res = checkDir(coords, end, currentPath, lines, cursor, GraphUtils.Direction.RIGHT.shift);
            total.addAll(res);
        } else if (coords.get(cursor) == '<') {
            var res = checkDir(coords, end, currentPath, lines, cursor, GraphUtils.Direction.LEFT.shift);
            total.addAll(res);
        } else if (coords.get(cursor) == '^') {
            var res = checkDir(coords, end, currentPath, lines, cursor, GraphUtils.Direction.UP.shift);
            total.addAll(res);
        } else if (coords.get(cursor) == 'v') {
            var res = checkDir(coords, end, currentPath, lines, cursor, GraphUtils.Direction.DOWN.shift);
            total.addAll(res);
        } else {
            var res = checkDir(coords, end, currentPath, lines, cursor, GraphUtils.Direction.RIGHT.shift);
            total.addAll(res);
            res = checkDir(coords, end, currentPath, lines, cursor, GraphUtils.Direction.DOWN.shift);
            total.addAll(res);

            res = checkDir(coords, end, currentPath, lines, cursor, GraphUtils.Direction.LEFT.shift);
            total.addAll(res);

            res = checkDir(coords, end, currentPath, lines, cursor, GraphUtils.Direction.UP.shift);
            total.addAll(res);

        }

        return total;
    }

    private static void print(List<Coordinate> cursor, Map<Coordinate, Character> coords, List<String> lines) {
        var cursSet = new HashSet<>(cursor);
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(0).length(); col++) {
                if (cursSet.contains(new Coordinate(row, col))) {
                    System.out.print("0");
                } else {
                    System.out.print(coords.get(new Coordinate(row, col)));
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static List<List<Coordinate>> checkDir(Map<Coordinate, Character> coords, Coordinate end, List<Coordinate> currentPath, List<String> lines, Coordinate cursor, Coordinate move) {
        ArrayList<Coordinate> nextPath;
        var nextCoord = new Coordinate(cursor.row + move.row, cursor.col + move.col);
        nextPath = new ArrayList<>(currentPath);
        nextPath.add(nextCoord);
        List<List<Coordinate>> total = new ArrayList<>();

        var result = doFindPath(coords, end, nextPath, lines);
        var filtered = result.stream().filter(res -> !res.isEmpty()).toList();
        List<List<Coordinate>> tails = new ArrayList<>();
        for (List<Coordinate> res : filtered) {
            var tail = res.subList(nextPath.size(), res.size());
            tails.add(tail);
        }
        if (!tails.isEmpty()) {
            // cache.put(nextCoord, tails);
        }
        total.addAll(result);

        return total;
    }
}
