package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class Day17Part2 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day17.txt");
        Map<Coordinate, Integer> coords = new HashMap();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                int weight = Character.getNumericValue(charArray[col]);
                coords.put(new Coordinate(row, col), weight);
            }
        }
        int minPath = dejkstraUpdated(coords, new Coordinate(0, 0), new Coordinate(lines.size() - 1, lines.get(0).length() - 1), lines);
        System.out.println(minPath - coords.get(new Coordinate(0, 0)));


    }

    private static int dejkstraUpdated(Map<Coordinate, Integer> coords, Coordinate start, Coordinate end, List<String> lines) {
        Set<CoordInfo> visited = new HashSet<>();
        Map<CoordInfo, List<CoordInfo>> coordToDistance = new HashMap<>();
        List<CoordInfo> initial = new ArrayList<>();
        var startInfo = new CoordInfo(start, Direction.STALL, 0);
        initial.add(startInfo);
        coordToDistance.put(startInfo, initial);
        PriorityQueue<DistanceCoord> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.distance));
        priorityQueue.add(new DistanceCoord(startInfo, 0));
        while (!priorityQueue.isEmpty()) {
            CoordInfo coordMin = findDistCoord(priorityQueue, visited);
            List<CoordInfo> minPath = coordToDistance.getOrDefault(coordMin, new ArrayList<>());
            visited.add(coordMin);
            List<CoordInfo> nextCoords = getAllowedNeighbours(coordMin, coords, lines);
            for (CoordInfo nextCoord : nextCoords) {
                var pathToNext = coordToDistance.get(nextCoord);
                var pathToNextSize = Integer.MAX_VALUE;
                if (pathToNext != null) {
                    pathToNextSize = getScore(pathToNext, coords);
                }
                int possiblePathSize = getScore(minPath, coords) + coords.get(nextCoord.coord);
                if (pathToNextSize > possiblePathSize) {
                    var newPathToNext = new ArrayList<>(minPath);
                    newPathToNext.add(nextCoord);
                    coordToDistance.put(nextCoord, newPathToNext);
                    priorityQueue.add(new DistanceCoord(nextCoord, possiblePathSize));
                }
                if (nextCoord.coord.equals(end)) {
                    //notFound = false;
                }

            }
        }
        int min = Integer.MAX_VALUE;
        for (Map.Entry<CoordInfo, List<CoordInfo>> entry : coordToDistance.entrySet()) {
            if (entry.getKey().coord.equals(end)) {
                Direction lastDir = entry.getValue().get(entry.getValue().size() - 1).dir;
                boolean isMatch = true;
                for (int i = 1; i < 4; i++) {
                    Direction dir = entry.getValue().get(entry.getValue().size() - 1 - i).dir;
                    if (dir != lastDir) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    int dist = getScore(entry.getValue(), coords);
                    if (dist < min) {
                        min = dist;
                    }
                }
            }
        }

        return min;
    }

    private static List<CoordInfo> getAllowedNeighbours(CoordInfo coordMin, Map<Coordinate, Integer> coords, List<String> lines) {
        List<CoordInfo> next = new ArrayList<>();
        for (Direction dir : coordMin.dir.getAllowed()) {
            var nextCoord = new Coordinate(coordMin.coord.row + dir.shift.row, coordMin.coord.col + dir.shift.col);
            var nextSameMoveCounter = 1;

            if (!dir.equals(coordMin.dir) && coordMin.dir != Direction.STALL) {
                if (coordMin.sameMovesCounter < 4) {
                    continue;
                }
            }
            if (dir.equals(coordMin.dir)) {
                nextSameMoveCounter = coordMin.sameMovesCounter + 1;
                if (nextSameMoveCounter > 10) {
                    continue;
                }
            }
            if (nextCoord.row < 0 || nextCoord.row >= lines.size() ||
                    nextCoord.col < 0 || nextCoord.col >= lines.get(0).length()) {
                continue;
            }
            next.add(new CoordInfo(nextCoord, dir, nextSameMoveCounter));
        }
        return next;
    }

    private static CoordInfo findDistCoord(PriorityQueue<DistanceCoord> distances, Set<CoordInfo> visited) {
        if (distances.isEmpty()) {
            return null;
        }
        CoordInfo coord = distances.poll().coordInfo;
        while (visited.contains(coord)) {
            coord = distances.poll().coordInfo;
        }
        return coord;
    }


    private static enum Direction {
        RIGHT(new Coordinate(0, 1)), LEFT(new Coordinate(0, -1)), DOWN(new Coordinate(1, 0)), UP(new Coordinate(-1, 0)),
        STALL(new Coordinate(0, 0));

        public Coordinate getShift() {
            return shift;
        }

        private Coordinate shift;

        private Direction(Coordinate coord) {
            this.shift = coord;
        }

        List<Direction> getAllowed() {
            if (this == STALL) {
                return Arrays.asList(RIGHT, DOWN, UP, LEFT);
            } else if (this == RIGHT) {
                return Arrays.asList(DOWN, UP, RIGHT);
            } else if (this == LEFT) {
                return Arrays.asList(UP, DOWN, LEFT);
            } else if (this == DOWN) {
                return Arrays.asList(LEFT, RIGHT, DOWN);
            } else
                return Arrays.asList(LEFT, RIGHT, UP);
        }
    }


    @AllArgsConstructor
    private static class CoordInfo {
        private Coordinate coord;
        private Direction dir;
        private int sameMovesCounter;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoordInfo coordInfo = (CoordInfo) o;

            if (sameMovesCounter != coordInfo.sameMovesCounter) return false;
            if (!Objects.equals(coord, coordInfo.coord)) return false;
            return dir == coordInfo.dir;
        }

        @Override
        public int hashCode() {
            int result = coord != null ? coord.hashCode() : 0;
            result = 31 * result + (dir != null ? dir.hashCode() : 0);
            result = 31 * result + sameMovesCounter;
            return result;
        }
    }

    @AllArgsConstructor
    private static class DistanceCoord {
        private CoordInfo coordInfo;
        private int distance;
    }

    private static int getScore(List<CoordInfo> scorePath, Map<Coordinate, Integer> coords) {

        int sum = scorePath.stream()
                .map(coordInfo -> coordInfo.coord)
                .map(coords::get)
                .mapToInt(value -> value)
                .sum();
        return sum;
    }

}
