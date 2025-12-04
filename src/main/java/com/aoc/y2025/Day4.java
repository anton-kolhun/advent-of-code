package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day4 {
    private static List<Coord> moves = List.of(new Coord(-1, -1), new Coord(-1, 0), new Coord(-1, 1),
            new Coord(0, -1), new Coord(0, 1), new Coord(1, -1), new Coord(1, 0), new Coord(1, 1));

    public static void main(String[] args) {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day4.txt");
        Map<Coord, Character> map = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == '@') {
                    map.put(new Coord(row, col), '@');
                }
            }
        }
        List<Coord> matching = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                Coord currentCoord = new Coord(row, col);
                if (map.containsKey(currentCoord)) {
                    List<Coord> neinbs = getNeibs(currentCoord, moves);
                    int neibCounter = 0;
                    for (Coord neinb : neinbs) {
                        if (map.containsKey(neinb)) {
                            neibCounter++;
                        }
                    }
                    if (neibCounter < 4) {
                        matching.add(currentCoord);
                    }
                }
            }
        }
        System.out.println("task1: " + matching.size());
    }

    private static List<Coord> getNeibs(Coord coord, List<Coord> moves) {
        return moves.stream().map(move -> new Coord(coord.row + move.row, coord.col + move.col))
                .toList();
    }

    record Coord(int row, int col) {

    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day4.txt");
        Map<Coord, Character> map = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == '@') {
                    map.put(new Coord(row, col), '@');
                }
            }
        }
        List<Coord> matching = new ArrayList<>();
        boolean isMapUpdated = true;
        while (isMapUpdated) {
            isMapUpdated = false;
            Map<Coord, Character> nextMap = new HashMap<>(map);
            for (int row = 0; row < lines.size(); row++) {
                String line = lines.get(row);
                char[] charArray = line.toCharArray();
                for (int col = 0; col < charArray.length; col++) {
                    Coord currentCoord = new Coord(row, col);
                    if (map.containsKey(currentCoord)) {
                        List<Coord> neinbs = getNeibs(currentCoord, moves);
                        int neibCounter = 0;
                        for (Coord neinb : neinbs) {
                            if (map.containsKey(neinb)) {
                                neibCounter++;
                            }
                        }
                        if (neibCounter < 4) {
                            matching.add(currentCoord);
                            nextMap.remove(currentCoord);
                            isMapUpdated = true;
                        }
                    }
                }
            }
            map = nextMap;
        }
        System.out.println("task2: " + matching.size());
    }
}
