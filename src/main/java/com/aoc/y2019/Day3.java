package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day3 {
    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day3.txt");
        List<List<Coordinate>> setOfMoves = new ArrayList<>();
        for (String line : lines) {
            List<Coordinate> moves = new ArrayList<>();
            List<String> directions = ParseUtils.splitByDelimiter(line, ",");
            for (String direction : directions) {
                Character dir = direction.charAt(0);
                int rowKoef = 1;
                int colKoef = 1;
                if (dir == 'D') {
                    rowKoef = -1;
                    colKoef = 0;
                } else if (dir == 'U') {
                    rowKoef = 1;
                    colKoef = 0;
                } else if (dir == 'R') {
                    rowKoef = 0;
                    colKoef = 1;
                } else if (dir == 'L') {
                    rowKoef = 0;
                    colKoef = -1;
                }
                Integer value = Integer.parseInt(direction.substring(1));
                Coordinate move = new Coordinate(rowKoef * value, colKoef * value);
                moves.add(move);
            }
            setOfMoves.add(moves);
        }
        Coordinate cursor;
        List<Set<Coordinate>> paths = new ArrayList<>();
        for (List<Coordinate> setOfMove : setOfMoves) {
            cursor = new Coordinate(0, 0);
            Set<Coordinate> path = new LinkedHashSet<>();
            for (Coordinate coordinate : setOfMove) {
                int rowKoef = 1;
                int colKoef = 1;
                if (coordinate.row != 0) {
                    colKoef = 0;
                    if (coordinate.row < 0) {
                        rowKoef = -1;
                    }
                } else {
                    rowKoef = 0;
                    if (coordinate.col < 0) {
                        colKoef = -1;
                    }
                }
                int steps = Math.max(Math.abs(coordinate.row), Math.abs(coordinate.col));
                for (int i = 0; i < steps; i++) {
                    cursor = new Coordinate(cursor.row + rowKoef, cursor.col + colKoef);
                    path.add(cursor);
                }
            }
            paths.add(path);
        }
        Set<Coordinate> path1 = paths.get(0);
        Set<Coordinate> path2 = paths.get(1);
        Set<Coordinate> cross = new HashSet<>();
        for (Coordinate coordinate : path1) {
            if (path2.contains(coordinate)) {
                cross.add(coordinate);
            }
        }
        int min = Integer.MAX_VALUE;
        for (Coordinate coordinate : cross) {
            int dist = Math.abs(coordinate.row) + Math.abs(coordinate.col);
            min = Math.min(dist, min);
        }
        System.out.println("task1: " + min);
    }


    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day3.txt");
        List<List<Coordinate>> setOfMoves = new ArrayList<>();
        for (String line : lines) {
            List<Coordinate> moves = new ArrayList<>();
            List<String> directions = ParseUtils.splitByDelimiter(line, ",");
            for (String direction : directions) {
                Character dir = direction.charAt(0);
                int rowKoef = 1;
                int colKoef = 1;
                if (dir == 'D') {
                    rowKoef = -1;
                    colKoef = 0;
                } else if (dir == 'U') {
                    rowKoef = 1;
                    colKoef = 0;
                } else if (dir == 'R') {
                    rowKoef = 0;
                    colKoef = 1;
                } else if (dir == 'L') {
                    rowKoef = 0;
                    colKoef = -1;
                }
                Integer value = Integer.parseInt(direction.substring(1));
                Coordinate move = new Coordinate(rowKoef * value, colKoef * value);
                moves.add(move);
            }
            setOfMoves.add(moves);
        }
        CoordWithTime cursor;
        List<Set<CoordWithTime>> paths = new ArrayList<>();
        for (List<Coordinate> setOfMove : setOfMoves) {
            cursor = new CoordWithTime(0, 0, 0);
            Set<CoordWithTime> path = new LinkedHashSet<>();
            for (Coordinate coordinate : setOfMove) {
                int rowKoef = 1;
                int colKoef = 1;
                if (coordinate.row != 0) {
                    colKoef = 0;
                    if (coordinate.row < 0) {
                        rowKoef = -1;
                    }
                } else {
                    rowKoef = 0;
                    if (coordinate.col < 0) {
                        colKoef = -1;
                    }
                }
                int steps = Math.max(Math.abs(coordinate.row), Math.abs(coordinate.col));
                for (int i = 0; i < steps; i++) {
                    cursor = new CoordWithTime(cursor.row + rowKoef, cursor.col + colKoef, cursor.time + 1);
                    path.add(cursor);
                }
            }
            paths.add(path);
        }
        Set<CoordWithTime> path1 = paths.get(0);
        Set<CoordWithTime> path2 = paths.get(1);
        Set<List<CoordWithTime>> cross = new HashSet<>();
        Map<CoordWithTime, CoordWithTime> path2Map = new HashMap<>();
        for (CoordWithTime coordWithTime : path2) {
            path2Map.put(coordWithTime, coordWithTime);
        }
        for (CoordWithTime coord1 : path1) {
            if (path2.contains(coord1)) {
                cross.add(List.of(coord1, path2Map.get(coord1)));
            }
        }
        int min = Integer.MAX_VALUE;
        for (
                List<CoordWithTime> pair : cross) {
            int dist = Math.abs(pair.get(0).time) + Math.abs(pair.get(1).time);
            min = Math.min(dist, min);
        }
        System.out.println("task2: " + min);
    }

    private static class CoordWithTime {
        int row, col, time;

        public CoordWithTime(int row, int col, int time) {
            this.row = row;
            this.col = col;
            this.time = time;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoordWithTime that = (CoordWithTime) o;
            return row == that.row && col == that.col;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            return result;
        }
    }


}

