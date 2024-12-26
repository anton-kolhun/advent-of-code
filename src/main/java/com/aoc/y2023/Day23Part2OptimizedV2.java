package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.CoordEdge;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.GraphUtils.Direction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23Part2OptimizedV2 {


    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day23.txt");
        Map<Coordinate, Character> coords = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coords.put(new Coordinate(row, col), c);
            }
        }

        Set<Coordinate> multipleChoicePoints = new HashSet<>();
        Set<Coordinate> visited = new HashSet<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                var coord = new Coordinate(row, col);
                if (visited.contains(coord)) {
                    continue;
                }
                var symbol = coords.getOrDefault(coord, 'X');
                if ((symbol != '.') && (symbol != '<') && (symbol != '>') && (symbol != 'v') && (symbol != '^')) {
                    continue;
                }
                visited.add(coord);
                int neighbours = 0;
                for (Direction dir : Direction.values()) {
                    var moved = new Coordinate(coord.row + dir.shift.row, coord.col + dir.shift.col);
                    var symb = coords.getOrDefault(moved, 'X');
                    if ((symb != '.') && (symb != '<') && (symb != '>') && (symb != 'v') && (symb != '^')) {
                        continue;
                    }
                    neighbours++;
                }
                if (neighbours > 2) {
                    multipleChoicePoints.add(coord);
                }
            }
        }

        List<CoordEdge> edges = new ArrayList<>();
        var start = new Coordinate(0, 1);
        var finish = new Coordinate(lines.size() - 1, lines.get(0).length() - 2);
        List<PathInfo> paths = new ArrayList<>();
        var cursor = start;
        var startInfo = new ArrayList<Coordinate>();
        startInfo.add(start);
        paths.add(new PathInfo(startInfo));
        visited = new HashSet<>();
        visited.add(cursor);
        while (!paths.isEmpty()) {
            var path = paths.remove(0);
            var currentCoord = path.coords.get(path.coords.size() - 1);
            var startCoord = path.coords.get(0);
            if (currentCoord.equals(finish)) {
                edges.add(new CoordEdge(path.coords.get(0), currentCoord, path.coords.size()));
                edges.add(new CoordEdge(currentCoord, path.coords.get(0), path.coords.size()));
                continue;
            }
            //visited.add(path.current);
            if (multipleChoicePoints.contains(currentCoord) && (!currentCoord.equals(startCoord))) {
                edges.add(new CoordEdge(startCoord, currentCoord, path.coords.size()));
                if (!startCoord.equals(start)) {
                    edges.add(new CoordEdge(currentCoord, startCoord, path.coords.size()));
                }
                if (!visited.contains(currentCoord)) {
                    visited.add(currentCoord);
                    List<Coordinate> newPath = new ArrayList<>();
                    newPath.add(currentCoord);
                    var newPathInfo = new PathInfo(newPath);
                    paths.add(newPathInfo);
                }
            } else {
                var nextCoords = getNextCoord(visited, currentCoord, coords, path);
                for (Coordinate nextCoord : nextCoords) {
                    var nextPath = new ArrayList<>(path.coords);
                    nextPath.add(nextCoord);
                    var nextPathInfo = new PathInfo(nextPath);
                    paths.add(nextPathInfo);
                }
            }
        }

        Map<Coordinate, List<CoordEdge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(coordEdge -> coordEdge.from));

        for (Map.Entry<Coordinate, List<CoordEdge>> entry : fromEdges.entrySet()) {
            var unique = new HashSet<>(entry.getValue());
            fromEdges.put(entry.getKey(), new ArrayList<>(unique));
        }

        Map<Coordinate, List<CoordEdge>> toEdges = edges.stream()
                .collect(Collectors.groupingBy(coordEdge -> coordEdge.to));
        for (Map.Entry<Coordinate, List<CoordEdge>> entry : toEdges.entrySet()) {
            var unique = new HashSet<>(entry.getValue());
            toEdges.put(entry.getKey(), new ArrayList<>(unique));
        }

        List<CoordEdge> updatedEdges = new ArrayList<>(edges);
        boolean shouldContinue = true;
        while (shouldContinue) {
            shouldContinue = false;
            for (Map.Entry<Coordinate, List<CoordEdge>> entry : toEdges.entrySet()) {
                if (entry.getValue().size() <= 2 && !entry.getKey().equals(finish) && !entry.getKey().equals(start) ) {
                    for (CoordEdge fromOneToRemove : entry.getValue()) {
                        for (CoordEdge coordEdge : toEdges.get(fromOneToRemove.from)) {
                            CoordEdge edge = new CoordEdge(coordEdge.from, coordEdge.to, coordEdge.weight + fromOneToRemove.weight);
                            if (!edge.from.equals(entry.getKey())) {
                                edge.to = entry.getKey();
                                updatedEdges.add(edge);
                                updatedEdges.add(new CoordEdge(edge.to, edge.from));
                                updatedEdges.remove(coordEdge);
                                updatedEdges.remove(new CoordEdge(coordEdge.to, coordEdge.from));
                            }
                        }
                    }
                    for (CoordEdge fromOneToRemove : entry.getValue()) {
                        updatedEdges.remove(fromOneToRemove);
                        updatedEdges.remove(new CoordEdge(fromOneToRemove.to, fromOneToRemove.from));
                    }
                    toEdges = updatedEdges.stream()
                            .collect(Collectors.groupingBy(coordEdge -> coordEdge.to));
                    fromEdges = updatedEdges.stream()
                            .collect(Collectors.groupingBy(coordEdge -> coordEdge.from));
                    shouldContinue = true;
                }
            }
        }

        List<ContractedPath> cursors = new ArrayList<>();
        for (CoordEdge coordEdge : fromEdges.get(start)) {
            var path = new ContractedPath();
            path.edges = List.of(coordEdge);
            path.allCoords.addAll(Set.of(coordEdge.from, coordEdge.to));
            cursors.add(path);
        }


        //Set<Coordinate> seen = new HashSet<>();
        long maxDist = Integer.MIN_VALUE;
        List<List<CoordEdge>> total = new ArrayList<>();
        while (!cursors.isEmpty()) {
            var curs = cursors.remove(0);
            var lastCoord = curs.edges.get(curs.edges.size() - 1).to;

            // seen.add(lastCoord);
            if (lastCoord.equals(finish)) {
                //total.add(curs.edges);
                long dist = curs.edges.stream().mapToInt(coordEdge -> coordEdge.weight - 1).sum();
                if (dist > maxDist) {
                    System.out.println(dist);
                    maxDist = dist;
                }
            }
            var edgs = fromEdges.getOrDefault(lastCoord, new ArrayList<>());
            for (CoordEdge edg : edgs) {
                if (!curs.allCoords.contains(edg.to)) {
                    var nextPath = new ArrayList<>(curs.edges);
                    nextPath.add(edg);
                    var nextCoords = new HashSet<>(curs.allCoords);
                    nextCoords.addAll(Set.of(edg.from, edg.to));
                    cursors.add(new ContractedPath(nextPath, nextCoords));
                }
            }

        }

        maxDist = Integer.MIN_VALUE;
        for (List<CoordEdge> coordEdges : total) {
            long dist = coordEdges.stream().mapToInt(coordEdge -> coordEdge.weight - 1).sum();
            if (dist > maxDist) {
                maxDist = dist;
            }
        }
        System.out.println(maxDist);


        //findPath(coords, new Coordinate(0, 1), new Coordinate(lines.size() - 1, lines.get(0).length() - 2), lines);


    }

    private static List<Coordinate> getNextCoord(Set<Coordinate> visited, Coordinate current, Map<Coordinate, Character> coords, PathInfo path) {
        List<Coordinate> nextCoords = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            var next = new Coordinate(current.row + dir.shift.row, current.col + dir.shift.col);
            var symbol = coords.getOrDefault(next, 'X');
            if (!path.coords.contains(next) && (
                    (symbol == '.') || (symbol == '<') || (symbol == '>') || (symbol == 'v') || (symbol == '^'))) {
                nextCoords.add(next);
            }
        }
        return nextCoords;
    }


    private static void print(List<Coordinate> cursor, Map<Coordinate, Character> coords, List<String> lines) {
        var cursSet = new HashSet<>(cursor);
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(0).length(); col++) {
                if (cursSet.contains(new Coordinate(row, col))) {
                    System.out.print("0");
                } else {
                    System.out.print(coords.get(new Coordinate(row, col)));
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    @AllArgsConstructor
    @NoArgsConstructor
    private static class PathInfo {
        private List<Coordinate> coords = new ArrayList<>();
    }


    @AllArgsConstructor
    @NoArgsConstructor
    private static class ContractedPath {
        List<CoordEdge> edges = new ArrayList<>();
        Set<Coordinate> allCoords = new HashSet<>();
    }

}
