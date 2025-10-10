package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import com.aoc.y2023.helper.ParseUtils;

import java.util.List;

public class Day17 {

    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day17.txt");
        List<String> parts = ParseUtils.splitByDelimiter(lines.get(0), ":");
        List<String> coordData = ParseUtils.splitByDelimiter(parts.get(1).trim(), ",");

        List<String> rowData = ParseUtils.splitByDelimiter(coordData.get(1).trim(), "=");
        rowData = ParseUtils.splitByDelimiter(rowData.get(1).trim(), "\\..");
        int row1 = Integer.parseInt(rowData.get(0));
        int row2 = Integer.parseInt(rowData.get(1));

        List<String> colData = ParseUtils.splitByDelimiter(coordData.get(0).trim(), "=");
        colData = ParseUtils.splitByDelimiter(colData.get(1).trim(), "\\..");
        int col1 = Integer.parseInt(colData.get(0));
        int col2 = Integer.parseInt(colData.get(1));

        int minRow = Math.min(row1, row2);
        int minCol = Math.min(col1, col2);
        int maxRow = Math.max(row1, row2);
        int maxCol = Math.max(col1, col2);

        int maxHeight = Integer.MIN_VALUE;
        int rangeCounter = 0;
        for (int shiftCol = 1; shiftCol <= maxCol; shiftCol++) {
            for (int shiftRow = -80; shiftRow <= 200; shiftRow++) {
                int height = getMaxHeight(shiftRow, shiftCol, minCol, maxCol, minRow, maxRow);
                if (inRange(shiftRow, shiftCol, minCol, maxCol, minRow, maxRow)) {
                    rangeCounter++;
                }
                if (height > maxHeight) {
                    maxHeight = height;
                }
            }
        }
        System.out.println("task1: " + maxHeight);
        System.out.println("task2: " + rangeCounter);
    }

    private static int getMaxHeight(int shiftRow, int shiftCol, int minCol, int maxCol, int minRow, int maxRow) {
        var cursor = new Coordinate(0, 0);
        int maxHeight = Integer.MIN_VALUE;
        boolean enteredArea = false;
        while (cursor.col < maxCol && cursor.row > minRow) {
            int cursorCol = cursor.col + shiftCol;
            if (shiftCol > 0) {
                shiftCol--;
            }
            int cursorRow = cursor.row + shiftRow;
            if (cursorRow > maxHeight) {
                maxHeight = cursorRow;
            }
            shiftRow--;
            cursor = new Coordinate(cursorRow, cursorCol);
            if (cursorRow <= maxRow && cursorRow >= minRow && cursorCol <= maxCol && cursorCol >= minCol) {
                enteredArea = true;
            } else {
                if (enteredArea) {
                    return maxHeight;
                }
            }
        }
        if (enteredArea) {
            return maxHeight;
        }
        return -1;
    }


    private static boolean inRange(int shiftRow, int shiftCol, int minCol, int maxCol, int minRow, int maxRow) {
        var cursor = new Coordinate(0, 0);
        while (cursor.col < maxCol && cursor.row > minRow) {
            int cursorCol = cursor.col + shiftCol;
            if (shiftCol > 0) {
                shiftCol--;
            }
            int cursorRow = cursor.row + shiftRow;
            shiftRow--;
            cursor = new Coordinate(cursorRow, cursorCol);
            if (cursorRow <= maxRow && cursorRow >= minRow && cursorCol <= maxCol && cursorCol >= minCol) {
                return true;
            }
        }
        return false;
    }

}
