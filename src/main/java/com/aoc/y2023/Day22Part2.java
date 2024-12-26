package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day22Part2 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day22.txt");
        Map<Coordinate, Integer> coordToBrick = new LinkedHashMap<>();
        Map<Integer, List<Coordinate>> brickToCoord = new LinkedHashMap<>();


        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            List<String> parts = ParseUtils.splitByDelimiter(line, "~");
            List<String> edge1 = ParseUtils.splitByDelimiter(parts.get(0), ",");
            List<String> edge2 = ParseUtils.splitByDelimiter(parts.get(1), ",");
            populateCoords(edge1, edge2, coordToBrick, row, brickToCoord);
        }

        var pair = liftDown(coordToBrick, brickToCoord);
        Map<Integer, Set<Integer>> deps = new HashMap<>();

        processBricks(pair.coordToBrick, pair.brickToCoord, deps);


    }

    private static BrickData liftDown(Map<Coordinate, Integer> coordToBrick, Map<Integer, List<Coordinate>> brickToCoord) {
        var updatedCoordToBrick = new HashMap<>(coordToBrick);
        var updatedBrickToCoord = new HashMap<>(brickToCoord);

        //List<Coordinate> coords = new ArrayList<>(brickToCoord.values().stream().flatMap(Collection::stream).toList());
        List<Coordinate> coords = brickToCoord.values().stream().sorted((l1, l2) -> {
                    if ((l1.get(0).z - l2.get(0).z) != 0) {
                        return l1.get(0).z - l2.get(0).z;
                    }
                    return -(l1.get(l1.size() - 1).z - l2.get(l2.size() - 1).z);
                })
                .map(coordinates -> coordinates.get(0))
                .toList();
        Set<Integer> visited = new HashSet<>();
        for (Coordinate coord : coords) {
            var brick = coordToBrick.get(coord);
            if (visited.contains(brick)) {
                continue;
            }
            visited.add(brick);
            boolean canMove = true;
            var cursor = brickToCoord.get(brick);
            while (canMove) {
                List<Coordinate> updatedCursor = new ArrayList<>();
                for (Coordinate co : cursor) {
                    if (co.z <= 1) {
                        canMove = false;
                        System.out.println(String.format("brick=%s reached bottom. %s ", brick, toStringCoords(updatedBrickToCoord.get(brick))));
                        break;
                    }
                    var belowCoord = new Coordinate(co.x, co.y, co.z - 1);
                    if (!updatedCoordToBrick.getOrDefault(belowCoord, brick).equals(brick)) {
                        System.out.println(String.format("brick=%s cannot move %s. stopped by brick=%s", brick,
                                toStringCoords(updatedBrickToCoord.get(brick)), updatedCoordToBrick.get(belowCoord)));
                        canMove = false;
                        break;
                    }
                }
                if (canMove) {
                    for (Coordinate coordinate : cursor) {
                        updatedCoordToBrick.remove(coordinate);
                        var newCoord = new Coordinate(coordinate.x, coordinate.y, coordinate.z - 1);
                        updatedCursor.add(newCoord);
                        updatedCoordToBrick.put(newCoord, brick);
                    }
                    updatedBrickToCoord.put(brick, updatedCursor);
                    System.out.println(String.format("brick=%s : moving from %s ---------> %s ", brick, toStringCoords(cursor), toStringCoords(updatedCursor)));
                    cursor = updatedCursor;
                }
            }
        }

//        List<List<Coordinate>> crds = new ArrayList<>(updatedBrickToCoord.values());
//        var sorted = new ArrayList<>(crds.stream().flatMap(Collection::stream).toList());
//        sorted.sort(Comparator.comparingInt(o -> o.z));
//        for (Coordinate coordinate : sorted) {
//            System.out.print(coordinate.z + " ");
//        }
//        System.out.println();

        return new BrickData(updatedCoordToBrick, updatedBrickToCoord);

    }

    private static String toStringCoords(List<Coordinate> cursor) {
        StringBuilder sb = new StringBuilder("");
        for (Coordinate coordinate : cursor) {
            sb.append("[" + coordinate.x + " " + coordinate.y + " " + coordinate.z + "]");
        }
        return sb.toString();
    }


    private static void populateCoords(List<String> edge1, List<String> edge2, Map<Coordinate, Integer> coordToBrick,
                                       int row, Map<Integer, List<Coordinate>> brickToCoord) {
        List<Integer> edgeNumbs1 = edge1.stream().map(Integer::parseInt).toList();
        List<Integer> edgeNumbs2 = edge2.stream().map(Integer::parseInt).toList();
        int diffIndex = 0;
        for (int i = 1; i < 3; i++) {
            if (!edgeNumbs1.get(i).equals(edgeNumbs2.get(i))) {
                diffIndex = i;
                break;
            }
        }
        var shift = new Coordinate(0, 0, 0);
        if (diffIndex == 0) {
            shift.x = 1;
        } else if (diffIndex == 1) {
            shift.y = 1;
        } else {
            shift.z = 1;
        }

        var minBridge = edgeNumbs1;
        int minVal = Math.min(edgeNumbs1.get(diffIndex), edgeNumbs1.get(diffIndex));
        if (edgeNumbs1.get(diffIndex) != minVal) {
            minBridge = edgeNumbs2;
        }
        int maxVal = Math.max(edgeNumbs1.get(diffIndex), edgeNumbs2.get(diffIndex));
        var cursorCoord = new Coordinate(minBridge.get(0), minBridge.get(1), minBridge.get(2));
        coordToBrick.put(cursorCoord, row + 1);
        brickToCoord.merge(row + 1, List.of(cursorCoord), (coordinates, coordinates2) -> {
            var total = new ArrayList<>(coordinates);
            total.addAll(coordinates2);
            return total;
        });
        for (int cursor = minVal; cursor < maxVal; cursor++) {
            cursorCoord = new Coordinate(cursorCoord.x + shift.x, cursorCoord.y + shift.y, cursorCoord.z + shift.z);
            coordToBrick.put(cursorCoord, row + 1);
            brickToCoord.merge(row + 1, List.of(cursorCoord), (coordinates, coordinates2) -> {
                var total = new ArrayList<>(coordinates);
                total.addAll(coordinates2);
                return total;
            });
        }
        System.out.print("");

    }

    @NoArgsConstructor
    @ToString
    private static class Coordinate {
        int x;
        int y;
        int z;

        public Coordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;

            if (x != that.x) return false;
            if (y != that.y) return false;
            return z == that.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }


    private static void processBricks(Map<Coordinate, Integer> coordToBrick, Map<Integer, List<Coordinate>> brickToCoord, Map<Integer, Set<Integer>> thisOnCoords) {
        //print(brickToCoord, coordToBrick);
        Set<Integer> visited = new HashSet<>();
        Map<Integer, List<Integer>> coordsOnThis = new HashMap<>();
        while (visited.size() != brickToCoord.keySet().size()) {
            var minZ = Integer.MAX_VALUE;
            var minZInd = -1;
            for (Map.Entry<Integer, List<Coordinate>> entry : brickToCoord.entrySet()) {
                if (!visited.contains(entry.getKey()) && entry.getValue().get(0).z < minZ) {
                    minZ = entry.getValue().get(0).z;
                    minZInd = entry.getKey();
                }
            }

            List<Coordinate> minBrick = brickToCoord.get(minZInd);

            Set<Integer> connectedBricksAbove = new HashSet<>();
            for (Coordinate minCoord : minBrick) {
                for (Map.Entry<Integer, List<Coordinate>> entry : brickToCoord.entrySet()) {
                    if (entry.getKey().equals(minZInd)) {
                        continue;
                    }
                    List<Coordinate> otherCoords = entry.getValue();

                    for (Coordinate otherCoord : otherCoords) {
                        if (minCoord.x == otherCoord.x && minCoord.y == otherCoord.y) {
                            if (otherCoord.z == minCoord.z + 1) {
                                connectedBricksAbove.add(entry.getKey());
                            }
                        }
                    }
                }

                System.out.print("");
                // if (rightAboveMinZIndex > 0 && !areBricksInBetween(rightAboveMinZIndex, coordToBrick, brickToCoord, minZInd)) {
            }

            for (Integer connected : connectedBricksAbove) {
                thisOnCoords.merge(connected, Set.of(minZInd), (integers, integers2) -> {
                    var l = new HashSet<>(integers);
                    l.addAll(integers2);
                    return l;
                });
            }

            coordsOnThis.put(minZInd, new ArrayList<>(connectedBricksAbove));
            visited.add(minZInd);
        }

        Set<Integer> noRemove = new HashSet<>();
        for (Map.Entry<Integer, Set<Integer>> entry : thisOnCoords.entrySet()) {
            if (entry.getValue().size() == 1) {
                noRemove.add(entry.getValue().iterator().next());
            }
        }

//        Set<Integer> bricksWitNoAbove = new HashSet<>();
//        for (Integer numb : visited) {
//            if (deps2.getOrDefault(numb, new ArrayList<>()).size() == 0) {
//                System.out.println(numb);
//                bricksWitNoAbove.add(numb);
//            }
//        }
        int totalSize = 0;
        for (Integer index : noRemove) {
            Set<Integer> cursor = new LinkedHashSet<>();
            cursor.add(index);
            Set<Integer> total = new HashSet<>();
            total.add(index);
            while (!cursor.isEmpty()) {
                Iterator<Integer> it = cursor.iterator();
                int nextToFall = it.next();
                it.remove();
                List<Integer> toAdd = coordsOnThis.get(nextToFall);
                for (Integer ind : toAdd) {
                    Set<Integer> below = new HashSet<>(thisOnCoords.get(ind));
                    below.removeAll(total);
                    if (below.isEmpty()) {
                        if (!total.contains(ind)) {
                            coordsOnThis.get(ind);
                            cursor.add(ind);
                            total.add(ind);
                        }
                    }

                }

            }
            totalSize = totalSize + total.size() - 1;
            System.out.println("x");

        }
        System.out.println(totalSize);

    }

    private static void print(Map<Integer, List<Coordinate>> brickToCoord, Map<Coordinate, Integer> coordToBrick) {
        var minCoord = new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        var maxCoord = new Coordinate(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (Map.Entry<Integer, List<Coordinate>> entry : brickToCoord.entrySet()) {
            for (Coordinate coordinate : entry.getValue()) {
                if (coordinate.x < minCoord.x) {
                    minCoord.x = coordinate.x;
                }
                if (coordinate.y < minCoord.y) {
                    minCoord.y = coordinate.y;
                }
                if (coordinate.z < minCoord.z) {
                    minCoord.z = coordinate.z;
                }

                if (coordinate.x > maxCoord.x) {
                    maxCoord.x = coordinate.x;
                }
                if (coordinate.y > maxCoord.y) {
                    maxCoord.y = coordinate.y;
                }
                if (coordinate.z > maxCoord.z) {
                    maxCoord.z = coordinate.z;
                }

            }
        }


        for (Map.Entry<Integer, List<Coordinate>> entry : brickToCoord.entrySet()) {
            System.out.println(entry.getKey());
            for (Coordinate coordinate : entry.getValue()) {
                System.out.print(String.format("x=%s, y=%s, z=%s; ", coordinate.x, coordinate.y, coordinate.z));
            }
            System.out.println();
        }

    }


    @AllArgsConstructor
    private static class BrickData {
        Map<Coordinate, Integer> coordToBrick;
        Map<Integer, List<Coordinate>> brickToCoord;
    }
}
