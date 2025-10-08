package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day20 {

    private static List<Coordinate> moves = List.of(
            new Coordinate(-1, -1), new Coordinate(-1, 0), new Coordinate(-1, 1),
            new Coordinate(0, -1), new Coordinate(0, 0), new Coordinate(0, 1),
            new Coordinate(1, -1), new Coordinate(1, 0), new Coordinate(1, 1));

    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day20.txt");
        List<Character> algo = new ArrayList<>();
        int lineNumber = 0;
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            for (char c : line.toCharArray()) {
                algo.add(c);
            }
            if (line.isEmpty()) {
                break;
            }
        }
        lineNumber++;

        Map<Coordinate, Character> inputImage = new HashMap<>();
        int row = 0;
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                inputImage.put(new Coordinate(row, col), c);
            }
            row++;
        }

        Map<Coordinate, Character> image = new HashMap<>(inputImage);
        for (int i = 0; i < 2; i++) {
            image = transform(image, algo, i);
        }
        long hashes = image.values().stream().filter(character -> character == '#').count();
        System.out.println("task1: " + hashes);

        image = new HashMap<>(inputImage);
        for (int i = 0; i < 50; i++) {
            image = transform(image, algo, i);
        }
        hashes = image.values().stream().filter(character -> character == '#').count();
        System.out.println("task2: " + hashes);

    }

    private static Map<Coordinate, Character> transform(Map<Coordinate, Character> image, List<Character> algo, int round) {
        int row;
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;
        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        for (Map.Entry<Coordinate, Character> entry : image.entrySet()) {
            minCol = Math.min(minCol, entry.getKey().col);
            maxCol = Math.max(maxCol, entry.getKey().col);
            minRow = Math.min(minRow, entry.getKey().row);
            maxRow = Math.max(maxRow, entry.getKey().row);
        }
        Map<Coordinate, Character> updatedImage = new HashMap<>(image);
        Character defaultChar = (round % 2 == 0) ? '.' : '#'; //// based on my sample algorithm structure
//        Character defaultChar = '.'; // based on test sample algorithm structure
        for (row = minRow - 1; row <= maxRow + 1; row++) {
            for (int col = minCol - 1; col <= maxCol + 1; col++) {
                List<Coordinate> neibs = getNeibs(new Coordinate(row, col));
                StringBuilder key = new StringBuilder();
                for (Coordinate neib : neibs) {
                    Character val = image.getOrDefault(neib, defaultChar);
                    if (val == '.') {
                        key.append('0');
                    } else {
                        key.append('1');
                    }
                }
                int value = Integer.parseInt(key.toString(), 2);
                Character val = algo.get(value);
                updatedImage.put(new Coordinate(row, col), val);
            }
        }
//        print(minRow, maxRow, minCol, maxCol, updatedImage);
        return updatedImage;
    }

    private static void print(int minRow, int maxRow, int minCol, int maxCol, Map<Coordinate, Character> updatedImage) {
        int row;
        for (row = minRow - 1; row <= maxRow + 1; row++) {
            for (int col = minCol - 1; col <= maxCol + 1; col++) {
                System.out.print(updatedImage.getOrDefault(new Coordinate(row, col), '.'));
            }
            System.out.println();
        }
        System.out.println();
    }


    private static List<Coordinate> getNeibs(Coordinate coord) {
        return moves.stream()
                .map(shift -> new Coordinate(coord.row + shift.row, coord.col + shift.col))
                .toList();
    }
}
