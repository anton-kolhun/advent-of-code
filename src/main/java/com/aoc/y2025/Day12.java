package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day12 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day12.txt");
        List<Present> presents = new ArrayList<>();
        Present cursor;
        int lineNum = 0;
        for (; lineNum < lines.size(); lineNum++) {
            String line = lines.get(lineNum);
            if (line.contains("x")) {
                break;
            }
            if (line.contains(":")) {
                cursor = new Present(new HashSet<>());
                for (int i = 0; i < 3; i++) {
                    line = lines.get(lineNum + i + 1);
                    char[] charArray = line.toCharArray();
                    for (int col = 0; col < charArray.length; col++) {
                        if (charArray[col] == '#') {
                            cursor.coords.add(new Coordinate(i, col));
                        }
                    }
                }
                lineNum = lineNum + 4;
                presents.add(cursor);
            }
        }

        List<Tree> trees = new ArrayList<>();
        Tree cursorTree;
        for (; lineNum < lines.size(); lineNum++) {
            String line = lines.get(lineNum);
            List<String> parts = ParseUtils.splitByDelimiter(line, ":");
            List<Integer> area = ParseUtils.splitByDelimiter(parts.getFirst(), "x").stream().map(Integer::parseInt).toList();
            int rows = area.get(1);
            int cols = area.get(0);
            List<Integer> gifts = ParseUtils.splitByDelimiter(parts.getLast(), " ").stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
            cursorTree = new Tree(rows, cols, gifts);
            trees.add(cursorTree);
        }
        int matching = 0;
        for (Tree tree : trees) {
            if (matchArea(tree.presents.stream().reduce(Integer::sum).get(), tree.rows * tree.cols)) {
                matching++;
            }
        }
        System.out.println("result: " + matching);
    }

    private static boolean matchArea(Integer gifts, int allocatedSquare) {
        return (9 * gifts) <= allocatedSquare;
    }


    record Present(Set<Coordinate> coords) {

    }

    record Coordinate(Integer row, Integer col) {
    }

    record Tree(int rows, int cols, List<Integer> presents) {
    }

}
