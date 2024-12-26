package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils;
import com.aoc.y2023.helper.GraphUtils.CoordEdge;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Day18 {
    static List<Coordinate> moves = List.of(
            new Coordinate(0, 1),
            new Coordinate(0, -1),
            new Coordinate(1, 0),
            new Coordinate(-1, 0)
    );

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day18.txt");

        Map<Coordinate, Character> coordToChar = new HashMap<>();

        for (int row = 0; row <= 70; row++) {
            for (int col = 0; col <= 70; col++) {
                coordToChar.put(new Coordinate(row, col), '.');
            }
        }

        for (int i = 0; i < 1024; i++) {
            String line = lines.get(i);
            List<Integer> coordParts = ParseUtils.splitByDelimiter(line, ",")
                    .stream().map(Integer::parseInt).toList();
            coordToChar.put(new Coordinate(coordParts.get(1), coordParts.get(0)), '#');
        }

        List<CoordEdge> edges = new ArrayList<>();
        var start = new Coordinate(0, 0);
        var end = new Coordinate(70, 70);
        for (Map.Entry<Coordinate, Character> entry : coordToChar.entrySet()) {
            if (entry.getValue() == '.') {
                for (Coordinate move : moves) {
                    var next = new Coordinate(entry.getKey().row + move.row, entry.getKey().col + move.col);
                    if (coordToChar.getOrDefault(next, 'X') == '.') {
                        edges.add(new CoordEdge(entry.getKey(), next));
                    }
                }

            }
        }
        List<Coordinate> path = GraphUtils.dejkstra(edges, start, end);
        System.out.println(path.size() - 1);
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day18.txt");
        boolean pathExists = true;
        int size = 1000;
        while (pathExists) {

            Map<Coordinate, Character> coordToChar = new HashMap<>();
            for (int row = 0; row <= 70; row++) {
                for (int col = 0; col <= 70; col++) {
                    coordToChar.put(new Coordinate(row, col), '.');
                }
            }

            for (int i = 0; i <= size; i++) {
                String line = lines.get(i);
                List<Integer> coordParts = ParseUtils.splitByDelimiter(line, ",")
                        .stream().map(Integer::parseInt).toList();
                coordToChar.put(new Coordinate(coordParts.get(1), coordParts.get(0)), '#');
            }

            List<CoordEdge> edges = new ArrayList<>();
            var start = new Coordinate(0, 0);
            var end = new Coordinate(70, 70);
            for (Map.Entry<Coordinate, Character> entry : coordToChar.entrySet()) {
                if (entry.getValue() == '.') {
                    for (Coordinate move : moves) {
                        var next = new Coordinate(entry.getKey().row + move.row, entry.getKey().col + move.col);
                        if (coordToChar.getOrDefault(next, 'X') == '.') {
                            edges.add(new CoordEdge(entry.getKey(), next));
                        }
                    }

                }
            }
            List<Coordinate> path = GraphUtils.dejkstra(edges, start, end);
            if (path == null) {
                //System.out.println(size);
                System.out.println(lines.get(size));
                pathExists = false;
            }
            size++;
        }

    }
}
