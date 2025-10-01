package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day24 {

    private static List<Coordinate> neighbourShifts = List.of(new Coordinate(0, 2), new Coordinate(0, -2),
            new Coordinate(-2, 1), new Coordinate(-2, -1), new Coordinate(2, 1), new Coordinate(2, -1));


    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day24.txt");
        Map<Coordinate, Element> board = new HashMap<>();
        Coordinate startCoordinate = new Coordinate(0, 0);
        Element start = new Element(startCoordinate);
        board.put(startCoordinate, start);
        flipTiles(lines, board);
        int blackCounter = calcBlackTiles(board);
        System.out.println("task1: " + blackCounter);
    }

    private static void flipTiles(List<String> lines, Map<Coordinate, Element> board) {
        for (String line : lines) {
            Coordinate cursor = new Coordinate(0, 0);
            int index = 0;
            while (index < line.length()) {
                String val = line.substring(index, index + 1);
                if (val.equals("e")) {
                    cursor = moveCursor(new Coordinate(0, 2), cursor, board);
                } else if (val.equals("w")) {
                    cursor = moveCursor(new Coordinate(0, -2), cursor, board);
                } else {
                    val = line.substring(index, index + 2);
                    if (val.equals("se")) {
                        cursor = moveCursor(new Coordinate(-2, 1), cursor, board);
                    } else if (val.equals("sw")) {
                        cursor = moveCursor(new Coordinate(-2, -1), cursor, board);
                    } else if (val.equals("ne")) {
                        cursor = moveCursor(new Coordinate(2, 1), cursor, board);
                    } else if (val.equals("nw")) {
                        cursor = moveCursor(new Coordinate(2, -1), cursor, board);
                    }
                    index++;
                }
                index++;
            }
            board.get(cursor).turn();
        }
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day24.txt");
        Map<Coordinate, Element> board = new HashMap<>();
        Coordinate startCoordinate = new Coordinate(0, 0);
        Element start = new Element(startCoordinate);
        board.put(startCoordinate, start);
        flipTiles(lines, board);
        for (int i = 0; i < 100; i++) {
            board = applyDailyRules(board);
        }
        int blackCounter = calcBlackTiles(board);
        System.out.println("task2: " + blackCounter);
    }

    private static Map<Coordinate, Element> applyDailyRules(Map<Coordinate, Element> board) {
        Set<Coordinate> coordToCheck = new HashSet<>();
        for (Map.Entry<Coordinate, Element> entry : board.entrySet()) {
            if (entry.getValue().color.equals("black")) {
                var currentCoord = entry.getKey();
                coordToCheck.add(entry.getKey());
                for (Coordinate neighbour : neighbourShifts) {
                    coordToCheck.add(new Coordinate(currentCoord.row + neighbour.row, currentCoord.col + neighbour.col));
                }
            }
        }
        Map<Coordinate, Element> updatedBoard = copy(board);
        for (Coordinate coordinate : coordToCheck) {
            var element = board.getOrDefault(coordinate, new Element(coordinate));
            int blackNeibs = countBlackNeibs(board, coordinate);
            if (element.color.equals("white")) {
                if (blackNeibs == 2) {
                    updatedBoard.put(coordinate, new Element(coordinate, "black"));
                }
            } else {
                if (blackNeibs == 0 || blackNeibs > 2) {
                    updatedBoard.put(coordinate, new Element(coordinate, "white"));
                }
            }
        }
        return updatedBoard;
    }

    private static int calcBlackTiles(Map<Coordinate, Element> board) {
        int blackCounter = 0;
        for (Map.Entry<Coordinate, Element> entry : board.entrySet()) {
            if (entry.getValue().color.equals("black")) {
                blackCounter++;
            }
        }
        return blackCounter;
    }

    private static Map<Coordinate, Element> copy(Map<Coordinate, Element> board) {
        Map<Coordinate, Element> copied = new HashMap<>();
        for (Map.Entry<Coordinate, Element> entry : board.entrySet()) {
            var el = entry.getValue();
            var copyEL = new Element(entry.getKey());
            copyEL.color = el.color;
            copied.put(entry.getKey(), copyEL);
        }
        return copied;
    }

    private static int countBlackNeibs(Map<Coordinate, Element> board, Coordinate currentCoord) {
        int counter = 0;
        for (Coordinate neighbourMove : neighbourShifts) {
            var neighbour = new Coordinate(currentCoord.row + neighbourMove.row, currentCoord.col + neighbourMove.col);
            Element el = board.get(neighbour);
            if (el != null && el.color.equals("black")) {
                counter++;
            }
        }
        return counter;
    }

    private static Coordinate moveCursor(Coordinate shift, Coordinate cursor, Map<Coordinate, Element> board) {
        var nextCoord = new Coordinate(cursor.row + shift.row, cursor.col + shift.col);
        var nextEl = new Element(nextCoord);
        if (!board.containsKey(nextCoord)) {
            board.put(nextCoord, nextEl);
        }
        return nextCoord;
    }

    static class Element {

        private String color;
        private Coordinate coordinate;

        public Element(Coordinate coordinate) {
            this.coordinate = coordinate;
            this.color = "white";
        }

        public Element(Coordinate coordinate, String color) {
            this.color = color;
            this.coordinate = coordinate;
        }

        public void turn() {
            if (color.equals("white")) {
                color = "black";
            } else {
                color = "white";
            }
        }

    }
}
