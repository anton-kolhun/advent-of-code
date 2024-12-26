package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils;
import com.aoc.y2023.helper.GraphUtils.CoordEdge;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day20 {

    static List<Coordinate> moves = List.of(
            new Coordinate(0, 1),
            new Coordinate(0, -1),
            new Coordinate(1, 0),
            new Coordinate(-1, 0)
    );

    public static void main(String[] args) {
        task1And2();
    }


    public static void task1And2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day20.txt");
        Map<Coordinate, Character> coords = new HashMap();
        Coordinate start = null;
        Coordinate end = null;
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coords.put(new Coordinate(row, col), c);
                if (c == 'S') {
                    start = new Coordinate(row, col);
                } else if (c == 'E') {
                    end = new Coordinate(row, col);
                }

            }
        }
        List<CoordEdge> edges = calcEdges(coords);
        List<Coordinate> path = GraphUtils.bfs(edges, start, end);

        Map<Coordinate, Integer> distFromStart = new HashMap<>();
        for (int i = 0; i < path.size(); i++) {
            distFromStart.put(path.get(i), i);
        }
        calcCheats(path, coords, distFromStart, 2);
        calcCheats(path, coords, distFromStart, 20);
    }

    private static void calcCheats(List<Coordinate> path, Map<Coordinate, Character> coords,
                                   Map<Coordinate, Integer> distFromStart, int cheatTime) {
        int saveTime = 100;
        Set<List<Coordinate>> pairs = new HashSet<>();
        for (int i = 0; i < path.size() - 1; i++) {
            Coordinate coord1 = path.get(i);
            for (int row = -cheatTime * 2; row <= cheatTime * 2; row++) {
                for (int col = -cheatTime * 2; col <= cheatTime * 2; col++) {
                    Coordinate coord2 = new Coordinate(coord1.row + row, coord1.col + col);
                    if (coords.getOrDefault(coord2, '#') == '#') {
                        continue;
                    }

                    int actualDelta = distFromStart.get(coord2) - distFromStart.get(coord1);
                    if (actualDelta < 0) {
                        continue;
                    }
                    int manhDist = Math.abs(coord2.row - coord1.row) + Math.abs(coord2.col - coord1.col);
                    if (manhDist > cheatTime) {
                        continue;
                    }
                    int saved = actualDelta - manhDist;
                    if (saved >= saveTime) {
                        pairs.add(List.of(coord1, coord2));
                    }
                }
            }
        }
        System.out.println(pairs.size());
    }

    private static List<CoordEdge> calcEdges(Map<Coordinate, Character> coords) {
        List<CoordEdge> edges = new ArrayList<>();
        for (Map.Entry<Coordinate, Character> entry : coords.entrySet()) {
            if (entry.getValue() == '.' || entry.getValue() == 'S' || entry.getValue() == 'E') {
                for (Coordinate move : moves) {
                    var neib = new Coordinate(entry.getKey().row + move.row, entry.getKey().col + move.col);
                    var sign = coords.getOrDefault(neib, 'X');
                    if (sign == '.' || sign == 'S' || sign == 'E') {
                        edges.add(new CoordEdge(entry.getKey(), neib));
                    }
                }

            }
        }
        return edges;
    }

}