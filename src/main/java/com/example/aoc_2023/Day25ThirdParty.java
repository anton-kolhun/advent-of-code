package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.Edge;
import com.example.aoc_2023.helper.ParseUtils;
import org.jgrapht.Graph;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;
import org.jgrapht.alg.interfaces.MinimumSTCutAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day25ThirdParty {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("day25.txt");
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

        Graph<String, DefaultEdge> g = new DefaultUndirectedGraph<>(DefaultEdge.class);
        for (Edge edge : edges) {
            g.addVertex(edge.from);
            g.addVertex(edge.to);
            g.addEdge(edge.from, edge.to);
        }
        MinimumSTCutAlgorithm<String, DefaultEdge> alg = new EdmondsKarpMFImpl<>(g);
        List<String> strings = new ArrayList<>(nodes);
        for (int i = 0; i < strings.size() - 1; i++) {
            String node1 = strings.get(i);
            for (int j = i + 1; j < strings.size(); j++) {
                String node2 = strings.get(j);
                int res = (int) alg.calculateMinCut(node1, node2);
                if (res == 3) {
                    System.out.println(alg.getSourcePartition().size() * (nodes.size() - alg.getSourcePartition().size()));
                    return;
                }
            }
        }
    }
}
