package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Day12 {

    static List<Coord> moves = List.of(
            new Coord(0, 1),
            new Coord(0, -1),
            new Coord(1, 0),
            new Coord(-1, 0)
    );

    static List<Coord> extendedMoves = List.of(
            new Coord(0, 1),
            new Coord(0, -1),
            new Coord(1, 0),
            new Coord(-1, 0),
            new Coord(-1, -1),
            new Coord(-1, 1),
            new Coord(1, -1),
            new Coord(1, 1)
    );

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1And2();
    }


    public static void task1And2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day12.txt");
        Map<Coord, Character> plots = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                plots.put(new Coord(row, col), c);
            }
        }
        List<Group> groups = getGroups(plots, lines.size(), lines.get(0).length());

        long total = 0;
        for (Group group : groups) {
            long perim = calculatePerimeter(group);
            total = total + perim * group.coords.size();
        }
        System.out.println(total);


        total = 0;
        for (Group group : groups) {
            long perim = calculatePerimeterTask2(group);
            total = total + perim * group.coords.size();
        }
        System.out.println(total);

    }

    private static long calculatePerimeter(Group group) {
        long total = group.coords.size() * 4;
        List<Coord> coords = group.coords;
        for (int i = 0; i < coords.size() - 1; i++) {
            Coord coord1 = coords.get(i);
            for (int j = i + 1; j < coords.size(); j++) {
                Coord coord2 = coords.get(j);
                if (coord1.row == coord2.row && Math.abs(coord1.col - coord2.col) == 1) {
                    total = total - 2;
                }
                if (coord1.col == coord2.col && Math.abs(coord1.row - coord2.row) == 1) {
                    total = total - 2;
                }
            }
        }
        return total;
    }


    private static List<Group> getGroups(Map<Coord, Character> plots, int rows, int cols) {
        List<Group> groups = new ArrayList<>();
        Set<Coord> visited = new HashSet<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                var coord = new Coord(row, col);
                if (!visited.contains(coord)) {
                    visited.add(coord);
                    List<Coord> groupCoords = new ArrayList<>();
                    groupCoords.add(coord);
                    Group currentGroup = new Group(plots.get(coord), groupCoords);
                    groups.add(currentGroup);
                    getNeibs(coord, plots, currentGroup, visited);
                }
            }
        }
        return groups;
    }

    private static void getNeibs(Coord coord, Map<Coord, Character> plots, Group group, Set<Coord> visited) {
        for (Coord move : moves) {
            var nextCoord = new Coord(coord.row + move.row, coord.col + move.col);
            if (!visited.contains(nextCoord) && plots.get(nextCoord) == group.plot) {
                visited.add(nextCoord);
                group.coords.add(nextCoord);
                getNeibs(nextCoord, plots, group, visited);
            }
        }
    }

    private static long calculatePerimeterTask2(Group group) {
        var zoomedIn = zoomIn(group.coords);
        Set<Coord> usedInPerimeter = new HashSet<>();
        for (Coord coord : zoomedIn) {
            for (Coord move : extendedMoves) {
                var neib = new Coord(coord.row + move.row, coord.col + move.col);
                if (!zoomedIn.contains(neib)) {
                    usedInPerimeter.add(coord);
                    break;
                }
            }
        }
        int size = traverse(new ArrayList<>(usedInPerimeter));
        return size;

    }

    private static void print(Collection<Coord> zoomed) {
        for (int row = -5; row < 20; row++) {
            for (int col = -5; col < 20; col++) {
                if (zoomed.contains(new Coord(row, col))) {
                    System.out.print("X");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int traverse(List<Coord> coords) {
        var accum = new Accum(0);
        Set<Coord> visited = new HashSet<>();
        for (Coord coord : coords) {
            if (!visited.contains(coord)) {
                traverseRec(coords, coord, null, visited, accum);
            }
        }
        return accum.value;

    }


    private static List<Coord> zoomIn(List<Coord> coords) {
        List<Coord> zoom = new ArrayList<>();
        for (Coord coord : coords) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    zoom.add(new Coord(coord.row * 3 + i, coord.col * 3 + j));
                }
            }
        }

        return zoom;
    }


    private static void traverseRec(List<Coord> usedInPrimeter, Coord cursor, Direction
            dir, Set<Coord> visited, Accum accum) {
        Direction nextDir = dir;
        if (dir == null) {
            for (Direction dirInt : Direction.values()) {
                var next = new Coord(cursor.row + dirInt.getShift().row, cursor.col + dirInt.getShift().col);
                if (usedInPrimeter.contains(next) && !visited.contains(next)) {
                    var inverted = new Coord(cursor.row + dirInt.invert().getShift().row, cursor.col
                            + dirInt.invert().getShift().col);
                    if (usedInPrimeter.contains(inverted)) {
                        accum.value = accum.value - 1;
                    }
                    break;
                }
            }
        }
        if (nextDir == null || !usedInPrimeter.contains(new Coord(cursor.row + dir.getShift().row, cursor.col + dir.getShift().col))
                || visited.contains(new Coord(cursor.row + dir.getShift().row, cursor.col + dir.getShift().col))) {
            for (Direction dirInt : Direction.values()) {
                var next = new Coord(cursor.row + dirInt.getShift().row, cursor.col + dirInt.getShift().col);
                if (usedInPrimeter.contains(next) && !visited.contains(next)) {
                    nextDir = dirInt;
                    break;
                }
            }
        }
        if (nextDir == null) {
            return;
        }
        if (nextDir != dir) {
            accum.value = accum.value + 1;
        }
        var nextCursor = new Coord(cursor.row + nextDir.getShift().row, cursor.col + nextDir.getShift().col);
        if (usedInPrimeter.contains(nextCursor) && !visited.contains(nextCursor)) {
            visited.add(nextCursor);
            traverseRec(usedInPrimeter, nextCursor, nextDir, visited, accum);
        }
    }


    @Data
    @AllArgsConstructor
    static class Coord {
        int row;
        int col;
    }

    @AllArgsConstructor
    @Data
    static class Group {
        Character plot;
        List<Coord> coords;
    }


    @Data
    @AllArgsConstructor
    static class Accum {
        int value = 0;
    }

    public enum Direction {
        RIGHT(new Coord(0, 1)), LEFT(new Coord(0, -1)), DOWN(new Coord(1, 0)), UP(new Coord(-1, 0));

        public Coord getShift() {
            return shift;
        }

        public Coord shift;

        private Direction(Coord coord) {
            this.shift = coord;
        }

        public Direction invert() {
            if (this == RIGHT) {
                return LEFT;
            }
            if (this == LEFT) {
                return RIGHT;
            }
            if (this == UP) {
                return DOWN;
            }
            if (this == DOWN) {
                return UP;
            }
            return null;
        }

    }
}
