package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.CoordEdge;
import com.example.aoc_2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.aoc_2023.helper.GraphUtils.dejkstra;

public class Day11 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1();
    }

    public static void task1() throws ExecutionException, InterruptedException {
        List<String> lines = FilesUtils.readFile("day11_t.txt");
        lines = transformRows(lines);
        lines = transformCols(lines);
        List<Coordinate> galaxies = new ArrayList<>();
        Map<Coordinate, Character> map = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                map.put(new Coordinate(row, col), c);
                if (c == '#') {
                    galaxies.add(new Coordinate(row, col));
                }
            }
        }
        // System.out.println(galaxies);

        List<Set<Coordinate>> pairs = findPairs(galaxies);

        List<CoordEdge> edges = new ArrayList<>();
        for (Map.Entry<Coordinate, Character> entry : map.entrySet()) {
            var point = entry.getKey();
            edges.add(new CoordEdge(point, new Coordinate(point.row + 1, point.col)));
            edges.add(new CoordEdge(point, new Coordinate(point.row - 1, point.col)));
            edges.add(new CoordEdge(point, new Coordinate(point.row, point.col + 1)));
            edges.add(new CoordEdge(point, new Coordinate(point.row, point.col - 1)));
        }

        long sum = calculateSum(pairs, edges);
        System.out.println(sum);

    }

    private static long calculateSum(List<Set<Coordinate>> pairs, List<CoordEdge> edges) throws ExecutionException, InterruptedException {
        int stepSize = pairs.size() / 10;
        int current = 0;
        var executors = Executors.newFixedThreadPool(1);
        List<Future<Long>> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var pairsSubList = pairs.subList(current, current + stepSize);
            Future<Long> res = executors.submit(() -> {
                long sum = 0;
                for (int j = 0; j < pairsSubList.size(); j++) {
                    Set<Coordinate> pair = pairsSubList.get(j);
                    var p = new ArrayList<>(pair);
                    var p1 = p.get(0);
                    var p2 = p.get(1);
                    List<Coordinate> path = dejkstra(edges, p1, p2);
                    long ress = path.size() - 1;
                    System.out.println(ress);
                    sum += ress;
                }
                return sum;
            });
            results.add(res);
            current = current + stepSize;
        }
        var pairsSubList = pairs.subList(current, pairs.size());
        Future<Long> res = executors.submit(() -> {
            long sum = 0;
            for (int j = 0; j < pairsSubList.size(); j++) {
                Set<Coordinate> pair = pairsSubList.get(j);
                var p = new ArrayList<>(pair);
                var p1 = p.get(0);
                var p2 = p.get(1);
                List<Coordinate> path = dejkstra(edges, p1, p2);
                sum += path.size() - 1;
            }
            return sum;
        });
        results.add(res);

        long total = 0;
        for (Future<Long> result : results) {
            long re = result.get();
            total = total + re;
        }
        return total;
    }

    private static List<Set<Coordinate>> findPairs(List<Coordinate> galaxies) {

        List<Set<Coordinate>> res = new ArrayList<>();
        for (int i = 0; i < galaxies.size() - 1; i++) {
            Coordinate galaxy1 = galaxies.get(i);
            for (int j = i + 1; j < galaxies.size(); j++) {
                Coordinate galaxy2 = galaxies.get(j);
                res.add(new HashSet<>(Arrays.asList(galaxy1, galaxy2)));
            }

        }
        return res;

    }


    private static List<String> transformRows(List<String> lines) {
        List<String> newLines = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            boolean isLineEmpty = true;
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == '#') {
                    isLineEmpty = false;
                }
            }
            newLines.add(line);
            if (isLineEmpty) {
                StringBuilder builder = new StringBuilder();
                for (int col = 0; col < charArray.length; col++) {
                    builder.append(".");
                }
                newLines.add(builder.toString());
            }
        }
        return newLines;
    }

    private static List<String> transformCols(List<String> newLines) {
        List<String> newLinesV2 = new ArrayList<>(newLines);
        int rowShift = 0;
        for (int col = 0; col < newLines.get(0).length(); col++) {
            boolean isColEmpty = true;
            for (int row = 0; row < newLines.size(); row++) {
                char c = newLines.get(row).charAt(col);
                if (c == '#') {
                    isColEmpty = false;
                }
            }
            if (isColEmpty) {
                List<String> updated = new ArrayList<>();
                for (String newLine : newLinesV2) {
                    char[] charArray = newLine.toCharArray();
                    List<Character> charArrayUpdated = new ArrayList<>();
                    for (int i = 0; i < charArray.length; i++) {
                        char c = charArray[i];
                        charArrayUpdated.add(c);
                        if (i == col + rowShift) {
                            charArrayUpdated.add('.');
                        }
                    }
                    StringBuilder newStr = new StringBuilder();
                    for (Character character : charArrayUpdated) {
                        newStr.append(character);
                    }
                    updated.add(newStr.toString());
                }
                newLinesV2 = updated;
                rowShift++;
            }
        }
        return newLinesV2;
    }

    private static void print(List<String> newLinesV2) {
        for (String s : newLinesV2) {
            for (char c : s.toCharArray()) {
                System.out.print(c);
            }
            System.out.println();
        }

    }


}
