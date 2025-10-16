package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day13 {
    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day13.txt");
        Set<Coordinate> coordinates = new HashSet<>();
        int lineNumber = 0;
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isEmpty()) {
                break;
            }
            List<String> parts = ParseUtils.splitByDelimiter(line, ",");
            coordinates.add(new Coordinate(Integer.parseInt(parts.get(1)), Integer.parseInt(parts.get(0))));
        }
        lineNumber++;

        List<FoldingInfo> foldings = new ArrayList<>();
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            List<String> parts = ParseUtils.splitByDelimiter(line, "=");
            String foldPart = parts.get(0);
            foldings.add(new FoldingInfo(Integer.parseInt(parts.get(1)), foldPart.substring(foldPart.length() - 1)));
        }

        for (int i = 0; i < foldings.size(); i++) {
            FoldingInfo folding = foldings.get(i);
            Set<Coordinate> updated = new HashSet<>();
            for (Coordinate coordinate : coordinates) {
                var newCoord = folding.foldCoordinate(coordinate);
                updated.add(newCoord);
            }
            coordinates = updated;
            if (i == 0) {
                System.out.println("task1: " + updated.size());
            }
        }
        print(coordinates);
        System.out.println("task2: BCZRCEAB"); //based on the print above

    }

    private static void print(Set<Coordinate> coordinates) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 50; col++) {
                if (coordinates.contains(new Coordinate(row, col))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    static class FoldingInfo {
        int value;
        String coordinate;

        public FoldingInfo(int value, String coordinate) {
            this.value = value;
            this.coordinate = coordinate;
        }

        public Coordinate foldCoordinate(Coordinate c) {
            if (coordinate.equals("x")) {
                int foldingValue = c.col;
                if (foldingValue > value) {
                    return new Coordinate(c.row, value - (foldingValue - value));
                }
            } else {
                int foldingValue = c.row;
                if (foldingValue > value) {
                    return new Coordinate(value - (foldingValue - value), c.col);
                }
            }
            return c;
        }

    }
}
