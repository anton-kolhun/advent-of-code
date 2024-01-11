package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.CoordEdge;
import com.example.aoc_2023.helper.GraphUtils.Coordinate;
import lombok.AllArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.aoc_2023.helper.GraphUtils.dejkstra;

public class Day11Part2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task2();
    }

    public static void task2() throws ExecutionException, InterruptedException {
        List<String> lines = FilesUtils.readFile("day11.txt");
        Map<Coordinate, ShiftInfo> shift = new HashMap<>();
        List<Integer> emptyRows = emptyRows(lines);
        List<Integer> emptyCols = emptyCols(lines);
        Iterator<Integer> rowIterator = emptyRows.iterator();
        Integer rowCursor = rowIterator.next();
        int rowShift = 0;
        for (int row = 0; row < lines.size(); row++) {
            if (rowCursor != null && row > rowCursor) {
                rowShift += 999999;
                if (rowIterator.hasNext()) {
                    rowCursor = rowIterator.next();
                } else {
                    rowCursor = null;
                }
            }
            Iterator<Integer> colIterator = emptyCols.iterator();
            Integer colCursor = colIterator.next();

            int colShift = 0;
            for (int col = 0; col < lines.get(0).length(); col++) {
                if (colCursor != null && col > colCursor) {
                    colShift += 999999;
                    if (colIterator.hasNext()) {
                        colCursor = colIterator.next();
                    } else {
                        colCursor = null;
                    }
                }
                shift.put(new Coordinate(row, col), new ShiftInfo(rowShift, colShift));
            }
        }
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

        BigInteger sum = calculateSum(pairs, edges, shift);
        System.out.println(sum);

    }

    private static BigInteger calculateSum(List<Set<Coordinate>> pairs, List<CoordEdge> edges, Map<Coordinate, ShiftInfo> shift) throws ExecutionException, InterruptedException {
        int stepSize = pairs.size() / 16;
        int current = 0;
        var executors = Executors.newFixedThreadPool(16);
        List<Future<BigInteger>> results = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            var pairsSubList = pairs.subList(current, current + stepSize);
            Future<BigInteger> res = executors.submit(() -> {
                BigInteger sum = BigInteger.ZERO;
                for (int j = 0; j < pairsSubList.size(); j++) {
                    Set<Coordinate> pair = pairsSubList.get(j);
                    var p = new ArrayList<>(pair);
                    var p1 = p.get(0);
                    var p2 = p.get(1);
                    List<Coordinate> path = dejkstra(edges, p1, p2);

                    int shiftSize = Math.abs(shift.get(p1).rowShift - shift.get(p2).rowShift) + Math.abs(shift.get(p1).colShift - shift.get(p2).colShift);
                    long ress = path.size() - 1 + shiftSize;
                    //  System.out.println(ress);
                    sum = sum.add(BigInteger.valueOf(ress));
                }
                return sum;
            });
            results.add(res);
            current = current + stepSize;
        }
        var pairsSubList = pairs.subList(current, pairs.size());
        Future<BigInteger> res = executors.submit(() -> {
            BigInteger sum = BigInteger.ZERO;
            for (int j = 0; j < pairsSubList.size(); j++) {
                Set<Coordinate> pair = pairsSubList.get(j);
                var p = new ArrayList<>(pair);
                var p1 = p.get(0);
                var p2 = p.get(1);
                List<Coordinate> path = dejkstra(edges, p1, p2);

                int shiftSize = Math.abs(shift.get(p1).rowShift - shift.get(p2).rowShift) + Math.abs(shift.get(p1).colShift - shift.get(p2).colShift);
                long ress = path.size() - 1 + shiftSize;
                //System.out.println(ress);
                sum = sum.add(BigInteger.valueOf(ress));
            }
            return sum;
        });
        results.add(res);

        BigInteger total = BigInteger.ZERO;
        for (Future<BigInteger> result : results) {
            BigInteger re = result.get();
            total = total.add(re);
        }
        executors.shutdown();
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


    private static List<Integer> emptyRows(List<String> lines) {
        List<Integer> emptyRows = new ArrayList<>();
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
            if (isLineEmpty) {
                emptyRows.add(row);
            }
        }
        return emptyRows;
    }


    private static List<Integer> emptyCols(List<String> lines) {
        List<Integer> emptyCols = new ArrayList<>();
        for (int col = 0; col < lines.get(0).length(); col++) {
            boolean isColEmpty = true;
            for (int row = 0; row < lines.size(); row++) {
                char c = lines.get(row).charAt(col);
                if (c == '#') {
                    isColEmpty = false;
                }
            }
            if (isColEmpty) {
                emptyCols.add(col);
            }
        }
        return emptyCols;
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


    @AllArgsConstructor
    private static class ShiftInfo {
        private int rowShift;
        private int colShift;
    }

}
