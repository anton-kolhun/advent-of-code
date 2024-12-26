package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.List;

public class Day13Part2 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day13.txt");
        List<List<String>> patterns = new ArrayList<>();
        List<String> cursor = new ArrayList<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                patterns.add(cursor);
                cursor = new ArrayList<>();
            } else {
                cursor.add(line);
            }
        }
        patterns.add(cursor);


        long total = 0;
        for (List<String> pattern : patterns) {
            int score = checkMirror(pattern);
            total += score;
            System.out.println(score);
        }
        System.out.println(total);

    }

    private static int checkMirror(List<String> pattern) {
        int mirrorLine = checkHorizontal(pattern);
        if (mirrorLine != 0) {
            int score = mirrorLine * 100;
            return score;
        } else {
            mirrorLine = checkVertical(pattern);
            int score = mirrorLine;
            return score;
        }
    }

    private static int checkVertical(List<String> pattern) {
        for (int col = 0; col < pattern.get(0).length() - 1; col++) {
            int missMatches = 0;
            var colLeft = getCol(pattern, col);
            var colRight = getCol(pattern, col + 1);
            int mirrorCol = 0;
            // if (colLeft.equals(colRight)) {
            missMatches += calculateMissmatches(colLeft, colRight);
            int cursorLeft = col;
            int cursorRight = col + 1;
            mirrorCol = cursorRight;
            while (cursorLeft > 0 && cursorRight < pattern.get(0).length() - 1) {
                cursorLeft--;
                cursorRight++;
                int diffs = calculateMissmatches(getCol(pattern, cursorLeft), getCol(pattern, cursorRight));
                missMatches += diffs;
                if (missMatches > 1) {
                    break;
                }
            }
            //  }
            if (missMatches == 1) {
                return mirrorCol;
            }
        }
        return 0;
    }

    private static int calculateMissmatches(String str1, String str2) {
        char[] charArray = str1.toCharArray();
        int diff = 0;
        for (int i = 0; i < charArray.length; i++) {
            char c1 = str1.charAt(i);
            char c2 = str2.charAt(i);
            if (c1 != c2) {
                diff++;
                //System.out.println("missamtch " + i);
            }
        }
        return diff;
    }

    private static String getCol(List<String> pattern, int col) {
        StringBuilder sb = new StringBuilder();
        for (String s : pattern) {
            sb.append(s.charAt(col));
        }
        return sb.toString();
    }

    private static int checkHorizontal(List<String> pattern) {
        for (int i = 0; i < pattern.size() - 1; i++) {
            int missMatches = 0;
            var lineAbove = pattern.get(i);
            var lineBelow = pattern.get(i + 1);
            int mirrorLine = 0;
            //  if (lineAbove.equals(lineBelow)) {
            missMatches += calculateMissmatches(lineAbove, lineBelow);
            int cursorAbove = i;
            int cursorBelow = i + 1;
            mirrorLine = cursorBelow;
            while (cursorAbove > 0 && cursorBelow < pattern.size() - 1) {
                cursorAbove--;
                cursorBelow++;
                int diffs = calculateMissmatches(pattern.get(cursorBelow), pattern.get(cursorAbove));
                missMatches += diffs;
                if (missMatches > 1) {
                    break;
                }
            }
            if (missMatches == 1) {
                return mirrorLine;
            }
        }
        // }
        return 0;
    }
}