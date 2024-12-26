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
import java.util.Set;
import java.util.stream.Collectors;

public class Day25KargerV2 {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        task1();
        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");


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
        var res = doKarger(new HashSet<>(edges), new HashSet<>(), nodes, new HashMap<>(), toEdges);
        var gr1 = expand(res);
        System.out.println(gr1);
        var ed = getEdgesToRemove(res, toEdges);
        System.out.println(ed);
        System.out.println(gr1.size() * (nodes.size() - gr1.size()));

    }

    private static KargerInfo doKarger(Set<Edge> edges, Set<Map<String, Set<String>>> cache, List<String> nodes,
                                       Map<String, Set<String>> merged, Map<String, List<Edge>> totalEdges) {
        if (nodes.size() == 2) {
            return new KargerInfo(merged, nodes.get(0), nodes.get(1));
        }

        var toEdges = edges.stream().collect(Collectors.groupingBy(e -> e.to));

        KargerInfo minRes = KargerInfo.marker;
        var minResLength = Integer.MAX_VALUE;
        for (Edge edge : edges) {
            var n1 = edge.from;
            var n2 = edge.to;

            List<Edge> toN1edges = toEdges.get(n1);
            Set<Edge> updated = new HashSet<>(edges);
            for (Edge toN1Edge : toN1edges) {
                if (toN1Edge.equals(new Edge(n2, n1))) {
                    continue;
                }
                updated.add(new Edge(toN1Edge.from, n2));
                updated.add(new Edge(n2, toN1Edge.from));
                updated.remove(toN1Edge);
                updated.remove(new Edge(toN1Edge.to, toN1Edge.from));
            }

            List<String> updatedNodes = new ArrayList<>(nodes);
            updatedNodes.remove(n1);
            updated.remove(edge);
            updated.remove(new Edge(edge.to, edge.from));
            var updatedMerged = new HashMap<>(merged);
            updatedMerged.merge(n2, Set.of(n1), (l1, l2) -> {
                var l = new HashSet<>(l1);
                l.addAll(l2);
                return l;
            });
            if (!cache.add(updatedMerged)) {
                    continue;
            }
            KargerInfo res = doKarger(updated, cache, updatedNodes, updatedMerged, totalEdges);
            if (res != KargerInfo.marker) {
                Set<Edge> edgesToRemove = getEdgesToRemove(res, totalEdges);
                if (edgesToRemove.size() <= minResLength) {
                    minResLength = edgesToRemove.size();
                    System.out.println(minResLength);
                    if (minResLength == 3) {
                        System.out.println("x");
                    }
                    minRes = res;
                }
            }
        }
        //System.out.println(minResLength);
        return minRes;
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
