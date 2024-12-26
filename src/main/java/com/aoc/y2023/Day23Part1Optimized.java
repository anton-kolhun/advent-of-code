package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.CoordEdge;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.GraphUtils.Direction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23Part1Optimized {


    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
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

        Set<Coordinate> multipleChoicePoints = findMultipleChoicePoints(lines, coords);

        var start = new Coordinate(0, 1);
        var finish = new Coordinate(lines.size() - 1, lines.get(0).length() - 2);
        List<PathInfo> paths = new ArrayList<>();
        var startInfo = new ArrayList<Coordinate>();
        startInfo.add(start);
        paths.add(new PathInfo(startInfo));
        Set<Coordinate> visited = new HashSet<>();
        visited.add(start);

        List<CoordEdge> edges = populateEdges(coords, multipleChoicePoints, finish, paths, visited);

        Map<Coordinate, List<CoordEdge>> fromEdges = edges.stream()
                .collect(Collectors.groupingBy(coordEdge -> coordEdge.from));

        for (Map.Entry<Coordinate, List<CoordEdge>> entry : fromEdges.entrySet()) {
            var unique = new HashSet<>(entry.getValue());
            fromEdges.put(entry.getKey(), new ArrayList<>(unique));
        }


        List<ContractedPath> cursors = new ArrayList<>();
        for (CoordEdge coordEdge : fromEdges.get(start)) {
            var path = new ContractedPath();
            path.edges = List.of(coordEdge);
            path.allCoords.addAll(Set.of(coordEdge.from, coordEdge.to));
            cursors.add(path);
        }
        List<List<CoordEdge>> total = getAllPaths(finish, fromEdges, cursors);

        long maxDist = Integer.MIN_VALUE;
        for (List<CoordEdge> coordEdges : total) {
            long dist = coordEdges.stream().mapToInt(coordEdge -> coordEdge.weight - 1).sum();
            if (dist > maxDist) {
                maxDist = dist;
            }
        }
        System.out.println(maxDist);


        //findPath(coords, new Coordinate(0, 1), new Coordinate(lines.size() - 1, lines.get(0).length() - 2), lines);


    }

    private static List<List<CoordEdge>> getAllPaths(Coordinate finish, Map<Coordinate, List<CoordEdge>> fromEdges, List<ContractedPath> cursors) {
        List<List<CoordEdge>> total = new ArrayList<>();
        long maxDist = Integer.MIN_VALUE;
        while (!cursors.isEmpty()) {
            var curs = cursors.remove(0);
            var lastCoord = curs.edges.get(curs.edges.size() - 1).to;

            // seen.add(lastCoord);
            if (lastCoord.equals(finish)) {
                total.add(curs.edges);
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
        return total;
    }

    private static List<CoordEdge> populateEdges(Map<Coordinate, Character> coords, Set<Coordinate> multipleChoicePoints, Coordinate finish, List<PathInfo> paths, Set<Coordinate> visited) {
        List<CoordEdge> edges = new ArrayList<>();
        while (!paths.isEmpty()) {
            var path = paths.remove(0);
            var currentCoord = path.coords.get(path.coords.size() - 1);
            var startCoord = path.coords.get(0);
            if (currentCoord.equals(finish)) {
                edges.add(new CoordEdge(path.coords.get(0), currentCoord, path.coords.size()));
                continue;
            }
            if (multipleChoicePoints.contains(currentCoord) && (!currentCoord.equals(startCoord))) {
                edges.add(new CoordEdge(startCoord, currentCoord, path.coords.size()));
                if (!visited.contains(currentCoord)) {
                    visited.add(currentCoord);
                    List<Coordinate> newPath = new ArrayList<>();
                    newPath.add(currentCoord);
                    var newPathInfo = new PathInfo(newPath);
                    paths.add(newPathInfo);
                }
            } else {
                var nextCoords = getNextCoord(currentCoord, coords, path);
                for (Coordinate nextCoord : nextCoords) {
                    var nextPath = new ArrayList<>(path.coords);
                    nextPath.add(nextCoord);
                    var nextPathInfo = new PathInfo(nextPath);
                    paths.add(nextPathInfo);
                }
            }
        }
        return edges;
    }

    private static Set<Coordinate> findMultipleChoicePoints(List<String> lines, Map<Coordinate, Character> coords) {
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
                if ((symbol != '.')) {
                    continue;
                }
                visited.add(coord);
                int neighbours = 0;
                for (Direction dir : Direction.values()) {
                    var moved = new Coordinate(coord.row + dir.shift.row, coord.col + dir.shift.col);
                    var symb = coords.getOrDefault(moved, 'X');
                    if (symb == '#') {
                        continue;
                    }
                    neighbours++;
                }
                if (neighbours > 2) {
                    multipleChoicePoints.add(coord);
                }
            }
        }
        return multipleChoicePoints;
    }

    private static List<Coordinate> getNextCoord(Coordinate current, Map<Coordinate, Character> coords, PathInfo path) {
        List<Coordinate> nextCoords = new ArrayList<>();
        List<Direction> directions = new ArrayList<>();
        Character symb = coords.get(current);
        if (symb == '>') {
            directions.add(Direction.RIGHT);
        } else if (symb == '<') {
            directions.add(Direction.LEFT);
        } else if (symb == '^') {
            directions.add(Direction.UP);
        } else if (symb == 'v') {
            directions.add(Direction.DOWN);
        } else if (symb == '.') {
            directions.addAll(Arrays.asList(Direction.values()));
        }

        for (Direction dir : directions) {
            var next = new Coordinate(current.row + dir.shift.row, current.col + dir.shift.col);
            var symbol = coords.getOrDefault(next, 'X');
            if (!path.coords.contains(next) && (
                    (symbol == '.') || (symbol == '<') || (symbol == '>') || (symbol == 'v') || (symbol == '^'))) {
                nextCoords.add(next);
            }
        }
        return nextCoords;
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
