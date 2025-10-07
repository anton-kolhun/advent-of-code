package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils;
import com.aoc.y2023.helper.GraphUtils.CoordEdge;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Day15 {
    private static List<Coordinate> moves = List.of(new Coordinate(0, 1), new Coordinate(1, 0), new Coordinate(0, -1), new Coordinate(-1, 0));

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day15.txt");
        List<CoordEdge> edges = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                var currentCoord = new Coordinate(row, col);
                List<Coordinate> neibs = getNeibs(currentCoord);
                for (Coordinate neib : neibs) {
                    if (neib.col >= 0 && neib.col < line.length() && neib.row >= 0 && neib.row < lines.size()) {
                        var coordEdge = new CoordEdge(neib, currentCoord);
                        coordEdge.weight = Integer.parseInt(String.valueOf(charArray[col]));
                        edges.add(coordEdge);
                    }
                }
            }
        }
        List<Coordinate> minPath = GraphUtils.dejkstraByWeight(edges, new Coordinate(0, 0), new Coordinate(lines.size() - 1, lines.get(0).length() - 1));
        Map<CoordEdge, Integer> edgesWeight = new HashMap<>();
        for (CoordEdge edge : edges) {
            edgesWeight.put(new CoordEdge(edge.from, edge.to, edge.weight), edge.weight);
        }
        int totalPath = 0;
        for (int i = 0; i < minPath.size() - 1; i++) {
            Coordinate coordinate = minPath.get(i);
            Coordinate next = minPath.get(i + 1);
            totalPath = totalPath + edgesWeight.get(new CoordEdge(coordinate, next));
        }
        System.out.println("task1: " + totalPath);
    }


    private static List<Coordinate> getNeibs(Coordinate coord) {
        return moves.stream().map(shift -> new Coordinate(coord.row + shift.row, coord.col + shift.col)).collect(Collectors.toList());
    }


    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day15.txt");
        Map<Coordinate, Coordinate> startingCoords = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                var coord = new Coordinate(row, col);
                coord.val = Integer.parseInt(String.valueOf(line.charAt(col)));
                startingCoords.put(coord, coord);
            }
        }
        int height = lines.size();
        int width = lines.get(0).length();
        Map<Coordinate, Coordinate> coords = new HashMap<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                for (Map.Entry<Coordinate, Coordinate> entry : startingCoords.entrySet()) {
                    var coord = entry.getKey();
                    var newCoordRow = coord.row + height * row;
                    var newCoordCol = coord.col + width * col;
                    var newCoord = new Coordinate(newCoordRow, newCoordCol);
                    int value = coord.val + row + col;
                    newCoord.val = (value - 1) % 9 + 1;
                    coords.put(newCoord, newCoord);
                }
            }
        }
        width = width * 5;
        height = height * 5;
        List<CoordEdge> edges = new ArrayList<>();
        for (Map.Entry<Coordinate, Coordinate> entry : coords.entrySet()) {
            var currentCoord = entry.getKey();
            List<Coordinate> neibs = getNeibs(currentCoord);
            for (Coordinate neib : neibs) {
                if (neib.col >= 0 && neib.col < width && neib.row >= 0 && neib.row < height) {
                    var coordEdge = new CoordEdge(neib, currentCoord);
                    coordEdge.weight = currentCoord.val;
                    edges.add(coordEdge);
                }
            }
        }
        calcUsing3rdParty(edges);
        List<Coordinate> minPath = GraphUtils.dejkstraByWeight(edges, new Coordinate(0, 0), new Coordinate(height - 1, width - 1));

        int totalPath = 0;
        for (int i = 1; i < minPath.size(); i++) {
            Coordinate coordinate = minPath.get(i);
            totalPath = totalPath + coordinate.val;
        }
        System.out.println("task2: " + totalPath);

    }

    private static void calcUsing3rdParty(List<CoordEdge> edges) {
        Graph<Coordinate, MyDefaultWeightedEdge> g = new DefaultDirectedWeightedGraph<>(MyDefaultWeightedEdge.class);
        for (CoordEdge edge : edges) {
            g.addVertex(edge.from);
            g.addVertex(edge.to);
            var edge3 = g.addEdge(edge.from, edge.to);
            g.setEdgeWeight(edge3, edge.weight);
        }
        DijkstraShortestPath<Coordinate, MyDefaultWeightedEdge> dij = new DijkstraShortestPath<>(g);
        GraphPath<Coordinate, MyDefaultWeightedEdge> path = dij.getPath(new Coordinate(0, 0), new Coordinate(499, 499));
        System.out.println("3rd party: " + path.getWeight());
    }

    private static void printCoords(int height, int width, Map<Coordinate, Coordinate> coords, Set<Coordinate> minPath) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                var coord = coords.get(new Coordinate(row, col));
                if (minPath.contains(coord)) {
                    System.out.print("\u001B[31m");
                }
                System.out.print(coord.val);
                if (minPath.contains(coord)) {
                    System.out.print("\u001B[0m");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    static class MyDefaultWeightedEdge extends DefaultWeightedEdge {
        @Override
        public Object getSource() {
            return super.getSource();
        }
    }
}
