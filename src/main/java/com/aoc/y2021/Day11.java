package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day11 {
    private static List<Coordinate> moves = List.of(new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(-1, 1), new Coordinate(0, -1), new Coordinate(0, 1), new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(1, 1));


    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day11.txt");
        Map<Coordinate, Integer> coords = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                coords.put(new Coordinate(row, col), Character.getNumericValue(charArray[col]));
            }
        }
        int maxHeight = lines.size();
        int maxWidth = lines.get(0).length();
        int flashCounter = 0;
        for (int step = 0; step < 1000; step++) {
            Set<Coordinate> flashed = new HashSet<>();
            for (Map.Entry<Coordinate, Integer> entry : coords.entrySet()) {
                Coordinate coord = entry.getKey();
                int value = entry.getValue() + 1;
                if (value > 9) {
                    flashed.add(coord);
                } else {
                    coords.put(coord, value);
                }
            }
            Set<Coordinate> totalFlashed = new HashSet<>(flashed);
            while (!flashed.isEmpty()) {
                var iterator = flashed.iterator();
                var flashedCoord = iterator.next();
                iterator.remove();
                for (Coordinate move : moves) {
                    var neib = new Coordinate(flashedCoord.row + move.row, flashedCoord.col + move.col);
                    if (neib.row < 0 || neib.col < 0 || neib.row >= maxHeight || neib.col >= maxWidth) {
                        continue;
                    }
                    int neibValue = coords.get(neib) + 1;
                    coords.put(neib, neibValue);
                    if (neibValue > 9) {
                        if (!totalFlashed.contains(neib)) {
                            flashed.add(neib);
                            totalFlashed.add(neib);
                        }
                    }
                }
            }
            for (Coordinate coordinate : totalFlashed) {
                coords.put(coordinate, 0);
            }
            if (totalFlashed.size() == coords.size()) {
                int answer = step + 1;
                System.out.println("task2: " + answer);
                break;
            }
            flashCounter += totalFlashed.size();
            if (step == 99) {
                System.out.println("task1: " + flashCounter);
            }
//            print(coords);
        }
    }

    private static void print(Map<Coordinate, Integer> coords) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                System.out.print(coords.get(new Coordinate(row, col)));
            }
            System.out.println();
        }
        System.out.println();
    }

}
