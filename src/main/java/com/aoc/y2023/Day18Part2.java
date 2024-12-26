package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day18Part2 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day18.txt");
        List<Coordinate> lava = new ArrayList<>();
        BigInteger contur = BigInteger.ZERO;
        Coordinate cursor = new Coordinate(0, 0);
        int maxRight = Integer.MIN_VALUE;
        int minLeft = Integer.MAX_VALUE;
        int minUp = Integer.MAX_VALUE;
        int maxDown = Integer.MIN_VALUE;
        BigInteger interSect = BigInteger.ZERO;
        //lava.add(cursor);
        for (String line : lines) {
            List<String> directions = ParseUtils.splitByDelimiter(line, " ");
            //Direction dir = Direction.ofString((directions.get(0).charAt(0)));
            //int steps = Integer.parseInt(directions.get(1));

            String stepsString = directions.get(2);
            int steps = (int) Long.parseLong(stepsString.substring(2, stepsString.length() - 2), 16);
            int intDir = Integer.parseInt(stepsString.substring(stepsString.length() - 2, stepsString.length() - 1));
            Direction dir;
            if (intDir == 0) {
                dir = Direction.RIGHT;
            } else if (intDir == 1) {
                dir = Direction.DOWN;
            } else if (intDir == 2) {
                dir = Direction.LEFT;
            } else {
                dir = Direction.UP;
            }


            cursor = new Coordinate(cursor.row + steps * dir.shift.row, cursor.col + steps * dir.shift.col);
            contur = contur.add(BigInteger.valueOf(steps));

            if (dir == Direction.LEFT || dir == Direction.UP) {
                interSect = interSect.add(BigInteger.valueOf(steps));
            }

            lava.add(cursor);
            if (cursor.col > maxRight) {
                maxRight = cursor.col;
            }
            if (cursor.col < minLeft) {
                minLeft = cursor.col;
            }
            if (cursor.row > maxDown) {
                maxDown = cursor.row;
            }
            if (cursor.row < minUp) {
                minUp = cursor.row;
            }
        }
        //lava.remove(lava.size() - 1);

        BigInteger sumTotal = BigInteger.ZERO;
        for (int i = 0; i < lava.size() - 1; i++) {
            Coordinate current = lava.get(i);
            Coordinate next = lava.get(i + 1);
            sumTotal = sumTotal.add(BigInteger.valueOf(current.col).multiply(BigInteger.valueOf(next.row)));
            sumTotal = sumTotal.subtract(BigInteger.valueOf(next.col).multiply(BigInteger.valueOf(current.row)));
            // sumTotal = sumTotal.add(BigInteger.ONE);
        }
        sumTotal = sumTotal.add(BigInteger.valueOf(lava.get(lava.size() - 1).col).multiply(BigInteger.valueOf(lava.get(0).row)));
        sumTotal = sumTotal.subtract(BigInteger.valueOf(lava.get(0).col).multiply(BigInteger.valueOf(lava.get(lava.size() - 1).row)));
        //sumTotal = sumTotal.add(BigInteger.ONE);
        sumTotal = sumTotal.divide(BigInteger.TWO);

//        BigInteger sumSubtract = BigInteger.ZERO;
//        for (int i = 0; i < lava.size() - 1; i++) {
//            Coordinate current = lava.get(i);
//            Coordinate next = lava.get(i + 1);
//            sumSubtract = sumSubtract.add(BigInteger.valueOf(next.col).multiply(BigInteger.valueOf(current.row)));
//        }
//        sumSubtract = sumSubtract.add(BigInteger.valueOf(lava.get(0).col).multiply(BigInteger.valueOf(lava.get(lava.size() - 1).row)));
//        sumSubtract = sumSubtract.divide(BigInteger.TWO);
//
//       sumTotal = sumTotal.subtract(sumSubtract);
        System.out.println(sumTotal.add(contur).subtract(interSect).add(BigInteger.ONE));

        //Map<Coordinate, Character> lavaInside = flood(lava, new Coordinate(minUp, minLeft), new Coordinate(maxDown, maxRight));
        //System.out.println(lavaInside.size() + lava.size());


    }

    private static Map<Coordinate, Character> flood(Map<Coordinate, Character> lava, Coordinate topLeft, Coordinate bottomRight) {
        Set<Coordinate> allVisited = new HashSet<>();
        Map<Coordinate, Character> lavaInside = new HashMap<>();
        for (int row = topLeft.row; row <= bottomRight.row; row++) {
            for (int col = topLeft.col; col <= bottomRight.col; col++) {
                var coord = new Coordinate(row, col);
                if (!allVisited.contains(coord) && !lava.containsKey(coord)) {
                    Set<Coordinate> visited = new HashSet<>();
                    boolean res = insideLavaV2(coord, visited, lava, topLeft, bottomRight);
                    System.out.println("iteration completed");
                    if (res) {
                        for (Coordinate coordinate : visited) {
                            lavaInside.put(coordinate, '#');
                        }
                    }
                    allVisited.addAll(visited);
                }
            }
        }
        return lavaInside;

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

        public static Direction ofString(Character value) {
            if (value == 'R') {
                return RIGHT;
            } else if (value == 'L') {
                return LEFT;
            } else if (value == 'U') {
                return UP;
            } else {
                return DOWN;
            }
        }

    }

    private static boolean insideLavaV2(Coordinate start, Set<Coordinate> visited, Map<Coordinate, Character> lava,
                                        Coordinate topLeft, Coordinate bottomRight) {
        List<Coordinate> cursors = new ArrayList<>();
        cursors.add(start);
        while (!cursors.isEmpty()) {
            List<Coordinate> nextCursors = new ArrayList<>();
            for (Coordinate coord : cursors) {
                if (!visited.contains(coord)) {
                    if (coord.row < topLeft.row || coord.row > bottomRight.row ||
                            coord.col < topLeft.col || coord.col > bottomRight.col) {
                        return false;
                    }
                    if (lava.containsKey(coord)) {
                        continue;
                    }
                    visited.add(coord);
                    var left = new Coordinate(coord.row + Direction.LEFT.shift.row, coord.col + Direction.LEFT.shift.col);
                    nextCursors.add(left);
                    var right = new Coordinate(coord.row + Direction.RIGHT.shift.row, coord.col + Direction.RIGHT.shift.col);
                    nextCursors.add(right);
                    var down = new Coordinate(coord.row + Direction.DOWN.shift.row, coord.col + Direction.DOWN.shift.col);
                    nextCursors.add(down);
                    var up = new Coordinate(coord.row + Direction.UP.shift.row, coord.col + Direction.UP.shift.col);
                    nextCursors.add(up);
                }
            }
            cursors = nextCursors;

        }
        return true;
    }

    @AllArgsConstructor
    private static class CoordInfo {
        private Coordinate coord;
        private Direction dir;
    }
}
