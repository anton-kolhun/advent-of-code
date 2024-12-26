package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day8.txt");
        Map<Coord, Character> signals = new HashMap<>();
        Map<Character, List<Coord>> charByCoord = new HashMap<>();
        int rows = lines.size();
        int cols = lines.get(0).length();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);
                if (ch == '#' || ch == '.') {
                    continue;
                }
                signals.put(new Coord(row, col), line.charAt(col));
                List<Coord> coords = new ArrayList<>();
                coords.add(new Coord(row, col));
                charByCoord.merge(line.charAt(col), coords, (coords1, coords2) -> {
                    coords1.addAll(coords2);
                    return coords1;
                });
            }
        }
        Set<Coord> total = new HashSet<>();
        for (Map.Entry<Character, List<Coord>> entry : charByCoord.entrySet()) {
            List<Coord> antiNodes = getAntiNodes(entry.getKey(), entry.getValue(), signals);
            total.addAll(antiNodes);
        }
        Set<Coord> filtered = total.stream()
                .filter(coord -> coord.row >= 0 && coord.row < rows && coord.col >= 0 && coord.col < cols)
                .collect(Collectors.toSet());

//        print(filtered, rows, cols);
        System.out.println(filtered.size());
    }

    private static List<Coord> getAntiNodes(Character ch, List<Coord> coords, Map<Coord, Character> signals) {
        List<Coord> res = new ArrayList<>();
        for (int i = 0; i < coords.size() - 1; i++) {
            Coord coord = coords.get(i);
            for (int j = i + 1; j < coords.size(); j++) {
                Coord otherCoord = coords.get(j);
                Coord diff1 = new Coord(coord.row - otherCoord.row, coord.col - otherCoord.col);
                Coord diff2 = new Coord(otherCoord.row - coord.row, otherCoord.col - coord.col);
                var antiNode1 = new Coord(coord.row + diff1.row, coord.col + diff1.col);
                var antiNode2 = new Coord(otherCoord.row + diff2.row, otherCoord.col + diff2.col);
                res.add(antiNode1);
                res.add(antiNode2);
            }

        }
        return res;
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day8.txt");
        Map<Coord, Character> signals = new HashMap<>();
        Map<Character, List<Coord>> charByCoord = new HashMap<>();
        int rows = lines.size();
        int cols = lines.get(0).length();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);
                if (ch == '#' || ch == '.') {
                    continue;
                }
                signals.put(new Coord(row, col), line.charAt(col));
                List<Coord> coords = new ArrayList<>();
                coords.add(new Coord(row, col));
                charByCoord.merge(line.charAt(col), coords, (coords1, coords2) -> {
                    coords1.addAll(coords2);
                    return coords1;
                });
            }
        }
        Set<Coord> total = new HashSet<>();
        for (Map.Entry<Character, List<Coord>> entry : charByCoord.entrySet()) {
            List<Coord> antiNodes = getAntiNodes2(entry.getKey(), entry.getValue(), signals, rows, cols);
            total.addAll(antiNodes);
        }
        Set<Coord> filtered = total.stream()
                .filter(coord -> coord.row >= 0 && coord.row < rows && coord.col >= 0 && coord.col < cols)
                .collect(Collectors.toSet());

//        print(filtered, rows, cols);

        System.out.println(filtered.size());
    }

    private static List<Coord> getAntiNodes2(Character ch, List<Coord> coords, Map<Coord, Character> signals,
                                             int rows, int cols) {
        List<Coord> res = new ArrayList<>();
        for (int i = 0; i < coords.size() - 1; i++) {
            Coord coord = coords.get(i);
            for (int j = i + 1; j < coords.size(); j++) {
                Coord otherCoord = coords.get(j);
                Coord diff1 = new Coord(coord.row - otherCoord.row, coord.col - otherCoord.col);
                Coord diff2 = new Coord(otherCoord.row - coord.row, otherCoord.col - coord.col);
                getAllAntiNodesInline(rows, cols, res, coord, diff1);
                getAllAntiNodesInline(rows, cols, res, otherCoord, diff2);
            }

        }
        return res;
    }

    private static void getAllAntiNodesInline(int rows, int cols, List<Coord> res, Coord coord, Coord diff) {
        var cursor = coord;
        res.add(coord);
        while (true) {
            cursor = new Coord(cursor.row + diff.row, cursor.col + diff.col);
            if (cursor.row >= 0 && cursor.row < rows && cursor.col >= 0 && cursor.col < cols) {
                res.add(cursor);
            } else {
                break;
            }
        }
    }

    public static void print(Set<Coord> antinodes, int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                var coord = new Coord(row, col);
                if (antinodes.contains(coord)) {
                    System.out.print("#");
                }
                System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }


    @AllArgsConstructor
    @Data
    static class Coord {
        int row;
        int col;
    }
}
