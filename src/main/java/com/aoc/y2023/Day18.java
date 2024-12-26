package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day18 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day18_t.txt");
        Map<Coordinate, Character> lava = new HashMap<>();
        Coordinate cursor = new Coordinate(0, 0);
        int maxRight = Integer.MIN_VALUE;
        int minLeft = Integer.MAX_VALUE;
        int minUp = Integer.MAX_VALUE;
        int maxDown = Integer.MIN_VALUE;
        lava.put(cursor, '#');
        for (String line : lines) {
            List<String> directions = ParseUtils.splitByDelimiter(line, " ");
            Direction dir = Direction.ofString((directions.get(0).charAt(0)));
            int steps = Integer.parseInt(directions.get(1));
            for (int i = 0; i < steps; i++) {
                cursor = new Coordinate(cursor.row + dir.shift.row, cursor.col + dir.shift.col);
                lava.put(cursor, '#');
            }
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

        Map<Coordinate, Character> lavaInside = flood(lava, new Coordinate(minUp, minLeft), new Coordinate(maxDown, maxRight));
        System.out.println(lavaInside.size() + lava.size());

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

    private static boolean insideLava(Coordinate cursor, Set<Coordinate> visited, Map<Coordinate, Character> lava,
                                      Coordinate topLeft, Coordinate bottomRight) {
        if (lava.containsKey(cursor)) {
            return true;
        }
        if (visited.contains(cursor)) {
            return true;
        }
        if (cursor.row < topLeft.row || cursor.row > bottomRight.row ||
                cursor.col < topLeft.col || cursor.col > bottomRight.col) {
            return false;
        }
        visited.add(cursor);
        var left = new Coordinate(cursor.row + Direction.LEFT.shift.row, cursor.col + Direction.LEFT.shift.col);
        boolean leftInside = insideLava(left, visited, lava, topLeft, bottomRight);
        var right = new Coordinate(cursor.row + Direction.RIGHT.shift.row, cursor.col + Direction.RIGHT.shift.col);
        boolean rightInside = insideLava(right, visited, lava, topLeft, bottomRight);
        var down = new Coordinate(cursor.row + Direction.DOWN.shift.row, cursor.col + Direction.DOWN.shift.col);
        boolean downInside = insideLava(down, visited, lava, topLeft, bottomRight);
        var up = new Coordinate(cursor.row + Direction.UP.shift.row, cursor.col + Direction.UP.shift.col);
        boolean upInside = insideLava(up, visited, lava, topLeft, bottomRight);
        return leftInside && rightInside && downInside && upInside;

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
}
