package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Edge;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day12 {

    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day12.txt");
        List<Edge> edges = new ArrayList<>();
        for (String line : lines) {
            List<String> nodes = ParseUtils.splitByDelimiter(line, "-");
            Edge e1 = new Edge(nodes.get(0), nodes.get(1));
            edges.add(e1);
            Edge e2 = new Edge(nodes.get(1), nodes.get(0));
            edges.add(e2);
        }
        Map<String, List<Edge>> fromNodes = edges.stream().collect(Collectors.groupingBy(e -> e.from));
        List<List<String>> paths = findPathsTask1("end", fromNodes, List.of("start"));
        System.out.println("task1: " + paths.size());

        List<List<String>> pathsTask2 = findPathsTask2("end", fromNodes, List.of("start"), false);
        System.out.println("task2: " +  pathsTask2.size());
    }

    private static List<List<String>> findPathsTask1(String dest, Map<String, List<Edge>> fromNodes, List<String> currentPath) {
        var cursor = currentPath.getLast();
        List<List<String>> totalPaths = new ArrayList<>();
        for (Edge edge : fromNodes.get(cursor)) {
            String nextNode = edge.to;
            if (nextNode.equals(dest)) {
                List<String> nextPath = new ArrayList<>(currentPath);
                nextPath.add(nextNode);
                totalPaths.add(nextPath);
            } else if (nextNode.toUpperCase().equals(nextNode)) {
                List<String> nextPath = new ArrayList<>(currentPath);
                nextPath.add(nextNode);
                var res = findPathsTask1(dest, fromNodes, nextPath);
                totalPaths.addAll(res);
            } else if (nextNode.toLowerCase().equals(nextNode) && !currentPath.contains(nextNode)) {
                List<String> nextPath = new ArrayList<>(currentPath);
                nextPath.add(nextNode);
                var res = findPathsTask1(dest, fromNodes, nextPath);
                totalPaths.addAll(res);
            }
        }
        return totalPaths;
    }

    private static List<List<String>> findPathsTask2(String dest, Map<String, List<Edge>> fromNodes, List<String> currentPath,
                                                     boolean twiceVisited) {
        var cursor = currentPath.getLast();
        List<List<String>> totalPaths = new ArrayList<>();
        for (Edge edge : fromNodes.get(cursor)) {
            String nextNode = edge.to;
            if (nextNode.equals(dest)) {
                List<String> nextPath = new ArrayList<>(currentPath);
                nextPath.add(nextNode);
                totalPaths.add(nextPath);
            } else if (nextNode.toUpperCase().equals(nextNode)) {
                List<String> nextPath = new ArrayList<>(currentPath);
                nextPath.add(nextNode);
                var res = findPathsTask2(dest, fromNodes, nextPath, twiceVisited);
                totalPaths.addAll(res);
            } else if (nextNode.toLowerCase().equals(nextNode) && !nextNode.equals("start")) {
                if (currentPath.contains(nextNode)) {
                    if (!twiceVisited) {
                        List<String> nextPath = new ArrayList<>(currentPath);
                        nextPath.add(nextNode);
                        var res = findPathsTask2(dest, fromNodes, nextPath, true);
                        totalPaths.addAll(res);
                    }
                } else {
                    List<String> nextPath = new ArrayList<>(currentPath);
                    nextPath.add(nextNode);
                    var res = findPathsTask2(dest, fromNodes, nextPath, twiceVisited);
                    totalPaths.addAll(res);
                }
            }
        }
        return totalPaths;
    }

}
