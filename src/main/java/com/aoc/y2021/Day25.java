package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day25 {

    public static void main(String[] args) {
        task();
    }

    private static void task() {
        Map<Coordinate, Character> coords = new HashMap<>();
        Set<Coordinate> rights = new HashSet<>();
        Set<Coordinate> downs = new HashSet<>();
        List<String> lines = FilesUtils.readFile("aoc_2021/day25.txt");
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                coords.put(new Coordinate(row, col), c);
                if (c == '>') {
                    rights.add(new Coordinate(row, col));
                }
                if (c == 'v') {
                    downs.add(new Coordinate(row, col));
                }
            }
        }
        int height = lines.size();
        int width = lines.get(0).length();

        Map<Coordinate,Character> previousCoords = new HashMap<>();
        int rounds = 0;
        while (!previousCoords.equals(coords)) {
            previousCoords = new HashMap<>(coords);
//            System.out.println("Step: " + (rounds + 1));
            var nextCoords = new HashMap<>(coords);
            var nextRights = new HashSet<>(rights);
            for (Coordinate right : rights) {
                var next = new Coordinate(right.row, right.col + 1);
                if (!coords.containsKey(next)) {
                    next = new Coordinate(next.row, 0);
                }
                if (coords.get(next) == '.') {
                    nextCoords.put(next, '>');
                    nextRights.add(next);
                    nextCoords.put(right, '.');
                    nextRights.remove(right);
                }
            }
            rights = nextRights;
            coords = nextCoords;

            nextCoords = new HashMap<>(coords);
            var nextDowns = new HashSet<>(downs);
            for (Coordinate down : downs) {
                var next = new Coordinate(down.row + 1, down.col);
                if (!coords.containsKey(next)) {
                    next = new Coordinate(0, down.col);
                }
                if (coords.get(next) == '.') {
                    nextCoords.put(next, 'v');
                    nextDowns.add(next);
                    nextCoords.put(down, '.');
                    nextDowns.remove(down);
                }
            }
            coords = nextCoords;
            downs = nextDowns;
//            print(coords, width, height);
            rounds++;
        }
        System.out.println(rounds);

    }

    private static void print(Map<Coordinate, Character> coords, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(coords.get(new Coordinate(row, col)));
            }
            System.out.println();
        }
        System.out.println();
    }
}
