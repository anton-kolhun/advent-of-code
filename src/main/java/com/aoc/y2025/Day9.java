package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day9 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day9.txt");
        List<Coordinate> coords = new ArrayList<>();
        for (String line : lines) {
            List<Integer> values = ParseUtils.splitByDelimiter(line, ",")
                    .stream().map(Integer::parseInt).toList();
            coords.add(new Coordinate(values.get(1), values.get(0)));
        }

        BigInteger max = BigInteger.ZERO;
        for (int i = 0; i < coords.size() - 1; i++) {
            Coordinate c1 = coords.get(i);
            for (int j = i + 1; j < coords.size(); j++) {
                Coordinate c2 = coords.get(j);
                BigInteger square = calculateSquare(c1, c2);
                if (square.compareTo(max) > 0) {
                    max = square;
                }
            }
        }
        System.out.println("task1: " + max);
    }

    private static BigInteger calculateSquare(Coordinate c1, Coordinate c2) {
        return BigInteger.valueOf(Math.abs(c1.row - c2.row) + 1).multiply(
                BigInteger.valueOf(Math.abs(c1.column - c2.column) + 1));
    }

    record Coordinate(Integer row, Integer column) {

    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day9.txt");
        List<Coordinate> redCoords = new ArrayList<>();
        for (String line : lines) {
            List<Integer> values = ParseUtils.splitByDelimiter(line, ",")
                    .stream().map(Integer::parseInt).toList();
            redCoords.add(new Coordinate(values.get(1), values.get(0)));
        }

        List<Coordinate> area = new ArrayList<>(redCoords);

        for (int i = 0; i < redCoords.size() - 1; i++) {
            Coordinate current = redCoords.get(i);
            Coordinate next = redCoords.get(i + 1);
            fillArea(current, next, area);
        }
        fillArea(redCoords.getLast(), redCoords.getFirst(), area);

        Map<Integer, Set<Coordinate>> rowToCoords = new HashMap<>();
        for (Coordinate coordinate : area) {
            Set<Coordinate> coords = new HashSet<>();
            coords.add(coordinate);
            rowToCoords.merge(coordinate.row, coords, (oldC, newC) -> {
                oldC.addAll(newC);
                return oldC;
            });
        }
        Map<Integer, List<Coordinate>> rowToCoordsSorted = new HashMap<>();
        for (Map.Entry<Integer, Set<Coordinate>> entry : rowToCoords.entrySet()) {
            List<Coordinate> sorted = new ArrayList<>(entry.getValue());
            sorted.sort(Comparator.comparing(Coordinate::column));
            rowToCoordsSorted.put(entry.getKey(), sorted);
        }
        BigInteger max = BigInteger.ZERO;
        for (int i = 0; i < redCoords.size() - 1; i++) {
            Coordinate c1 = redCoords.get(i);
            for (int j = i + 1; j < redCoords.size(); j++) {
                Coordinate c2 = redCoords.get(j);
                boolean withinArea = withinArea(c1, c2, rowToCoordsSorted);
                if (withinArea) {
                    BigInteger square = calculateSquare(c1, c2);
                    if (square.compareTo(max) > 0) {
                        max = square;
                    }
                }
            }
        }
        System.out.println("task2: " + max);
    }

    private static boolean withinArea(Coordinate c1, Coordinate c2, Map<Integer, List<Coordinate>> rowToCoords) {
        int minRow = Math.min(c1.row, c2.row);
        int maxRow = Math.max(c1.row, c2.row);
        int minCol = Math.min(c1.column, c2.column);
        int maxCol = Math.max(c1.column, c2.column);
        for (int row = minRow; row <= maxRow; row++) {
            List<Coordinate> rowValues = rowToCoords.get(row);
            if (minCol < rowValues.getFirst().column || maxCol > rowValues.getLast().column()) {
                return false;
            }
            boolean inArea = false;
            int prevCol = -10;
            int crossings = 0;
            for (int i = 0; i < rowValues.size(); i++) {
                Coordinate cursor = rowValues.get(i);
                if (cursor.column != prevCol + 1) {
                    crossings++;
                }
                prevCol = cursor.column;
                if (inArea && (crossings % 2 == 0)) {
                    return false;
                }
                if (minCol >= cursor.column) {
                    if (crossings % 2 == 0) {
                        return false;
                    }
                    inArea = true;
                }
                if (maxCol >= cursor.column) {
                    break;
                }
            }
        }
        return true;
    }


    private static void fillArea(Coordinate current, Coordinate next, List<Coordinate> area) {
        int rowMove = 0;
        int rowKoef = 1;
        int colMove = 0;
        int colKoef = 1;
        if (!current.row.equals(next.row)) {
            rowMove = 1;
            if (current.row.compareTo(next.row) > 0) {
                rowKoef = -1;
            }
        } else {
            colMove = 1;
            if (current.column.compareTo(next.column) > 0) {
                colKoef = -1;
            }
        }
        Coordinate cursor = current;
        while (!cursor.equals(next)) {
            cursor = new Coordinate(cursor.row + rowMove * rowKoef, cursor.column + colMove * colKoef);
            area.add(cursor);
        }
    }
}
