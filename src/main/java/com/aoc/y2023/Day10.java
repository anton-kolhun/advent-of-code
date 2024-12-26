package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.CoordEdge;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day10 {

    public static void main(String[] args) {
        task1();
    }

    public static Set<Coordinate> task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day10.txt");
        List<CoordEdge> edges = new ArrayList<>();
        Coordinate start = new Coordinate();

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                parseCell(edges, row, col, c, start);
            }
        }

        Map<Coordinate, List<CoordEdge>> fromEdges = edges.stream().collect(Collectors.groupingBy(edge -> edge.from));

        List<CoordEdge> edgesFromStart = new ArrayList<>();
        if (fromEdges.getOrDefault(new Coordinate(start.row + 1, start.col), new ArrayList<>()).stream().map(coordEdge -> coordEdge.to).toList().contains(start)) {
            edgesFromStart.add(new CoordEdge(start, new Coordinate(start.row + 1, start.col)));
        }
        if (fromEdges.getOrDefault(new Coordinate(start.row - 1, start.col), new ArrayList<>()).stream().map(coordEdge -> coordEdge.to).toList().contains(start)) {
            edgesFromStart.add(new CoordEdge(start, new Coordinate(start.row - 1, start.col)));
        }
        if (fromEdges.getOrDefault(new Coordinate(start.row, start.col + 1), new ArrayList<>()).stream().map(coordEdge -> coordEdge.to).toList().contains(start)) {
            edgesFromStart.add(new CoordEdge(start, new Coordinate(start.row, start.col + 1)));
        }
        if (fromEdges.getOrDefault(new Coordinate(start.row, start.col - 1), new ArrayList<>()).stream().map(coordEdge -> coordEdge.to).toList().contains(start)) {
            edgesFromStart.add(new CoordEdge(start, new Coordinate(start.row, start.col - 1)));
        }
        fromEdges.put(start, edgesFromStart);

        List<Coordinate> path = dfs(start, fromEdges);
        return new HashSet<>(path);
    }


    public static List<Coordinate> dfs(Coordinate start, Map<Coordinate, List<CoordEdge>> fromEdges) {
        Set<Coordinate> visited = new HashSet<>();
        visited.add(start);

        Coordinate cursor = start;
        List<Coordinate> path = new ArrayList<>();
        path.add(start);
        cursor = fromEdges.get(cursor).get(0).to;
        path.add(cursor);
        Coordinate previous = start;
        while (!cursor.equals(start)) {
            if (previous.equals(fromEdges.get(cursor).get(0).to)) {
                previous = cursor;
                cursor = fromEdges.get(cursor).get(1).to;
            } else {
                previous = cursor;
                cursor = fromEdges.get(cursor).get(0).to;
            }
            path.add(cursor);
        }
        return path;
    }

    private static Coordinate parseCell(List<CoordEdge> edges, int row, int col, char c, Coordinate start) {
        if (c == '|') {
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row + 1, col)));
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row - 1, col)));
        } else if (c == '-') {
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row, col + 1)));
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row, col - 1)));
        } else if (c == 'L') {
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row, col + 1)));
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row - 1, col)));
        } else if (c == 'J') {
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row - 1, col)));
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row, col - 1)));
        } else if (c == '7') {
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row + 1, col)));
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row, col - 1)));
        } else if (c == 'F') {
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row + 1, col)));
            edges.add(new CoordEdge(new Coordinate(row, col), new Coordinate(row, col + 1)));
        } else if (c == 'S') {
            start.row = row;
            start.col = col;
        }
        return start;
    }
}
