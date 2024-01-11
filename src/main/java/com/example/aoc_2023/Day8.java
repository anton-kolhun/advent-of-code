package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.Edge;
import com.example.aoc_2023.helper.MathUtils;
import com.example.aoc_2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day8 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("day8.txt");
        String instruction = lines.get(0);
        List<Edge> edges = new ArrayList<>();
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            List<String> nodeInfo = ParseUtils.splitByDelimiter(line, "=");
            String from = nodeInfo.get(0).trim();
            String edgesInfo = nodeInfo.get(1).trim();
            List<String> toInfos = ParseUtils.splitByDelimiter(edgesInfo, ",");
            String left = toInfos.get(0).substring(1);
            String right = toInfos.get(1).substring(1, toInfos.get(1).length() - 1);
            edges.add(new Edge(from, left));
            edges.add(new Edge(from, right));
        }
        Map<String, List<Edge>> fromNodes = edges.stream().collect(Collectors.groupingBy(e -> e.from));

        String cursor = "AAA";
        int steps = 0;
        boolean shouldContinue = true;
        while (shouldContinue)
            for (char c : instruction.toCharArray()) {
                var toEdges = fromNodes.get(cursor);
                if (c == 'L') {
                    cursor = toEdges.get(0).to;
                } else if (c == 'R') {
                    cursor = toEdges.get(1).to;
                }
                steps++;
                if (cursor.equals("ZZZ")) {
                    shouldContinue = false;
                    break;
                }
            }
        System.out.println(steps);

    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("day8.txt");
        String instruction = lines.get(0);
        List<Edge> edges = new ArrayList<>();
        List<String> startNodes = new ArrayList<>();
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            List<String> nodeInfo = ParseUtils.splitByDelimiter(line, "=");
            String from = nodeInfo.get(0).trim();
            if (from.endsWith("A")) {
                startNodes.add(from);
            }
            String edgesInfo = nodeInfo.get(1).trim();
            List<String> toInfos = ParseUtils.splitByDelimiter(edgesInfo, ",");
            String left = toInfos.get(0).substring(1);
            String right = toInfos.get(1).substring(1, toInfos.get(1).length() - 1);
            edges.add(new Edge(from, left));
            edges.add(new Edge(from, right));

        }
        Map<String, List<Edge>> fromNodes = edges.stream().collect(Collectors.groupingBy(e -> e.from));
        List<String> cursors = startNodes;
        List<BigInteger> numbs = new ArrayList<>();
        for (String cursor : cursors) {
            int steps = 0;
            boolean shouldContinue = true;
            while (shouldContinue) {
                for (char c : instruction.toCharArray()) {
                    var toEdges = fromNodes.get(cursor);
                    if (c == 'L') {
                        cursor = toEdges.get(0).to;
                    } else if (c == 'R') {
                        cursor = toEdges.get(1).to;
                    }
                    steps++;
                    if (cursor.endsWith("Z")) {
                        shouldContinue = false;
                        break;
                    }
                }
            }
            numbs.add(BigInteger.valueOf(steps));
        }

        BigInteger current = numbs.get(0);
        for (int i = 1; i < numbs.size(); i++) {
            current = MathUtils.lcm(current, numbs.get(i));
        }
        System.out.println(current);
    }
}
