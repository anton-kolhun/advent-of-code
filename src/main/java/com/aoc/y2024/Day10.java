package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day10 {

    public static void main(String[] args) {
        task1And2();
    }

    public static void task1And2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day10.txt");
        Map<Coord, Integer> coordToValue = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coordToValue.put(new Coord(row, col), getValue(c));
            }
        }

        List<Coord> moves = List.of(
                new Coord(0, 1),
                new Coord(0, -1),
                new Coord(1, 0),
                new Coord(-1, 0)
        );
        List<List<Coord>> total = new ArrayList<>();
        Set<Set<Coord>> pairs = new HashSet<>();
        for (Map.Entry<Coord, Integer> entry : coordToValue.entrySet()) {
            if (entry.getValue() == 0) {
                List<Coord> start = new ArrayList<>();
                start.add(entry.getKey());
                List<List<Coord>> paths = findPaths(coordToValue, start, moves);
                for (List<Coord> path : paths) {
                    pairs.add(Set.of(path.getFirst(), path.getLast()));
                }
                total.addAll(paths);

            }
        }
        System.out.println(pairs.size());
        System.out.println(total.size());

    }

    private static int getValue(char c) {
        if (c == '.') {
            return -1;
        }
        return Integer.parseInt(String.valueOf(c));
    }

    private static List<List<Coord>> findPaths(Map<Coord, Integer> coordToValue, List<Coord> current, List<Coord> moves) {
        if (current.size() == 10) {
            return List.of(current);
        }
        var lastCoordValue = coordToValue.get(current.getLast());
        List<List<Coord>> total = new ArrayList<>();
        for (Coord move : moves) {
            var nextCoord = new Coord(current.getLast().row + move.row, current.getLast().col + move.col);
            if ((lastCoordValue + 1) == coordToValue.getOrDefault(nextCoord, -1)) {
                var nextCur = new ArrayList<>(current);
                nextCur.add(nextCoord);
                var res = findPaths(coordToValue, nextCur, moves);
                total.addAll(res);
            }
        }
        return total;

    }

    @Data
    @AllArgsConstructor
    static class Coord {
        int row;
        int col;
    }
}
