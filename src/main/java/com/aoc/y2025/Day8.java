package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day8 {

    public static void main(String[] args) {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day8.txt");
        List<Coord> coordinates = new ArrayList<>();
        for (String line : lines) {
            List<Integer> vals = ParseUtils.splitByDelimiter(line, ",").stream()
                    .map(Integer::parseInt).toList();
            coordinates.add(new Coord(vals.get(0), vals.get(1), vals.get(2)));
        }
        Map<Coord, Circuit> coordToCircuits = new HashMap<>();
        Set<List<Integer>> visited = new HashSet<>();
        for (int steps = 0; steps < 1000; steps++) {
            double min = Long.MAX_VALUE;
            List<Integer> minPair = new ArrayList<>();
            for (int i = 0; i < coordinates.size() - 1; i++) {
                Coord c1 = coordinates.get(i);
                for (int j = i + 1; j < coordinates.size(); j++) {
                    Coord c2 = coordinates.get(j);
                    double dist = Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2) + Math.pow(c1.z - c2.z, 2));
                    if (dist < min && !visited.contains(List.of(i, j))) {
                        min = dist;
                        minPair = List.of(i, j);
                    }
                }
            }
            visited.add(minPair);
            Coord minCoord1 = coordinates.get(minPair.get(0));
            Coord minCoord2 = coordinates.get(minPair.get(1));
            Set<Coord> defCoord1 = new HashSet<>();
            defCoord1.add(minCoord1);
            var circuit1 = coordToCircuits.getOrDefault(minCoord1, new Circuit(defCoord1));
            Set<Coord> defCoord2 = new HashSet<>();
            defCoord2.add(minCoord2);
            var circuit2 = coordToCircuits.getOrDefault(minCoord2, new Circuit(defCoord2));
            if (!circuit1.equals(circuit2)) {
                Set<Coord> mergedCoords = new HashSet<>(circuit1.coords);
                mergedCoords.addAll(circuit2.coords);
                Circuit merged = new Circuit(mergedCoords);
                for (Coord mergedCoord : mergedCoords) {
                    coordToCircuits.put(mergedCoord, merged);
                }
            }
        }

        Set<Circuit> circuits = new HashSet<>(coordToCircuits.values());
        List<Circuit> sorted = circuits.stream()
                .sorted((o1, o2) -> o2.coords.size() - o1.coords.size())
                .toList();
        System.out.println("task1: " + sorted.get(0).coords.size() * sorted.get(1).coords.size() * sorted.get(2).coords.size());
    }


    record Coord(long x, long y, long z) {
    }

    private static class Circuit {
        Set<Coord> coords;

        public Circuit(Set<Coord> coords) {
            this.coords = coords;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Circuit circuit = (Circuit) o;
            return Objects.equals(coords, circuit.coords);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(coords);
        }
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day8.txt");
        List<Coord> coordinates = new ArrayList<>();
        for (String line : lines) {
            List<Integer> vals = ParseUtils.splitByDelimiter(line, ",").stream()
                    .map(Integer::parseInt).toList();
            coordinates.add(new Coord(vals.get(0), vals.get(1), vals.get(2)));
        }
        Map<Coord, Circuit> coordToCircuits = new HashMap<>();
        Set<List<Integer>> visited = new HashSet<>();
        Set<Integer> visitedCoords = new HashSet<>();
        List<Integer> minPair = null;
        while (visitedCoords.size() < coordinates.size() || new HashSet<>(coordToCircuits.values()).size() > 1) {
            double min = Long.MAX_VALUE;
            minPair = new ArrayList<>();
            for (int i = 0; i < coordinates.size() - 1; i++) {
                Coord c1 = coordinates.get(i);
                for (int j = i + 1; j < coordinates.size(); j++) {
                    Coord c2 = coordinates.get(j);
                    double dist = Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2) + Math.pow(c1.z - c2.z, 2));
                    if (dist < min && !visited.contains(List.of(i, j))) {
                        min = dist;
                        minPair = List.of(i, j);
                    }
                }
            }
            visited.add(minPair);
            Coord minCoord1 = coordinates.get(minPair.get(0));
            Coord minCoord2 = coordinates.get(minPair.get(1));
            visitedCoords.add(minPair.get(0));
            visitedCoords.add(minPair.get(1));
            Set<Coord> defCoord1 = new HashSet<>();
            defCoord1.add(minCoord1);
            var circuit1 = coordToCircuits.getOrDefault(minCoord1, new Circuit(defCoord1));
            Set<Coord> defCoord2 = new HashSet<>();
            defCoord2.add(minCoord2);
            var circuit2 = coordToCircuits.getOrDefault(minCoord2, new Circuit(defCoord2));
            if (!circuit1.equals(circuit2)) {
                Set<Coord> mergedCoords = new HashSet<>(circuit1.coords);
                mergedCoords.addAll(circuit2.coords);
                Circuit merged = new Circuit(mergedCoords);
                for (Coord mergedCoord : mergedCoords) {
                    coordToCircuits.put(mergedCoord, merged);
                }
            }

        }
        var c1 = coordinates.get(minPair.get(0));
        var c2 = coordinates.get(minPair.get(1));
        System.out.println("task2: " + c1.x * c2.x);
    }
}
