package com.example.aoc_2024;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Day14 {

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
        task1();
//        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day14.txt");
        Map<Coord, List<Robot>> robots = new LinkedHashMap<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " ");
            List<String> initialCoords = ParseUtils.splitByDelimiter(parts.get(0), "=");
            List<Integer> coords = ParseUtils.splitByDelimiter(initialCoords.get(1), ",")
                    .stream()
                    .map(Integer::parseInt)
                    .toList();

            List<String> paceString = ParseUtils.splitByDelimiter(parts.get(1), "=");
            List<Integer> pace = ParseUtils.splitByDelimiter(paceString.get(1), ",")
                    .stream()
                    .map(Integer::parseInt)
                    .toList();

            List<Robot> totalRobots = new ArrayList<>();
            totalRobots.add(new Robot(pace.get(0), pace.get(1)));
            robots.merge(new Coord(coords.get(0), coords.get(1)), totalRobots, (robots1, robots2) -> {
                robots1.addAll(robots2);
                return robots1;
            });
        }


        int seconds = 10000;
//        int seconds = 100; for part 1
        int rowSize = 103;
        int colSize = 101;
        for (int second = 0; second < seconds; second++) {
            Map<Coord, List<Robot>> nextRobots = new LinkedHashMap<>();
            for (Map.Entry<Coord, List<Robot>> entry : robots.entrySet()) {
                for (Robot robot : entry.getValue()) {
                    Coord current = entry.getKey();
                    int col = move(current.col, robot.colPace, colSize);
                    int row = move(current.row, robot.rowPace, rowSize);
                    Coord next = new Coord(col, row);
                    List<Robot> totalRobots = new ArrayList<>();
                    totalRobots.add(robot);
                    nextRobots.merge(next, totalRobots, (robots1, robots2) -> {
                        robots1.addAll(robots2);
                        return robots1;
                    });
                }

            }
            isTree(nextRobots, rowSize, colSize, second);
            //print2(nextRobots, rowSize, colSize, second);
            robots = nextRobots;
        }

        int rowMiddle = rowSize / 2;
        int colMiddle = colSize / 2;
        List<Square> squares = List.of(
                new Square(new Coord(0, 0), new Coord(colMiddle - 1, rowMiddle - 1)),
                new Square(new Coord(0, rowMiddle + 1), new Coord(colMiddle - 1, rowSize - 1)),
                new Square(new Coord(colMiddle + 1, 0), new Coord(colSize - 1, rowMiddle - 1)),
                new Square(new Coord(colMiddle + 1, rowMiddle + 1), new Coord(colSize - 1, rowSize - 1))
        );

        Map<Square, Integer> squareToSize = new HashMap<>();
        for (Map.Entry<Coord, List<Robot>> entry : robots.entrySet()) {
            for (Square square : squares) {
                if (square.match(entry.getKey())) {
                    squareToSize.merge(square, entry.getValue().size(), Integer::sum);
                }
            }

        }
        long res = squareToSize.values().stream().reduce((v1, v2) -> v1 * v2).get();
        System.out.println(res);
    }


    private static boolean isTree(Map<Coord, List<Robot>> nextRobots, int rowSize, int colSize, int second) {
        Set<Coord> totalVisited = new HashSet<>();
        for (Map.Entry<Coord, List<Robot>> entry : nextRobots.entrySet()) {
            Set<Coord> visited = new HashSet<>();
            if (!totalVisited.contains(entry.getKey())) {
                navigate(entry.getKey(), rowSize, colSize, visited, nextRobots);
                totalVisited.addAll(visited);
                if (visited.size() > 100) {
                    System.out.println("is it a tree?");
                    print2(nextRobots, rowSize, colSize, second + 1);
                    return true;
                }
            }
        }

        return false;
    }


    private static void navigate(Coord coord, int rowSize, int colSize, Set<Coord> visited,
                                 Map<Coord, List<Robot>> robots) {
        Set<Coord> toVisit = new LinkedHashSet<>();
        toVisit.add(coord);
        while (!toVisit.isEmpty()) {
            var iter = toVisit.iterator();
            var current = iter.next();
            iter.remove();
            visited.add(current);
            for (Coord move : extendedMoves) {
                var next = new Coord(current.col + move.col, current.row + move.row);
                if (next.row >= 0 && next.row < rowSize
                        && next.col >= 0 && next.col < colSize &&
                        robots.containsKey(next) &&
                        !visited.contains(next)) {
                    toVisit.add(next);
                }
            }
        }

    }

    private static void print(Map<Coord, List<Robot>> nextRobots, int rowSize, int colSize, int cycle) {
        System.out.println("second = " + cycle);
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (nextRobots.containsKey(new Coord(col, row))) {
                    System.out.print(nextRobots.get(new Coord(col, row)).size());
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    private static void print2(Map<Coord, List<Robot>> nextRobots, int rowSize, int colSize, int cycle) {
        System.out.println("second = " + cycle);
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (nextRobots.containsKey(new Coord(col, row))) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    private static int move(int current, int pace, int maxSize) {
        int total = current + pace;
        int actual = total % maxSize;

        if (total < 0) {
            if (pace < 0) {
                actual = maxSize + actual;
            }
        }
        return actual;
    }


    @Data
    @AllArgsConstructor
    static class Coord {
        int col;
        int row;
    }

    @Data
    @AllArgsConstructor
    static class Robot {
        int colPace;
        int rowPace;
    }

    @AllArgsConstructor
    @Data
    static class Square {
        Coord from;
        Coord to;

        boolean match(Coord other) {
            if (other.col >= from.col && other.col <= to.col
                    && other.row >= from.row && other.row <= to.row) {
                return true;
            }
            return false;
        }
    }
}
