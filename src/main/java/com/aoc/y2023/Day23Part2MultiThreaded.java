package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day23Part2MultiThreaded {

    private static ExecutorService executor = Executors.newFixedThreadPool(5000);

    private static volatile AtomicInteger activeThreads = new AtomicInteger();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task2();
        executor.shutdown();
    }

    public static void task2() throws ExecutionException, InterruptedException {
        List<String> lines = FilesUtils.readFile("aoc_2023/day23.txt");
        Map<Coordinate, Character> coords = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coords.put(new Coordinate(row, col), c);
            }
        }
        findPath(coords, new Coordinate(0, 1), new Coordinate(lines.size() - 1, lines.get(0).length() - 2), lines);


    }

    private static void findPath(Map<Coordinate, Character> coords, Coordinate start, Coordinate end, List<String> lines) throws ExecutionException, InterruptedException {
        List<List<Coordinate>> paths = doFindPath(coords, end, List.of(start), new HashMap<>(), lines);
        int maxPathSize = Integer.MIN_VALUE;
        for (List<Coordinate> path : paths) {
            if (path.size() > maxPathSize) {
                maxPathSize = path.size();
            }
        }
        System.out.println(maxPathSize - 1);

    }

    private static List<List<Coordinate>> doFindPath(Map<Coordinate, Character> coords, Coordinate end,
                                                     List<Coordinate> currentPath, Map<Coordinate, List<List<Coordinate>>> cache, List<String> lines) throws ExecutionException, InterruptedException {
        var cursor = currentPath.get(currentPath.size() - 1);
        if (cursor.equals(end)) {
            //print(currentPath, coords, lines);
//            System.out.println(Thread.currentThread() + " " +  currentPath.size());
            System.out.println(currentPath.size());

            return List.of(currentPath);
        }
        if (coords.getOrDefault(cursor, 'x') == '#') {
            return Collections.emptyList();
        }
        if (cursor.row < 0 || cursor.row >= lines.size() || cursor.col < 0 || cursor.col >= lines.get(0).length()) {
            return Collections.emptyList();
        }
        if (new HashSet<>(currentPath).size() < currentPath.size()) {
            return Collections.emptyList();
        }
        //print(currentPath, coords, lines);
        List<List<Coordinate>> total = new ArrayList<>();

        if (activeThreads.get() < 2500) {
            Future<List<List<Coordinate>>> resUp = executor.submit(() -> {
                activeThreads.incrementAndGet();
                var result = checkDir(coords, end, currentPath, cache, lines, cursor, GraphUtils.Direction.UP.shift);
                activeThreads.decrementAndGet();
                return result;
            });

            Future<List<List<Coordinate>>> resDown = executor.submit(() -> {
                activeThreads.incrementAndGet();
                var result = checkDir(coords, end, currentPath, cache, lines, cursor, GraphUtils.Direction.DOWN.shift);
                activeThreads.decrementAndGet();
                return result;
            });

            Future<List<List<Coordinate>>> resLeft = executor.submit(() -> {
                activeThreads.incrementAndGet();
                var result = checkDir(coords, end, currentPath, cache, lines, cursor, GraphUtils.Direction.LEFT.shift);
                activeThreads.decrementAndGet();
                return result;
            });

            Future<List<List<Coordinate>>> resRight = executor.submit(() -> {
                activeThreads.incrementAndGet();
                var result = checkDir(coords, end, currentPath, cache, lines, cursor, GraphUtils.Direction.RIGHT.shift);
                activeThreads.decrementAndGet();
                return result;
            });

            total.addAll(resUp.get());
            total.addAll(resDown.get());
            total.addAll(resLeft.get());
            total.addAll(resRight.get());
        } else {
            var res = checkDir(coords, end, currentPath, cache, lines, cursor, GraphUtils.Direction.UP.shift);
            total.addAll(res);

            res = checkDir(coords, end, currentPath, cache, lines, cursor, GraphUtils.Direction.LEFT.shift);
            total.addAll(res);

            res = checkDir(coords, end, currentPath, cache, lines, cursor, GraphUtils.Direction.RIGHT.shift);
            total.addAll(res);

            res = checkDir(coords, end, currentPath, cache, lines, cursor, GraphUtils.Direction.DOWN.shift);
            total.addAll(res);

        }

        return total;
    }

    private static void print(List<Coordinate> cursor, Map<Coordinate, Character> coords, List<String> lines) {
        var cursSet = new HashSet<>(cursor);
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(0).length(); col++) {
                if (cursSet.contains(new Coordinate(row, col))) {
                    System.out.print("0");
                } else {
                    System.out.print(coords.get(new Coordinate(row, col)));
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static List<List<Coordinate>> checkDir(Map<Coordinate, Character> coords, Coordinate end, List<Coordinate> currentPath, Map<Coordinate,
            List<List<Coordinate>>> cache, List<String> lines, Coordinate cursor, Coordinate move) throws ExecutionException, InterruptedException {
        ArrayList<Coordinate> nextPath;
        var nextCoord = new Coordinate(cursor.row + move.row, cursor.col + move.col);
        nextPath = new ArrayList<>(currentPath);
        nextPath.add(nextCoord);
        List<List<Coordinate>> total = new ArrayList<>();
        List<List<Coordinate>> cached = cache.get(nextCoord);
        if (cached != null) {
            for (List<Coordinate> tail : cached) {
                var res = new ArrayList<>(nextPath);
                res.addAll(tail);
                if (new HashSet<>(res).size() == res.size()) {
                    total.add(res);
                }
            }
        } else {
            var result = doFindPath(coords, end, nextPath, cache, lines);
            var filtered = result.stream().filter(res -> !res.isEmpty()).collect(Collectors.toList());
            List<List<Coordinate>> tails = new ArrayList<>();
            for (List<Coordinate> res : filtered) {
                var tail = res.subList(nextPath.size(), res.size());
                tails.add(tail);
            }
            if (!tails.isEmpty()) {
                //cache.put(nextCoord, tails);
            }
            total.addAll(result);
        }
        return total;
    }
}
