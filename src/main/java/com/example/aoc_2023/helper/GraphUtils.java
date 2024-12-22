package com.example.aoc_2023.helper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphUtils {

    public static void main(String[] args) {
        Edge e1 = new Edge("1", "2");
        Edge e2 = new Edge("2", "3");
        Edge e21 = new Edge("2", "4");
        Edge e3 = new Edge("3", "4");
        var edges = Arrays.asList(e1, e2, e3, e21);
        List<String> res = dfs(edges, "1", "4");
        System.out.println(res);

        System.out.println(bfs(edges, "1", "4"));

        System.out.println(dejkstra(edges, "1", "4"));
    }


    public static List<String> dejkstra(List<Edge> edges, String start, String end) {
        Map<String, List<Edge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));

        Set<String> visited = new HashSet<>();
        Map<String, List<String>> coordToDistance = new HashMap<>();
        List<String> initial = new ArrayList<>();
        initial.add(start);
        coordToDistance.put(start, initial);
        PriorityQueue<Distance> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.distance));
        priorityQueue.add(new Distance(start, 0));
        boolean notFound = true;
        while (notFound && !priorityQueue.isEmpty()) {
            String coordMin = findCoord(priorityQueue, visited);
            List<String> minPath = coordToDistance.getOrDefault(coordMin, new ArrayList<>());
            visited.add(coordMin);
            List<Edge> fromEdgs = fromEdges.getOrDefault(coordMin, new ArrayList<>());
            for (Edge fromEdge : fromEdgs) {
                var pathTo = coordToDistance.get(fromEdge.to);
                var pathTosize = Integer.MAX_VALUE;
                if (pathTo != null) {
                    pathTosize = pathTo.size();
                }
                if (pathTosize > minPath.size() + 1) {
                    var newPath = new ArrayList<>(minPath);
                    newPath.add(fromEdge.to);
                    coordToDistance.put(fromEdge.to, newPath);
                    priorityQueue.add(new Distance(fromEdge.to, newPath.size()));
                }
                if (fromEdge.to.equals(end)) {
                    notFound = false;
                }

            }
        }
        return coordToDistance.get(end);

    }

    public static List<String> bfs(List<Edge> edges, String start, String end) {
        Map<String, List<Edge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));
        Set<String> visited = new HashSet<>();
        List<List<String>> paths = new ArrayList<>();
        List<String> startList = Arrays.asList(start);
        paths.add(startList);
        while (!paths.isEmpty()) {
            List<String> currentPath = paths.remove(0);
            var s = currentPath.get(currentPath.size() - 1);
            if (s.equals(end)) {
                return currentPath;
            }
            for (Edge edge : fromEdges.getOrDefault(s, new ArrayList<>())) {
                if (!visited.contains(edge.to)) {
                    List<String> nextPath = new ArrayList<>(currentPath);
                    nextPath.add(edge.to);
                    visited.add(edge.to);
                    paths.add(nextPath);
                }
            }
        }
        return Collections.emptyList();
    }

    public static List<Coordinate> bfs(List<CoordEdge> edges, Coordinate start, Coordinate end) {
        Map<Coordinate, List<CoordEdge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));
        Set<Coordinate> visited = new HashSet<>();
        List<List<Coordinate>> paths = new ArrayList<>();
        List<Coordinate> startList = Arrays.asList(start);
        paths.add(startList);
        while (!paths.isEmpty()) {
            List<Coordinate> currentPath = paths.remove(0);
            var s = currentPath.get(currentPath.size() - 1);
            if (s.equals(end)) {
                return currentPath;
            }
            for (CoordEdge edge : fromEdges.getOrDefault(s, new ArrayList<>())) {
                if (!visited.contains(edge.to)) {
                    List<Coordinate> nextPath = new ArrayList<>(currentPath);
                    nextPath.add(edge.to);
                    visited.add(edge.to);
                    paths.add(nextPath);
                }
            }
        }
        return Collections.emptyList();
    }


    public static List<String> dfs(List<Edge> edges, String start, String end) {
        Map<String, List<Edge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));
        List<String> path = new ArrayList<>();
        path.add(start);
        return doDfs(fromEdges, start, end, path, new HashSet<>());
    }

    public static List<String> doDfs(Map<String, List<Edge>> fromEdges, String cursor, String end, List<String> currentPath,
                                     Set<String> visited) {
        if (cursor.equals(end)) {
            return currentPath;
        }
        List<Edge> nodes = fromEdges.getOrDefault(cursor, new ArrayList<>());
        for (Edge node : nodes) {
            if (!visited.contains(node.to)) {
                List<String> nextPath = new ArrayList<>(currentPath);
                nextPath.add(node.to);
                visited.add(node.to);
                var res = doDfs(fromEdges, node.to, end, nextPath, visited);
                if (!res.isEmpty()) {
                    return res;
                }
            }
        }
        return Collections.emptyList();
    }

    private static String findCoord(PriorityQueue<Distance> distances, Set<String> visited) {
        if (distances.isEmpty()) {
            return null;
        }
        String coord = distances.poll().point;
        while (visited.contains(coord)) {
            coord = distances.poll().point;
        }
        return coord;
    }


    @AllArgsConstructor
    @ToString
    public static class Edge {
        public String from;
        public String to;
        public int weight;

        public Edge() {
        }

        public Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (!Objects.equals(from, edge.from)) return false;
            return Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }
    }

    private static class Distance {
        private String point;
        private int distance;

        public Distance(String point, int distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    @AllArgsConstructor
    public static class CoordEdge {
        public Coordinate from;
        public Coordinate to;
        public int weight;

        public CoordEdge() {
        }

        public CoordEdge(Coordinate from, Coordinate to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoordEdge edge = (CoordEdge) o;

            if (!Objects.equals(from, edge.from)) return false;
            return Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class Coordinate {
        public int row;
        public int col;
        public int val;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "Coordinate{" +
                    "row=" + row +
                    ", col=" + col +
                    ", val=" + val +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;

            if (row != that.row) return false;
            return col == that.col;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            return result;
        }
    }


    public static List<Coordinate> dejkstra(List<CoordEdge> edges, Coordinate start, Coordinate end) {
        Map<Coordinate, List<CoordEdge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));

        Set<Coordinate> visited = new HashSet<>();
        Map<Coordinate, List<Coordinate>> coordToDistance = new HashMap<>();
        List<Coordinate> initial = new ArrayList<>();
        initial.add(start);
        coordToDistance.put(start, initial);
        PriorityQueue<DistanceCoord> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.distance));
        priorityQueue.add(new DistanceCoord(start, 0));
        boolean notFound = true;
        while (notFound && !priorityQueue.isEmpty()) {
            Coordinate coordMin = findDistCoord(priorityQueue, visited);
            List<Coordinate> minPath = coordToDistance.getOrDefault(coordMin, new ArrayList<>());
            visited.add(coordMin);
            List<CoordEdge> fromEdgs = fromEdges.getOrDefault(coordMin, new ArrayList<>());
            for (CoordEdge fromEdge : fromEdgs) {
                var pathTo = coordToDistance.get(fromEdge.to);
                var pathTosize = Integer.MAX_VALUE;
                if (pathTo != null) {
                    pathTosize = pathTo.size();
                }
                if (pathTosize > minPath.size() + 1) {
                    var newPath = new ArrayList<>(minPath);
                    newPath.add(fromEdge.to);
                    coordToDistance.put(fromEdge.to, newPath);
                    priorityQueue.add(new DistanceCoord(fromEdge.to, newPath.size()));
                }
                if (fromEdge.to.equals(end)) {
                    notFound = false;
                }

            }
        }
        return coordToDistance.get(end);

    }


    public static List<List<Coordinate>> dejkstraMultiple(List<CoordEdge> edges, Coordinate start, Coordinate end) {
        Map<Coordinate, List<CoordEdge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(edge -> edge.from));

        Set<Coordinate> visited = new HashSet<>();
        Map<Coordinate, List<List<Coordinate>>> coordToDistance = new HashMap<>();
        List<Coordinate> initial = new ArrayList<>();
        initial.add(start);
        coordToDistance.put(start, List.of(initial));
        PriorityQueue<DistanceCoord> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.distance));
        priorityQueue.add(new DistanceCoord(start, 0));
        boolean notFound = true;
        while (!priorityQueue.isEmpty()) {
            Coordinate coordMin = findDistCoord(priorityQueue, visited);
            if (coordMin.equals(end)) {
                break;
            }
            List<List<Coordinate>> minPaths = coordToDistance.getOrDefault(coordMin, new ArrayList<>());
            visited.add(coordMin);
            List<CoordEdge> fromEdgs = fromEdges.getOrDefault(coordMin, new ArrayList<>());
            for (CoordEdge fromEdge : fromEdgs) {
                var pathTo = coordToDistance.get(fromEdge.to);
                var pathTosize = Integer.MAX_VALUE;
                if (pathTo != null) {
                    pathTosize = pathTo.get(0).size();
                }
                int minPathSize = minPaths.get(0).size();
                if (pathTosize > minPathSize + 1) {
                    List<List<Coordinate>> newPaths = new ArrayList<>();
                    for (List<Coordinate> minPath : minPaths) {
                        var newPath = new ArrayList<>(minPath);
                        newPath.add(fromEdge.to);
                        newPaths.add(newPath);
                    }
                    coordToDistance.put(fromEdge.to, newPaths);
                    priorityQueue.add(new DistanceCoord(fromEdge.to, newPaths.get(0).size()));
                }

                if (pathTosize == minPathSize + 1) {
                    List<List<Coordinate>> newPaths = new ArrayList<>();
                    for (List<Coordinate> minPath : minPaths) {
                        var newPath = new ArrayList<>(minPath);
                        newPath.add(fromEdge.to);
                        newPaths.add(newPath);
                    }
                    List<List<Coordinate>> updated = new ArrayList<>(coordToDistance.get(fromEdge.to));
                    updated.addAll(newPaths);
                    coordToDistance.put(fromEdge.to, updated);
                    priorityQueue.add(new DistanceCoord(fromEdge.to, newPaths.get(0).size()));
                }


                if (fromEdge.to.equals(end)) {
                    notFound = false;
                }

            }
        }
        return coordToDistance.get(end);

    }


    private static class DistanceCoord {
        private Coordinate point;
        private int distance;

        public DistanceCoord(Coordinate point, int distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    private static Coordinate findDistCoord(PriorityQueue<DistanceCoord> distances, Set<Coordinate> visited) {
        if (distances.isEmpty()) {
            return null;
        }
        Coordinate coord = distances.poll().point;
        while (visited.contains(coord)) {
            coord = distances.poll().point;
        }
        return coord;
    }


    public static enum Direction {
        RIGHT(new Coordinate(0, 1)), LEFT(new Coordinate(0, -1)), DOWN(new Coordinate(1, 0)), UP(new Coordinate(-1, 0));

        public Coordinate getShift() {
            return shift;
        }

        public Coordinate shift;

        private Direction(Coordinate coord) {
            this.shift = coord;
        }

    }


}
