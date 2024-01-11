package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.CoordEdge;
import com.example.aoc_2023.helper.GraphUtils.Coordinate;
import com.example.aoc_2023.helper.GraphUtils.Direction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23Part2Optimized {


    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("day23.txt");
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

    private static void findPath(Map<Coordinate, Character> coords, Coordinate start, Coordinate end, List<String> lines) {
        List<List<Coordinate>> paths = doFindPath(coords, end, List.of(start), new HashMap<>(), lines);
        int maxPathSize = Integer.MIN_VALUE;
        for (List<Coordinate> path : paths) {
            if (path.size() > maxPathSize) {
                maxPathSize = path.size();
            }
        }
        System.out.println(maxPathSize - 1);

    }

    private static List<List<Coordinate>> doFindPath(Map<Coordinate, Character> coords, Coordinate end,
                                                     List<Coordinate> currentPath, Map<Coordinate, List<List<Coordinate>>> cache, List<String> lines) {
        var cursor = currentPath.get(currentPath.size() - 1);
        if (cursor.equals(end)) {
            //print(currentPath, coords, lines);
            System.out.println(currentPath.size() - 1);
            return List.of(currentPath);
        }
        if (coords.getOrDefault(cursor, 'x') == '#') {
            return Collections.emptyList();
        }
        if (cursor.row < 0 || cursor.row >= lines.size() || cursor.col < 0 || cursor.col >= lines.get(0).length()) {
            return Collections.emptyList();
        }
        if (new HashSet<>(currentPath).size() < currentPath.size()) {
            return Collections.emptyList();
        }
        //print(currentPath, coords, lines);
        List<List<Coordinate>> total = new ArrayList<>();


        var res = checkDir(coords, end, currentPath, cache, lines, cursor, Direction.UP.shift);
        total.addAll(res);

        res = checkDir(coords, end, currentPath, cache, lines, cursor, Direction.LEFT.shift);
        total.addAll(res);

        res = checkDir(coords, end, currentPath, cache, lines, cursor, Direction.RIGHT.shift);
        total.addAll(res);

        res = checkDir(coords, end, currentPath, cache, lines, cursor, Direction.DOWN.shift);
        total.addAll(res);


        return total;
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

    private static List<List<Coordinate>> checkDir(Map<Coordinate, Character> coords, Coordinate end, List<Coordinate> currentPath, Map<Coordinate,
            List<List<Coordinate>>> cache, List<String> lines, Coordinate cursor, Coordinate move) {
        ArrayList<Coordinate> nextPath;
        var nextCoord = new Coordinate(cursor.row + move.row, cursor.col + move.col);
        nextPath = new ArrayList<>(currentPath);
        nextPath.add(nextCoord);
        List<List<Coordinate>> total = new ArrayList<>();
        List<List<Coordinate>> cached = cache.get(nextCoord);
        if (cached != null) {
            for (List<Coordinate> tail : cached) {
                var res = new ArrayList<>(nextPath);
                res.addAll(tail);
                if (new HashSet<>(res).size() == res.size()) {
                    total.add(res);
                }
            }
        } else {
            var result = doFindPath(coords, end, nextPath, cache, lines);
            var filtered = result.stream().filter(res -> !res.isEmpty()).collect(Collectors.toList());
            List<List<Coordinate>> tails = new ArrayList<>();
            for (List<Coordinate> res : filtered) {
                var tail = res.subList(nextPath.size(), res.size());
                tails.add(tail);
            }
            if (!tails.isEmpty()) {
                //cache.put(nextCoord, tails);
            }
            total.addAll(result);
        }
        return total;
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
