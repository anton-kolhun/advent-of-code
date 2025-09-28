package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils;
import com.aoc.y2023.helper.GraphUtils.Edge;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day7 {

    public static void main(String[] args) {
        task();
    }

    public static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day7.txt");
        List<Edge> edges = new ArrayList<>();
        Set<String> allColors = new HashSet<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, "contain");
            String bagFrom = parts.get(0).replace("bags", "").trim();
            allColors.add(bagFrom);
            List<String> bagsTo = ParseUtils.splitByDelimiter(parts.get(1), ",");
            for (String to : bagsTo) {
                var toTrimmed = to.trim();
                int index = toTrimmed.indexOf(" ");
                var toBag = toTrimmed.substring(index + 1).replace("bags", "").replace("bag", "").replace(".", "").trim();
                allColors.add(toBag);
                Edge edge = new Edge(bagFrom, toBag);
                if (!edge.to.contains("other")) {
                    edge.weight = Integer.parseInt(toTrimmed.substring(0, index).trim());
                }
                edges.add(edge);
            }
        }
        int paths = 0;
        allColors.remove("shiny gold");
        for (String color : allColors) {
            var res = GraphUtils.bfs(edges, color, "shiny gold");
            if (!res.isEmpty()) {
                paths++;
            }
        }

        System.out.println("task1 = " + paths);

        Map<String, List<Edge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));

        int res = dfsBags(fromEdges, "shiny gold");
        res = res - 1; // subtract the "shiny gold" itself
        System.out.println("task2 = " + res);
    }

    private static int dfsBags(Map<String, List<Edge>> fromEdges, String cursor) {
        int total = 1;
        List<Edge> toEdges = fromEdges.getOrDefault(cursor, new ArrayList<>());
        for (Edge toEdge : toEdges) {
            int res = dfsBags(fromEdges, toEdge.to);
            total = total + res * toEdge.weight;
        }
        return total;
    }

}