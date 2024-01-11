package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day21 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
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

        for (int cycle = 0; cycle < 26501365; cycle++) {
            List<Coordinate> nextCursors = new ArrayList<>();
            Set<Coordinate> cycleTaken = new HashSet<>();
            for (Coordinate cursor : cursors) {
                addCoord(cursor, Direction.LEFT, lines, coords, cycleTaken, nextCursors);
                addCoord(cursor, Direction.RIGHT, lines, coords, cycleTaken, nextCursors);
                addCoord(cursor, Direction.UP, lines, coords, cycleTaken, nextCursors);
                addCoord(cursor, Direction.DOWN, lines, coords, cycleTaken, nextCursors);


            }
            cursors = nextCursors;
            print(new HashSet<>(cursors), lines);
        }

        System.out.println(cursors.size());

    }

    private static void addCoord(Coordinate cursor, Direction right, List<String> lines, Map<Coordinate, Character> coords, Set<Coordinate> cycleTaken, List<Coordinate> nextCursors) {
        var character = new Coordinate(cursor.row + right.shift.row, cursor.col + right.shift.col);
        if (character.row < 0 || character.row >= lines.size() ||
                character.col < 0 || character.col >= lines.get(0).length()) {
            return;
        }
        if (coords.get(character) != '#' && !cycleTaken.contains(character)) {
            nextCursors.add(character);
            cycleTaken.add(character);
        }
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

    private static void print(Set<Coordinate> coords, List<String> lines) {
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                if (coords.contains(new Coordinate(row, col))) {
                    System.out.print("0");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
