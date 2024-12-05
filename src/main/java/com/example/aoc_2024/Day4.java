package com.example.aoc_2024;

import com.example.aoc_2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day4 {

    public static String XMAS = "XMAS";
    public static String MAS = "MAS";


    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day4.txt");
        Map<Coordinate, Character> coordToVal = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coordToVal.put(new Coordinate(row, col), c);
            }
        }
        int rowNumber = lines.size() - 1;
        int colNumber = lines.get(0).length() - 1;
        Set<Coordinate> directions = Set.of(
                new Coordinate(1, 0),
                new Coordinate(-1, 0),
                new Coordinate(0, 1),
                new Coordinate(0, -1),
                new Coordinate(1, 1),
                new Coordinate(1, -1),
                new Coordinate(-1, 1),
                new Coordinate(-1, -1)
        );
        int number = 0;
        for (int row = 0; row <= rowNumber; row++) {
            for (int col = 0; col <= colNumber; col++) {
                int res = checkXmas(row, col, coordToVal, directions);
                number = number + res;
            }
        }
        System.out.println(number);
    }

    private static int checkXmas(int row, int col, Map<Coordinate, Character> coordToVal, Set<Coordinate> directions) {
        var cursor = new Coordinate(row, col);
        List<Coordinate> coords = new ArrayList<>();
        coords.add(cursor);
        Set<List<Coordinate>> res = doCheckXmas(0, coords, coordToVal, directions, null);
//        for (List<Coordinate> re : res) {
//            System.out.print(re);
//            System.out.print("     ");
//            StringBuffer stringRes = new StringBuffer();
//            for (Coordinate coordinate : re) {
//                stringRes.append(coordToVal.get(coordinate));
//            }
//            System.out.println(stringRes);
//
//        }
        return res.size();
    }

    private static Set<List<Coordinate>> doCheckXmas(int charIndex, List<Coordinate> current,
                                                     Map<Coordinate, Character> coordToVal, Set<Coordinate> directions, Coordinate curDir) {
        Coordinate curCoord = current.getLast();
        char expected = XMAS.charAt(charIndex);
        if (coordToVal.getOrDefault(curCoord, '#') != expected) {
            return Collections.emptySet();
        }
        if ((charIndex == (XMAS.length() - 1)) && (coordToVal.getOrDefault(curCoord, '#') == expected)) {
            return Set.of(current);
        }

        Set<List<Coordinate>> total = new HashSet<>();
        if (curDir == null) {
            for (Coordinate direction : directions) {
                List<Coordinate> nextCurrent = new ArrayList<>(current);
                nextCurrent.add(new Coordinate(curCoord.row + direction.row, curCoord.col + direction.col));
                Set<List<Coordinate>> res = doCheckXmas(charIndex + 1, nextCurrent, coordToVal, directions, direction);
                total.addAll(res);
            }
        } else {
            List<Coordinate> nextCurrent = new ArrayList<>(current);
            nextCurrent.add(new Coordinate(curCoord.row + curDir.row, curCoord.col + curDir.col));
            Set<List<Coordinate>> res = doCheckXmas(charIndex + 1, nextCurrent, coordToVal, directions, curDir);
            total.addAll(res);
        }
        return total;
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day4.txt");
        Map<Coordinate, Character> coordToVal = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coordToVal.put(new Coordinate(row, col), c);
            }
        }
        int rowNumber = lines.size() - 1;
        int colNumber = lines.get(0).length() - 1;
        Set<Coordinate> directions = Set.of(
                new Coordinate(1, 1),
                new Coordinate(1, -1),
                new Coordinate(-1, 1),
                new Coordinate(-1, -1)
        );
        Set<List<Coordinate>> total = new HashSet<>();
        for (int row = 0; row <= rowNumber; row++) {
            for (int col = 0; col <= colNumber; col++) {
                Set<List<Coordinate>> res = checkXmas2(row, col, coordToVal, directions);
                total.addAll(res);
            }
        }

        int number = 0;
        Set<List<Coordinate>> used = new HashSet<>();
        for (List<Coordinate> mas : total) {
            if (used.contains(mas)) {
                continue;
            }
            List<List<Coordinate>> flips = flip(mas);
            if (total.contains(flips.get(0))) {
                number++;
                used.add(flips.get(0));
                used.add(mas);
            }
            if (total.contains(flips.get(1))) {
                number++;
                used.add(flips.get(1));
                used.add(mas);
            }
        }
        System.out.println(number);
    }

    private static Set<List<Coordinate>> checkXmas2(int row, int col, Map<Coordinate, Character> coordToVal, Set<Coordinate> directions) {
        var cursor = new Coordinate(row, col);
        List<Coordinate> coords = new ArrayList<>();
        coords.add(cursor);
        Set<List<Coordinate>> res = doCheckXmas2(0, coords, coordToVal, directions, null);
        return res;
    }

    private static List<List<Coordinate>> flip(List<Coordinate> mas) {
        var start = mas.getFirst();
        var end = mas.getLast();
        var flip1 = List.of(new Coordinate(start.row, end.col), mas.get(1), new Coordinate(end.row, start.col));
        var flip2 = List.of(new Coordinate(end.row, start.col), mas.get(1), new Coordinate(start.row, end.col));
        return List.of(flip1, flip2);
    }

    private static Set<List<Coordinate>> doCheckXmas2(int charIndex, List<Coordinate> current,
                                                      Map<Coordinate, Character> coordToVal, Set<Coordinate> directions, Coordinate curDir) {
        Coordinate curCoord = current.getLast();
        char expected = MAS.charAt(charIndex);
        if (coordToVal.getOrDefault(curCoord, '#') != expected) {
            return Collections.emptySet();
        }
        if ((charIndex == (MAS.length() - 1)) && (coordToVal.getOrDefault(curCoord, '#') == expected)) {
            return Set.of(current);
        }

        Set<List<Coordinate>> total = new HashSet<>();
        if (curDir == null) {
            for (Coordinate direction : directions) {
                List<Coordinate> nextCurrent = new ArrayList<>(current);
                nextCurrent.add(new Coordinate(curCoord.row + direction.row, curCoord.col + direction.col));
                Set<List<Coordinate>> res = doCheckXmas2(charIndex + 1, nextCurrent, coordToVal, directions, direction);
                total.addAll(res);
            }
        } else {
            List<Coordinate> nextCurrent = new ArrayList<>(current);
            nextCurrent.add(new Coordinate(curCoord.row + curDir.row, curCoord.col + curDir.col));
            Set<List<Coordinate>> res = doCheckXmas2(charIndex + 1, nextCurrent, coordToVal, directions, curDir);
            total.addAll(res);
        }
        return total;
    }


    @Data
    @AllArgsConstructor
    static class Coordinate {
        int row;
        int col;

        @Override
        public String toString() {
            return String.format("(row=%s; col=%s)", row, col);
        }
    }
}
