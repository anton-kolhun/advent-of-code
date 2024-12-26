package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Edge;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Day25Karger {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        task1();
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms");


    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day25.txt");
        List<Edge> edges = new ArrayList<>();
        Set<String> nodes = new HashSet<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            List<String> parts = ParseUtils.splitByDelimiter(line, ": ");
            String from = parts.get(0);
            String to = parts.get(1);
            List<String> toEls = ParseUtils.splitByDelimiter(to, " ");
            for (String toEl : toEls) {
                edges.add(new Edge(from, toEl));
                edges.add(new Edge(toEl, from));
                nodes.add(from);
                nodes.add(toEl);
            }
        }

        //var fromEdges = edges.stream().collect(Collectors.groupingBy(e -> e.from));
        //var toEdges = edges.stream().collect(Collectors.groupingBy(e -> e.to));
        karger(edges, new ArrayList<>(nodes));
    }

    public static void karger(List<Edge> edges, List<String> nodes) {
        var toEdges = edges.stream().collect(Collectors.groupingBy(e -> e.to));
        var res = doKarger(new HashSet<>(edges), nodes);
        var gr1 = expand(res);
        var ed = getEdgesToRemove(res, toEdges);
        System.out.println(gr1);
        System.out.println(gr1.size() * (nodes.size() - gr1.size()));

    }

    private static KargerInfo doKarger(Set<Edge> edges, List<String> nodes) {

        var toEdges = edges.stream().collect(Collectors.groupingBy(e -> e.to));

        var minResLength = Integer.MAX_VALUE;
        KargerInfo minRes = new KargerInfo();
        var r1 = new Random();
        for (int i = 0; i < 100000; i++) {
            Map<String, Set<String>> merged = new HashMap<>();
            List<String> updatedNodes = new ArrayList<>(nodes);
            Set<Edge> updatedEdges = new HashSet<>(edges);
            boolean isGood = true;
            while (updatedNodes.size() > 2) {
                var updatedToEdges = updatedEdges.stream().collect(Collectors.groupingBy(e -> e.to));
                var index = r1.nextInt(updatedEdges.size());
                var edge = new ArrayList<>(updatedEdges).get(index);
                var n1 = edge.from;
                var n2 = edge.to;
                List<Edge> toN1edges = updatedToEdges.get(n1);
                for (Edge toN1Edge : toN1edges) {
                    if (toN1Edge.equals(new Edge(edge.to, edge.from))) {
                        continue;
                    }
                    //System.out.println("Adding edge " + new Edge(toN1Edge.from, n2));
                    updatedEdges.add(new Edge(toN1Edge.from, n2));
                    updatedEdges.add(new Edge(n2, toN1Edge.from));
                    //System.out.println("Removing  edge " + toN1Edge);
                    updatedEdges.remove(toN1Edge);
                    updatedEdges.remove(new Edge(toN1Edge.to, toN1Edge.from));
                }
                //System.out.println("removing " + n1);
                updatedEdges.remove(edge);
                updatedEdges.remove(new Edge(edge.to, edge.from));
                updatedNodes.remove(n1);
                merged.merge(n2, Set.of(n1), (l1, l2) -> {
                    var l = new HashSet<>(l1);
                    l.addAll(l2);
                    return l;
                });
            }
            Set<String> nodeGroup = expand(updatedNodes.get(0), updatedNodes.get(1), merged);
            Set<Edge> edgesToRemove = getEdgesToRemove(nodeGroup, toEdges);
            if (edgesToRemove.size() < minResLength) {
                minResLength = edgesToRemove.size();
                if (minResLength == 3) {
                    System.out.println("x");
                }
                minRes = new KargerInfo(merged, updatedNodes.get(0), updatedNodes.get(1));
            }
        }
        return minRes;
    }

    private static Set<String> expand(String node1, String node2, Map<String, Set<String>> merged) {
        List<String> cursors = new ArrayList<>();
        if (merged.containsKey(node1)) {
            cursors.add(node1);
        } else {
            cursors.add(node2);
        }
        Set<String> visited = new HashSet<>();
        while (!cursors.isEmpty()) {
            List<String> nextCursors = new ArrayList<>();
            for (String cursor : cursors) {
                visited.add(cursor);
                var next = merged.getOrDefault(cursor, new HashSet<>());
                for (String s : next) {
                    if (!visited.contains(s)) {
                        nextCursors.add(s);
                    }
                }
            }
            cursors = nextCursors;
        }
        return visited;
    }

    private static Set<Edge> getEdgesToRemove(KargerInfo info, Map<String, List<Edge>> toEdges) {
        //Set<String> total = new HashSet<>();
        Set<String> visited = expand(info);
        Set<Edge> edgesToRm = new HashSet<>();
        for (String v : visited) {
            var edges = toEdges.get(v);
            for (Edge edge : edges) {
                if (!visited.contains(edge.from) || !visited.contains(edge.to)) {
                    edgesToRm.add(edge);
                }
            }

        }
        return edgesToRm;
    }

    private static Set<Edge> getEdgesToRemove(Set<String> groupNode, Map<String, List<Edge>> toEdges) {
        //Set<String> total = new HashSet<>();
        Set<Edge> edgesToRm = new HashSet<>();
        for (String v : groupNode) {
            var edges = toEdges.get(v);
            for (Edge edge : edges) {
                if (!groupNode.contains(edge.from) || !groupNode.contains(edge.to)) {
                    edgesToRm.add(edge);
                }
            }

        }
        return edgesToRm;
    }


    private static Set<String> expand(KargerInfo info) {
        List<String> cursors = new ArrayList<>();
        if (info.mergedMap.containsKey(info.groupNode1)) {
            cursors.add(info.groupNode1);
        } else {
            cursors.add(info.groupNode2);
        }
        Set<String> visited = new HashSet<>();
        while (!cursors.isEmpty()) {
            List<String> nextCursors = new ArrayList<>();
            for (String cursor : cursors) {
                visited.add(cursor);
                var next = info.mergedMap.getOrDefault(cursor, new HashSet<>());
                for (String s : next) {
                    if (!visited.contains(s)) {
                        nextCursors.add(s);
                    }
                }
            }
            cursors = nextCursors;
        }
        return visited;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    private static class KargerInfo {

        private static KargerInfo marker = new KargerInfo();

        private Map<String, Set<String>> mergedMap;
        private String groupNode1;
        private String groupNode2;
    }
}
