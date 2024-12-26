package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day3 {

    public static void main(String[] args) {
        // task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day3.txt");
        List<NumbInfo> allNumbs = new ArrayList<>();
        HashMap<Coordinate, Character> nonDigitCoords = new HashMap<>();
        HashMap<Coordinate, Character> allChars = new HashMap<>();
        for (int line = 0; line < lines.size(); line++) {
            String currentLine = lines.get(line);
            char[] charArray = currentLine.toCharArray();
            StringBuilder currentNumb = new StringBuilder();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                allChars.put(new Coordinate(line, i), c);
                if (Character.isDigit(c)) {
                    currentNumb.append(c);
                } else {
                    if (!currentNumb.isEmpty()) {
                        var numbInfo = new NumbInfo(currentNumb.toString(), new Coordinate(line, i - 1));
                        allNumbs.add(numbInfo);
                        currentNumb = new StringBuilder();

                    }
                    if (c != '.') {
                        nonDigitCoords.put(new Coordinate(line, i), c);
                    }
                }
            }
            if (!currentNumb.isEmpty()) {
                allNumbs.add(new NumbInfo(currentNumb.toString(), new Coordinate(line, charArray.length - 1)));
            }
        }
        List<Integer> matching = new ArrayList<>();
        for (NumbInfo numb : allNumbs) {
            addIfMatches(nonDigitCoords, matching, numb, allChars);
        }
        long res = matching.stream().mapToInt(value -> value).sum();
        System.out.println(res);


    }

    private static void addIfMatches(Map<Coordinate, Character> nonDigitCoords, List<Integer> matching, NumbInfo numb,
                                     Map<Coordinate, Character> allChars) {
        int length = numb.numb.length();
        int column = numb.endCoordinate.col - length + 1;
        int row = numb.endCoordinate.row;
        for (int line = row - 1; line <= row + 1; line++) {
            for (int col = column - 1; col <= column + length; col++) {
                if (nonDigitCoords.containsKey(new Coordinate(line, col))) {
                    if (line == row && col >= column && col <= column + length - 1) {
                        continue;
                    }
                    matching.add(Integer.parseInt(numb.numb));
                    //numb, nonDigitCoords, allChars);
                    return;
                }
            }
        }
//        print(numb, nonDigitCoords, allChars);
        //System.out.println("ex");
        //System.out.println("no hit = " + numb.numb + " , row = " + numb.endCoordinate.row);
    }

    private static void print(NumbInfo numb, Map<Coordinate, Character> nonDigitCoords, Map<Coordinate, Character> allChars) {
        int length = numb.numb.length();
        int column = numb.endCoordinate.col - length + 1;
        int row = numb.endCoordinate.row;
        System.out.println("row = " + row);
        for (int line = row - 1; line <= row + 1; line++) {
            for (int col = column - 1; col <= column + length; col++) {
                if (nonDigitCoords.containsKey(new Coordinate(line, col))) {
                    System.out.print(nonDigitCoords.get(new Coordinate(line, col)));
                } else if (line == row && col >= column && col <= column + length - 1) {
                    System.out.print(numb.numb.charAt(col - column));
                } else {
                    System.out.print(allChars.getOrDefault(new Coordinate(line, col), 'E'));
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    public static void task2() {

        List<String> lines = FilesUtils.readFile("aoc_2023/day3.txt");
        List<NumbInfo> allNumbs = new ArrayList<>();
        Map<Coordinate, Character> nonDigitCoords = new HashMap<>();
        Map<Coordinate, Character> allChars = new HashMap<>();
        Map<Coordinate, Character> stars = new HashMap<>();
        for (int line = 0; line < lines.size(); line++) {
            String currentLine = lines.get(line);
            char[] charArray = currentLine.toCharArray();
            StringBuilder currentNumb = new StringBuilder();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                allChars.put(new Coordinate(line, i), c);
                if (Character.isDigit(c)) {
                    currentNumb.append(c);
                } else {
                    if (!currentNumb.isEmpty()) {
                        var numbInfo = new NumbInfo(currentNumb.toString(), new Coordinate(line, i - 1));
                        allNumbs.add(numbInfo);
                        currentNumb = new StringBuilder();

                    }
                    if (c != '.') {
                        nonDigitCoords.put(new Coordinate(line, i), c);
                    }
                    if (c == '*') {
                        stars.put(new Coordinate(line, i), c);
                    }
                }
            }
            if (!currentNumb.isEmpty()) {
                allNumbs.add(new NumbInfo(currentNumb.toString(), new Coordinate(line, charArray.length - 1)));
            }
        }

        Map<Coordinate, NumbInfo> digitArea = new HashMap<>();
        for (NumbInfo numb : allNumbs) {
            int length = numb.numb.length();
            for (int i = 0; i < length; i++) {
                digitArea.put(new Coordinate(numb.endCoordinate.row, numb.endCoordinate.col - i), numb);
            }
        }

        long sum = 0;
        for (Map.Entry<Coordinate, Character> entry : stars.entrySet()) {
            Coordinate coord = entry.getKey();
            sum += calc2adjNumbIfExists(coord, digitArea);
        }
        System.out.println(sum);


    }

    private static long calc2adjNumbIfExists(Coordinate starCoord, Map<Coordinate, NumbInfo> digitAreas) {
        int length = 1;
        int ro = starCoord.row;
        int column = starCoord.col;
        Set<NumbInfo> adjEls = new HashSet<>();
        for (int row = ro - 1; row <= ro + 1; row++) {
            for (int col = column - 1; col <= column + length; col++) {
                if (digitAreas.containsKey(new Coordinate(row, col))) {
                    adjEls.add(digitAreas.get(new Coordinate(row, col)));
                }
            }
        }
        if (adjEls.size() == 2) {
            long multiple = 1;
            for (NumbInfo adjEl : adjEls) {
                multiple = multiple * Integer.parseInt(adjEl.numb);
            }
            return multiple;
        }
        return 0;
    }


    @AllArgsConstructor
    private static class NumbInfo {
        private String numb;
        private Coordinate endCoordinate;
    }


    @AllArgsConstructor
    @Data
    private static class Coordinate {
        int row;
        int col;
    }
}
