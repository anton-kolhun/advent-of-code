package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Edge;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Day23 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day23.txt");
        List<Edge> edges = new ArrayList<>();
        Set<String> comps = new HashSet<>();
        for (String line : lines) {
            List<String> pairs = ParseUtils.splitByDelimiter(line, "-");
            Edge e = new Edge(pairs.get(0), pairs.get(1));
            edges.add(e);
            Edge eRevert = new Edge(pairs.get(1), pairs.get(0));
            edges.add(eRevert);
            comps.add(pairs.get(0));
            comps.add(pairs.get(1));
        }

        Map<String, List<Edge>> groupedFrom = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));

        Set<Set<String>> total = new HashSet<>();
        for (Edge edge : edges) {
            List<Set<String>> sets = findAll(edge, groupedFrom);
            total.addAll(sets);
        }

        var filtered = total.stream()
                .filter(set -> {
                    for (String s : set) {
                        if (s.startsWith("t")) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
        System.out.println(filtered.size());
    }

    private static List<Set<String>> findAll(Edge edge, Map<String, List<Edge>> groupedFrom) {
        var toConnected = groupedFrom.get(edge.to).stream().map(edge1 -> edge1.to).toList();
        var fromConnected = groupedFrom.get(edge.from).stream().map(edge1 -> edge1.to).toList();
        List<Set<String>> ofThree = new ArrayList<>();
        for (String c1 : fromConnected) {
            if (toConnected.contains(c1)) {
                ofThree.add(Set.of(c1, edge.from, edge.to));
            }
        }
        return ofThree;
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day23.txt");
        List<Edge> edges = new ArrayList<>();
        Set<String> comps = new HashSet<>();
        for (String line : lines) {
            List<String> pairs = ParseUtils.splitByDelimiter(line, "-");
            Edge e = new Edge(pairs.get(0), pairs.get(1));
            edges.add(e);
            Edge eRevert = new Edge(pairs.get(1), pairs.get(0));
            edges.add(eRevert);
            comps.add(pairs.get(0));
            comps.add(pairs.get(1));
        }

        Map<String, List<Edge>> groupedFrom = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));

        Map<String, Set<Edge>> groupedFromSet = new HashMap<>();
        for (Map.Entry<String, List<Edge>> entry : groupedFrom.entrySet()) {
            groupedFromSet.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }

        List<Set<String>> sets = findAllConnected(new ArrayList<>(comps), groupedFromSet, new HashSet<>());

        Set<String> max = sets.stream().max(Comparator.comparingInt(Set::size)).get();

        var updated = max.stream().sorted()
                .reduce((s, s2) -> s + "," + s2)
                .get();
        System.out.println(updated);
    }


    private static List<Set<String>> findAllConnected(List<String> remaining, Map<String, Set<Edge>> groupedFrom,
                                                      Set<String> currentGroup) {
        if (remaining.isEmpty()) {
            return List.of(currentGroup);
        }
        var nextRemaining = new ArrayList<>(remaining);
        var newNode = nextRemaining.remove(0);
        boolean coonnetedToAll = true;
        for (String node : currentGroup) {
            if (!groupedFrom.get(node).contains(new Edge(node, newNode))) {
                coonnetedToAll = false;
                break;
            }
        }
        List<Set<String>> total = new ArrayList<>();
        if (coonnetedToAll) {
            Set<String> nextGroup = new HashSet<>(currentGroup);
            nextGroup.add(newNode);
            var res = findAllConnected(nextRemaining, groupedFrom, nextGroup);
            total.addAll(res);
        }
        var res = findAllConnected(nextRemaining, groupedFrom, currentGroup);
        total.addAll(res);
        return total;
    }

}