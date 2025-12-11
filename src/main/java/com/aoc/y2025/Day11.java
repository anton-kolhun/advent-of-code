package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Edge;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day11 {


    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day11.txt");
        List<Edge> edges = new ArrayList<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, ":").stream().map(String::trim).toList();
            String from = parts.get(0);
            var toParts = ParseUtils.splitByDelimiter(parts.get(1), " ").stream().map(String::trim).toList();
            for (String toPart : toParts) {
                edges.add(new Edge(from, toPart));
            }
        }
        Map<String, List<Edge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));

        int res = dfs(fromEdges, "you", "out", new HashSet<>());
        System.out.println("task1: " + res);
    }

    private static int dfs(Map<String, List<Edge>> fromEdges, String cursor, String end, Set<String> visited) {
        int total = 0;
        if (cursor.equals(end)) {
            total = total + 1;
        } else {
            List<Edge> edges = fromEdges.getOrDefault(cursor, new ArrayList<>());
            for (Edge edge : edges) {
                if (!visited.contains(edge.to)) {
                    var nextVisited = new HashSet<>(visited);
                    nextVisited.add(edge.to);
                    int res = dfs(fromEdges, edge.to, end, nextVisited);
                    total = total + res;
                }
            }
        }
        return total;
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day11.txt");
        List<Edge> edges = new ArrayList<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, ":").stream().map(String::trim).toList();
            String from = parts.get(0);
            var toParts = ParseUtils.splitByDelimiter(parts.get(1), " ").stream().map(String::trim).toList();
            for (String toPart : toParts) {
                edges.add(new Edge(from, toPart));
            }
        }

        Map<String, List<Edge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));

        // Given there is no path from "dac" to "fft".
        // Then the only way to get from "svr" to "you" walking past "dac" and "fft":
        // "svr" -> "fft" -> "dac" -> "out"
        var res1 = dfs(fromEdges, "svr", "fft", new HashMap<>(), new LinkedHashSet<>());
        var res2 = dfs(fromEdges, "fft", "dac", new HashMap<>(), new LinkedHashSet<>());
        var res3 = dfs(fromEdges, "dac", "out", new HashMap<>(), new LinkedHashSet<>());
        System.out.println("task2: " + res1.multiply(res2).multiply(res3));
    }


    private static BigInteger dfs(Map<String, List<Edge>> fromEdges, String cursor, String end, Map<String, BigInteger> cache,
                                  LinkedHashSet<String> path) {
        if (cursor.equals(end)) {
            return BigInteger.ONE;
        }
        BigInteger total = BigInteger.ZERO;
        if (cache.containsKey(cursor)) {
            return cache.get(cursor);
        }
        List<Edge> edges = fromEdges.getOrDefault(cursor, new ArrayList<>());
        for (Edge edge : edges) {
            BigInteger res;
            LinkedHashSet<String> nextPath = new LinkedHashSet<>(path);
            nextPath.add(edge.to);
            res = dfs(fromEdges, edge.to, end, cache, nextPath);
            total = total.add(res);
        }
        cache.put(cursor, total);
        return total;
    }

}
