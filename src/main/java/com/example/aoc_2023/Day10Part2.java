package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.CoordEdge;
import com.example.aoc_2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day10Part2 {

    public static void main(String[] args) {
        task2();
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("day10_u.txt");

        Set<Coordinate> path = Day10.task1();
        Map<Coordinate, Character> coords = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                parseChar(coords, row, c, col, path);
            }
        }

        printNew(coords, lines);
        int counter = 0;
        for (Map.Entry<Coordinate, Character> entry : coords.entrySet()) {
            if (entry.getValue() == 'X') {
                counter++;
            }
        }
        System.out.println("total = " + counter);


        Set<Coordinate> totalVisited = new HashSet<>();
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(0).length(); col++) {
                Coordinate start = new Coordinate(3 * row, 3 * col);
                if (coords.get(new Coordinate(start.row + 1, start.col + 1)) == 'X') {
                    if (!totalVisited.contains(start)) {
                        Set<Coordinate> visited = new HashSet<>();
                        flood(coords, start, visited);
                        counter = 0;
                        List<Coordinate> xCoords = new ArrayList<>();
                        for (Coordinate coordinate : visited) {
                            if (coords.get(coordinate) == 'X') {
                                counter++;
                                xCoords.add(coordinate);
                            }
                        }
                        totalVisited.addAll(visited);
                        System.out.println(counter);
                    }

                }
            }
        }
        Set<Coordinate> visited = new HashSet<>();
       // flood(coords, new Coordinate(82, 292), visited);
        System.out.println(visited.size());

    }


    private static void flood(Map<Coordinate, Character> coords, Coordinate cursor, Set<Coordinate> visited) {
        List<Coordinate> cursors = new ArrayList<>();
        cursors.add(cursor);
        while (!cursors.isEmpty()) {List<Coordinate> nextCursors = new ArrayList<>();
            for (Coordinate coordinate : cursors) {
                var p1 = navigate(coords, coordinate, visited, new Coordinate(1, 0));
                if (p1 != null) {
                    nextCursors.add(p1);
                }
                var p2 = navigate(coords, coordinate, visited, new Coordinate(-1, 0));
                if (p2 != null) {
                    nextCursors.add(p2);
                }
                var p3 = navigate(coords, coordinate, visited, new Coordinate(0, 1));
                if (p3 != null) {
                    nextCursors.add(p3);
                }
                var p4 = navigate(coords, coordinate, visited, new Coordinate(0, -1));
                if (p4 != null) {
                    nextCursors.add(p4);
                }
            }
            cursors = nextCursors;
        }
    }

    private static Coordinate navigate(Map<Coordinate, Character> coords, Coordinate cursor, Set<Coordinate> visited, Coordinate shift) {
        var next = new Coordinate(cursor.row + shift.row, cursor.col + shift.col);
        var c = coords.getOrDefault(next, '?');
        if (!visited.contains(next) && ((c == 'X' || c == '.'))) {
            visited.add(next);
            //flood(coords, next, visited);
            return next;
        }
        return null;
    }

    private static void parseChar(Map<Coordinate, Character> coords, int row, char c, int col, Set<Coordinate> path) {
        int actualRow = row * 3;
        int actualCol = col * 3;
        if (!path.contains(new Coordinate(row, col))) {
            c = '.';
        }
        if (c == '.') {
            coords.put(new Coordinate(actualRow, actualCol), '.');
            coords.put(new Coordinate(actualRow, actualCol + 1), '.');
            coords.put(new Coordinate(actualRow, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol + 1), 'X');
            coords.put(new Coordinate(actualRow + 1, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 1), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 2), '.');
        } else if (c == '|') {
            coords.put(new Coordinate(actualRow, actualCol), '.');
            coords.put(new Coordinate(actualRow, actualCol + 1), '|');
            coords.put(new Coordinate(actualRow, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol + 1), '|');
            coords.put(new Coordinate(actualRow + 1, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 1), '|');
            coords.put(new Coordinate(actualRow + 2, actualCol + 2), '.');
        } else if (c == '7') {
            coords.put(new Coordinate(actualRow, actualCol), '.');
            coords.put(new Coordinate(actualRow, actualCol + 1), '.');
            coords.put(new Coordinate(actualRow, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol), '-');
            coords.put(new Coordinate(actualRow + 1, actualCol + 1), '-');
            coords.put(new Coordinate(actualRow + 1, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 1), '|');
            coords.put(new Coordinate(actualRow + 2, actualCol + 2), '.');
        } else if (c == 'F') {
            coords.put(new Coordinate(actualRow, actualCol), '.');
            coords.put(new Coordinate(actualRow, actualCol + 1), '.');
            coords.put(new Coordinate(actualRow, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol + 1), '-');
            coords.put(new Coordinate(actualRow + 1, actualCol + 2), '-');
            coords.put(new Coordinate(actualRow + 2, actualCol), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 1), '|');
            coords.put(new Coordinate(actualRow + 2, actualCol + 2), '.');
        } else if (c == 'J') {
            coords.put(new Coordinate(actualRow, actualCol), '.');
            coords.put(new Coordinate(actualRow, actualCol + 1), '|');
            coords.put(new Coordinate(actualRow, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol), '-');
            coords.put(new Coordinate(actualRow + 1, actualCol + 1), '-');
            coords.put(new Coordinate(actualRow + 1, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 1), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 2), '.');
        } else if (c == 'L') {
            coords.put(new Coordinate(actualRow, actualCol), '.');
            coords.put(new Coordinate(actualRow, actualCol + 1), '|');
            coords.put(new Coordinate(actualRow, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol + 1), '-');
            coords.put(new Coordinate(actualRow + 1, actualCol + 2), '-');
            coords.put(new Coordinate(actualRow + 2, actualCol), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 1), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 2), '.');
        } else if (c == '-') {
            coords.put(new Coordinate(actualRow, actualCol), '.');
            coords.put(new Coordinate(actualRow, actualCol + 1), '.');
            coords.put(new Coordinate(actualRow, actualCol + 2), '.');
            coords.put(new Coordinate(actualRow + 1, actualCol), '-');
            coords.put(new Coordinate(actualRow + 1, actualCol + 1), '-');
            coords.put(new Coordinate(actualRow + 1, actualCol + 2), '-');
            coords.put(new Coordinate(actualRow + 2, actualCol), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 1), '.');
            coords.put(new Coordinate(actualRow + 2, actualCol + 2), '.');
        }
    }

    private static void printNew(Map<Coordinate, Character> coords, List<String> lines) {
        for (int row = 0; row < lines.size() * 3; row++) {
            for (int col = 0; col < lines.get(0).length() * 3; col++) {
                System.out.print(coords.get(new Coordinate(row, col)));
            }
            System.out.println();
        }
    }


    public static List<Coordinate> dfs(Coordinate start, Map<Coordinate, List<CoordEdge>> fromEdges) {
        Set<Coordinate> visited = new HashSet<>();
        visited.add(start);

        Coordinate cursor = start;
        List<Coordinate> path = new ArrayList<>();
        path.add(start);
        cursor = fromEdges.get(cursor).get(0).to;
        path.add(cursor);
        Coordinate previous = start;
        while (!cursor.equals(start)) {
            if (previous.equals(fromEdges.get(cursor).get(0).to)) {
                previous = cursor;
                cursor = fromEdges.get(cursor).get(1).to;
            } else {
                previous = cursor;
                cursor = fromEdges.get(cursor).get(0).to;
            }
            path.add(cursor);
        }
        return path;
    }


}
