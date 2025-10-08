package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.ParseUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day5 {

    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day5.txt");
        Map<Coordinate, Integer> coordToOccurT1 = new HashMap<>();
        Map<Coordinate, Integer> coordToOccurT2 = new HashMap<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, "->").stream().map(String::trim).toList();
            var c1 = ParseUtils.splitByDelimiter(parts.get(0), ",");
            Coordinate coord1 = new Coordinate(Integer.parseInt(c1.get(0)), Integer.parseInt(c1.get(1)));
            var c2 = ParseUtils.splitByDelimiter(parts.get(1), ",");
            Coordinate coord2 = new Coordinate(Integer.parseInt(c2.get(0)), Integer.parseInt(c2.get(1)));
            int rowMax = Math.max(coord1.row, coord2.row);
            int rowMin = Math.min(coord1.row, coord2.row);
            int colMax = Math.max(coord1.col, coord2.col);
            int colMin = Math.min(coord1.col, coord2.col);
            if (coord1.row == coord2.row || coord1.col == coord2.col) {
                for (int row = rowMin; row <= rowMax; row++) {
                    for (int col = colMin; col <= colMax; col++) {
                        coordToOccurT1.merge(new Coordinate(row, col), 1, Integer::sum);
                        coordToOccurT2.merge(new Coordinate(row, col), 1, Integer::sum);
                    }
                }
            } else {
                int rowShift;
                if (coord1.row > coord2.row) {
                    rowShift = -1;
                } else {
                    rowShift = 1;
                }
                int colShift;
                if (coord1.col > coord2.col) {
                    colShift = -1;
                } else {
                    colShift = 1;
                }
                var cursor = coord1;
                while (!cursor.equals(coord2)) {
                    coordToOccurT2.merge(cursor, 1, Integer::sum);
                    cursor = new Coordinate(cursor.row + rowShift, cursor.col + colShift);
                }
                coordToOccurT2.merge(coord2, 1, Integer::sum);
            }
        }
        var overOneOccurT1 = coordToOccurT1.values().stream().filter(occur -> occur > 1).toList();
        System.out.println("task1: " + overOneOccurT1.size());
        var overOneOccurT2 = coordToOccurT2.values().stream().filter(occur -> occur > 1).toList();
        System.out.println("task2: " + overOneOccurT2.size());

    }
}
