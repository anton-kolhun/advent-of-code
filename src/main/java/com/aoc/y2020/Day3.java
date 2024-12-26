package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3 {

    public static void main(String[] args) {
//        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day3.txt");
        Map<Coordinate, Character> coordToZone = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coordToZone.put(new Coordinate(row, col), c);
            }
        }
        Coordinate shift = new Coordinate(1, 3);
        Coordinate start = new Coordinate(1, 3);
        Coordinate cursor = start;
        int hashNumber = 0;
        if (coordToZone.get(cursor) == '#') {
            hashNumber++;
        }
        while (cursor.row != (lines.size() - 1)) {
            int col = (cursor.col + shift.col) % lines.get(0).length();
            cursor = new Coordinate(cursor.row + shift.row, col);
            if (coordToZone.get(cursor) == '#') {
                hashNumber++;
            }
        }
        System.out.println(hashNumber);
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day3.txt");
        Map<Coordinate, Character> coordToZone = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coordToZone.put(new Coordinate(row, col), c);
            }
        }
        List<Coordinate> starts = List.of(
                new Coordinate(1, 1),
                new Coordinate(1, 3),
                new Coordinate(1, 5),
                new Coordinate(1, 7),
                new Coordinate(2, 1)
        );

        Coordinate shift = new Coordinate(1, 3);
        int hashNumber = 0;
        long multiply = 1;
        for (Coordinate start : starts) {
            hashNumber = 0;
            Coordinate cursor = start;
            shift = start;
            if (coordToZone.get(cursor) == '#') {
                hashNumber++;
            }
            while (cursor.row < (lines.size() - 1)) {
                int col = (cursor.col + shift.col) % lines.get(0).length();
                cursor = new Coordinate(cursor.row + shift.row, col);
                if (coordToZone.getOrDefault(cursor, 'X') == '#') {
                    hashNumber++;
                }
            }
            multiply = multiply * hashNumber;
        }
        System.out.println(multiply);
    }


    @Data
    @AllArgsConstructor
    static class Coordinate {
        int row;
        int col;
    }
}
