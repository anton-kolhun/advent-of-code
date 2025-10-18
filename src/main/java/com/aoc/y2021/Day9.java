package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day9 {
    private static List<Coordinate> moves = List.of(new Coordinate(-1, 0), new Coordinate(0, -1),
            new Coordinate(0, 1), new Coordinate(1, 0));


    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day9.txt");
        Map<Coordinate, Integer> coordsToValues = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coordsToValues.put(new Coordinate(row, col), Character.getNumericValue(charArray[col]));
            }
        }

        Set<Coordinate> matching = new HashSet<>();
        for (Map.Entry<Coordinate, Integer> entry : coordsToValues.entrySet()) {
            boolean match = true;
            for (Coordinate move : moves) {
                var neib = new Coordinate(entry.getKey().row + move.row, entry.getKey().col + move.col);
                if (coordsToValues.getOrDefault(neib, 10) <= entry.getValue()) {
                    match = false;
                    break;
                }
            }
            if (match) {
                matching.add(entry.getKey());
            }
        }
        int sum = 0;
        for (Coordinate coordinate : matching) {
            sum += coordsToValues.get(coordinate) + 1;
        }
        System.out.println("task1: " + sum);

        List<List<Coordinate>> basins = new ArrayList<>();
        for (Coordinate coordinate : matching) {
            List<Coordinate> basin = findBasin(coordinate, coordsToValues);
            basins.add(basin);
        }
        List<Integer> basinSizes = new ArrayList<>(basins.stream().map(List::size).toList());
        basinSizes.sort((o1, o2) -> o2 - o1);
        long total = 1;
        for (int i = 0; i < 3; i++) {
            Integer basinSize = basinSizes.get(i);
            total *= basinSize;

        }
        System.out.println("task2: " + total);
    }

    private static List<Coordinate> findBasin(Coordinate input, Map<Coordinate, Integer> coordsToValues) {
        List<Coordinate> basin = new ArrayList<>();
        basin.add(input);
        List<Coordinate> toScan = new ArrayList<>();
        toScan.add(input);
        Set<Coordinate> visited = new HashSet<>();
        visited.add(input);
        while (!toScan.isEmpty()) {
            Coordinate coordinate = toScan.removeFirst();
            for (Coordinate move : moves) {
                var neib = new Coordinate(move.row + coordinate.row, move.col + coordinate.col);
                if (!visited.contains(neib) && coordsToValues.getOrDefault(neib, 10) < 9) {
                    toScan.add(neib);
                    basin.add(neib);
                    visited.add(neib);
                }
            }
        }
        return basin;
    }
}