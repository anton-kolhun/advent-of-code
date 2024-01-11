package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.Coordinate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class Day21Part2 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("day21.txt");
        Map<Coordinate, Character> coords = new HashMap();
        Coordinate start = null;
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.toCharArray().length; col++) {
                char c = line.charAt(col);
                coords.put(new Coordinate(row, col), c);
                if (c == 'S') {
                    start = new Coordinate(row, col);
                }
            }
        }

        List<Coordinate> cursors = new ArrayList<>();
        cursors.add(start);

        Set<CoordData> unique = new HashSet<>();
        Set<Set<Coordinate>> uniqueCoords = new HashSet<>();
        Map<Integer, Integer> rowCounters = new HashMap<>();
        Map<Integer, Integer> cycleToNumb = new HashMap<>();
        for (int cycle = 0; cycle < 300; cycle++) {
            System.out.println();
            List<Coordinate> nextCursors = new ArrayList<>();
            Set<Coordinate> cycleTaken = new HashSet<>();
    /*        if (!addUniqueCoords(uniqueCoords, cursors, lines, cycle)) {
                addUnique(unique, cursors, lines, cycle);
            }*/
            for (Coordinate cursor : cursors) {
                addCoord(cursor, Direction.LEFT, lines, coords, cycleTaken, nextCursors);
                addCoord(cursor, Direction.RIGHT, lines, coords, cycleTaken, nextCursors);
                addCoord(cursor, Direction.UP, lines, coords, cycleTaken, nextCursors);
                addCoord(cursor, Direction.DOWN, lines, coords, cycleTaken, nextCursors);


            }
            int prevCursSize = cursors.size();
            cursors = nextCursors;
            //System.out.println(cursors.size() - prevCursSize);
            //print(new HashSet<>(cursors), lines, cycle);
            //rowCounters = calcRows(new HashSet<>(cursors), lines, rowCounters);
            printOriginal(cursors, lines, cycle);
            cycleToNumb.put(cycle, cursors.size());
        }

        // for (int cycleNumb = 150; cycleNumb < 500; cycleNumb++) {
//        int cycleStart = 300;
//        int initialStartVal = cycleToNumb.get(cycleStart);
//        boolean shouldFind = true;
//        int stepSize = 1;
//        while (shouldFind) {
//            int currentCycleVal = cycleToNumb.get(cycleStart + stepSize);
//            int currentDelta = currentCycleVal - initialStartVal;
//            int nextCycleVal = cycleToNumb.get(cycleStart + 2 * stepSize);
//            int nextDelta = nextCycleVal - currentCycleVal;
//            int superDelta = nextDelta - currentDelta;
//            currentCycleVal = nextCycleVal;
//            currentDelta = nextDelta;
//            shouldFind = false;
//            for (int j = 3; j <= 7; j++) {
//                nextCycleVal = cycleToNumb.get(cycleStart + j * stepSize);
//                nextDelta = nextCycleVal - currentCycleVal;
//                if (nextDelta != currentDelta + superDelta) {
//                    shouldFind = true;
//                    break;
//                }
//                currentCycleVal = nextCycleVal;
//                currentDelta = nextDelta;
//            }
//            if (!shouldFind) {
//                System.out.println("match!");
//                break;
//            }
//            stepSize++;
//        }
//        System.out.println(stepSize);
//        System.out.println(cursors.size());

//        “pseudo” cycle solution.
//        say, f(iteration) = numer_of_matches
//        Considering provided sample and cycle_length = 11
//        f(55) = 1914
//        f(66) = 2794 |  2794 - 1914 =  880
//        f(77) = 3836 | 3836 - 2794 = 1042 = 880 + 162
//        f(88) = 5040 | 5040 - 3836 = 1204 = 1042 + 162...
//...
//        then f(a*n) = f(a) + n * delta_1  + (1 + n-1)/2 * (n-1) * delta_2
//                (delta_1 = 880, delta_2 = 162 in the sample above)
//        E.g., the formula for  500 iterations:
//        500 = 71 + 39 * 11
//        f(39* 71)= 3282 + 39 * 1120 + (1 + 38)/2 * 38 * 162 = 167004


        // 26501365 = 202299 * 131 + 196
        //34786 + 202299 * 61649 + (1 + 202298)/2 * 202298 * 30774 = 629720570456311
        BigDecimal res = BigDecimal.valueOf(cycleToNumb.get(195)).add(BigDecimal.valueOf(202299)
                .multiply(BigDecimal.valueOf(61649))
                .add(BigDecimal.valueOf(202299).divide(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(202298)).multiply(BigDecimal.valueOf(30774))));
        System.out.println(res);


    }

    private static void printOriginal(List<Coordinate> coords, List<String> lines, int cycle) {
        System.out.println(coords.size());
        System.out.println("cycle = " + cycle);
        int minRow = Integer.MAX_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int maxCol = Integer.MIN_VALUE;
        for (Coordinate coord : coords) {
            if (coord.row < minRow) {
                minRow = coord.row;
            }
            if (coord.col < minCol) {
                minCol = coord.col;
            }
            if (coord.row > maxRow) {
                maxRow = coord.row;
            }
            if (coord.col > maxCol) {
                maxCol = coord.col;
            }
        }

//        for (int row = minRow; row <= maxRow; row++) {
//            for (int col = minCol; col <= maxCol; col++) {
//                if (coords.contains(new Coordinate(row, col))) {
//                    System.out.print("0");
//                } else {
//                    System.out.print(".");
//                }
//            }
//            System.out.println();
//        }
        System.out.println();
    }

    private static void addUnique(Set<CoordData> unique, List<Coordinate> coords, List<String> lines, int cycle) {
        CoordData data = new CoordData();
        for (Coordinate coord : coords) {
            var updated = transform(lines, coord);
            data.coordToOccur.merge(updated, 1, Integer::sum);
        }
    }

    private static boolean addUniqueCoords(Set<Set<Coordinate>> unique, List<Coordinate> coords, List<String> lines, int cycle) {
        Set<Coordinate> transformedCoords = new HashSet<>();
        for (Coordinate coord : coords) {
            var updated = transform(lines, coord);
            transformedCoords.add(updated);
        }
        if (!unique.add(transformedCoords)) {
            System.out.println("cycle for coords  " + cycle);
            System.out.println(coords.size());
            return false;
        }
        return true;
    }

    private static void addCoord(Coordinate cursor, Direction right, List<String> lines, Map<Coordinate, Character> coords, Set<Coordinate> cycleTaken, List<Coordinate> nextCursors) {
        var coord = new Coordinate(cursor.row + right.shift.row, cursor.col + right.shift.col);
        var transformed = transform(lines, coord);
        if (coords.get(transformed) != '#' && !cycleTaken.contains(coord)) {
            nextCursors.add(coord);
            cycleTaken.add(coord);
        }
    }

    private static Coordinate transform(List<String> lines, Coordinate coord) {
        var updated = new Coordinate(coord.row, coord.col);
        if (coord.row < 0) {
            int actualRow = Math.abs(coord.row % (lines.size()));
            if (actualRow == 0) {
                updated.row = 0;
            } else {
                updated.row = lines.size() - actualRow;
            }
        } else if (coord.row >= lines.size()) {
            updated.row = Math.abs(coord.row % lines.size());
        }
        if (coord.col < 0) {
            int actualCol = Math.abs(coord.col % (lines.get(0).length()));
            if (actualCol == 0) {
                updated.col = 0;
            } else {
                updated.col = lines.get(0).length() - actualCol;
            }
        } else if (coord.col >= lines.get(0).length()) {
            updated.col = Math.abs(coord.col % lines.get(0).length());
        }
        return updated;
    }

    private static enum Direction {
        RIGHT(new Coordinate(0, 1)), LEFT(new Coordinate(0, -1)), DOWN(new Coordinate(1, 0)), UP(new Coordinate(-1, 0));

        public Coordinate getShift() {
            return shift;
        }

        private Coordinate shift;

        private Direction(Coordinate coord) {
            this.shift = coord;
        }

    }

    private static void print(Set<Coordinate> coords, List<String> lines, int cycle) {
        System.out.println("cycle = " + cycle);
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            int rowCounter = 0;
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                boolean isNull = false;
                for (Coordinate coord : coords) {
                    var transformed = transform(lines, new Coordinate(coord.row, coord.col));
                    if (transformed.equals(new Coordinate(row, col))) {
                        isNull = true;
                        break;
                    }
                }
                if (isNull) {
                    System.out.print("0");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }

        System.out.println(coords.size());
    }

    private static Map<Integer, Integer> calcRows(Set<Coordinate> coords, List<String> lines, Map<Integer, Integer> previousRows) {
        //System.out.println();
        Map<Integer, Integer> rowCounters = new TreeMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                boolean isNull = false;
                for (Coordinate coord : coords) {
                    var transformed = transform(lines, new Coordinate(coord.row, coord.col));
                    if (transformed.equals(new Coordinate(row, col))) {
                        rowCounters.merge(row, 1, Integer::sum);
                    }
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : rowCounters.entrySet()) {
            System.out.print(entry.getValue() - previousRows.getOrDefault(entry.getKey(), 0) + " ");
        }
        System.out.println();

        return rowCounters;
    }


    private static class CoordData {
        Map<Coordinate, Integer> coordToOccur = new HashMap<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoordData coordData = (CoordData) o;

            return Objects.equals(coordToOccur, coordData.coordToOccur);
        }

        @Override
        public int hashCode() {
            return coordToOccur != null ? coordToOccur.hashCode() : 0;
        }
    }


}