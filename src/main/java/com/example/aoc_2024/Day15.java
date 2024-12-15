package com.example.aoc_2024;

import com.example.aoc_2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Day15 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day15.txt");
        Map<Coord, Character> elements = new HashMap<>();
        Coord robot = null;
        int row;
        for (row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            if (line.isEmpty()) {
                row = row + 1;
                break;
            }
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                elements.put(new Coord(row, col), c);
                if (c == '@') {
                    robot = new Coord(row, col);
                }
            }
        }


        List<Coord> moves = new ArrayList<>();

        for (int i = row; i < lines.size(); i++) {
            String line = lines.get(i);
            for (char c : line.toCharArray()) {
                Coord move;
                if (c == '>') {
                    move = new Coord(0, 1);
                } else if (c == '<') {
                    move = new Coord(0, -1);
                } else if (c == 'v') {
                    move = new Coord(1, 0);
                } else {
                    move = new Coord(-1, 0);
                }
                moves.add(move);
            }
        }

        for (int step = 0; step < moves.size(); step++) {
            Coord move = moves.get(step);
            Coord nextPossibleCoord;
            nextPossibleCoord = new Coord(robot.row + move.row, robot.col + move.col);
            elements.put(robot, '.');
            if (elements.get(nextPossibleCoord) == '.') {
                elements.put(robot, '.');
                robot = new Coord(robot.row + move.row, robot.col + move.col);
                elements.put(robot, '@');
                continue;
            }
            robot = moveBoxes(robot, elements, move);
            //print(elements, rows, lines.getFirst().length(), move, step);
        }

        long total = 0;
        for (Map.Entry<Coord, Character> entry : elements.entrySet()) {
            if (entry.getValue() == 'O') {
                total = total + (entry.getKey().row * 100 + entry.getKey().col);
            }
        }

        System.out.println(total);


    }

    private static void print(Map<Coord, Character> elements, int rows, int cols, Coord move, int step) {
        System.out.println("step = " + step + "....." + move);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                System.out.print(elements.get(new Coord(row, col)));
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    private static Coord moveBoxes(Coord robot, Map<Coord, Character> elements, Coord move) {
        Coord boxBLockLast = robot;
        List<Coord> boxes = new ArrayList<>();
        boxes.add(robot);
        Coord nextToBlock = new Coord(robot.row + move.row, robot.col + move.col);
        while (elements.get(nextToBlock) == 'O') {
            boxBLockLast = nextToBlock;
            boxes.add(nextToBlock);
            nextToBlock = new Coord(nextToBlock.row + move.row, nextToBlock.col + move.col);
        }

        var nextToBLock = new Coord(boxBLockLast.row + move.row, boxBLockLast.col + move.col);
        Coord shift = new Coord(0, 0);
        if (elements.get(nextToBLock) == '.') {
            shift = new Coord(move.row, move.col);
        }

        var robotNew = new Coord(boxes.getFirst().row + shift.row, boxes.getFirst().col + shift.col);
        for (int i = 0; i < boxes.size(); i++) {
            Coord box = boxes.get(i);
            elements.put(box, '.');
        }
        elements.put(robotNew, '@');

        for (int i = 1; i < boxes.size(); i++) {
            Coord box = boxes.get(i);
            var newBox = new Coord(box.row + shift.row, box.col + shift.col);
            elements.put(newBox, 'O');
        }

        return robotNew;

    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day15.txt");
        Map<Coord, Character> elements = new HashMap<>();
        Coord robot = null;
        int row;
        int actualCol;
        Map<Coord, Box> boxesCoord = new HashMap<>();
        for (row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            if (line.isEmpty()) {
                row = row + 1;
                break;
            }
            char[] charArray = line.toCharArray();
            actualCol = 0;
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == '@') {
                    robot = new Coord(row, actualCol);
                    elements.put(new Coord(row, actualCol++), '@');
                    elements.put(new Coord(row, actualCol++), '.');
                } else if (c == '#') {
                    elements.put(new Coord(row, actualCol++), '#');
                    elements.put(new Coord(row, actualCol++), '#');
                } else if (c == 'O') {
                    elements.put(new Coord(row, actualCol++), '[');
                    elements.put(new Coord(row, actualCol++), ']');
                    Box box = new Box(new Coord(row, actualCol - 2), new Coord(row, actualCol - 1));
                    boxesCoord.put(new Coord(row, actualCol - 2), box);
                    boxesCoord.put(new Coord(row, actualCol - 1), box);
                } else if (c == '.') {
                    elements.put(new Coord(row, actualCol++), '.');
                    elements.put(new Coord(row, actualCol++), '.');
                }
            }
        }

        List<Coord> moves = new ArrayList<>();

        for (int i = row; i < lines.size(); i++) {
            String line = lines.get(i);
            for (char c : line.toCharArray()) {
                Coord move;
                if (c == '>') {
                    move = new Coord(0, 1);
                } else if (c == '<') {
                    move = new Coord(0, -1);
                } else if (c == 'v') {
                    move = new Coord(1, 0);
                } else {
                    move = new Coord(-1, 0);
                }
                moves.add(move);
            }
        }
//        print(elements, rows, actualCol, new Coord(0, 0), 0);

        for (int step = 0; step < moves.size(); step++) {
            Coord move = moves.get(step);
            Coord nextPossibleCoord;
            nextPossibleCoord = new Coord(robot.row + move.row, robot.col + move.col);
            elements.put(robot, '.');
            if (elements.get(nextPossibleCoord) == '.') {
                elements.put(robot, '.');
                robot = new Coord(robot.row + move.row, robot.col + move.col);
                elements.put(robot, '@');
//                print(elements, rows, actualCol, move, step);
                continue;
            }
            robot = moveBoxesPart2(robot, elements, move, boxesCoord);
//            print(elements, rows, actualCol, move, step);
        }

        long total = 0;
        for (Map.Entry<Coord, Character> entry : elements.entrySet()) {
            if (entry.getValue() == '[') {
                total = total + (entry.getKey().row * 100 + entry.getKey().col);
            }
        }
        System.out.println(total);
    }

    private static Coord moveBoxesPart2(Coord robot, Map<Coord, Character> elements, Coord move, Map<Coord, Box> boxesCoord) {
        Coord nextToRobot = new Coord(robot.row + move.row, robot.col + move.col);
        List<Coord> boxCords = new ArrayList<>();
        boxCords.add(nextToRobot);
        Set<Box> boxesToMove = new HashSet<>();
        Set<Coord> visited = new HashSet<>();
        while (!boxCords.isEmpty()) {
            List<Coord> nextBoxCoords = new ArrayList<>();
            for (Coord boxCord : boxCords) {
                if (boxesCoord.containsKey(boxCord) && !visited.contains(boxCord)) {
                    var box = boxesCoord.get(boxCord);
                    visited.add(box.left);
                    visited.add(box.right);
                    boxesToMove.add(box);
                    nextBoxCoords.add(new Coord(box.left.row + move.row, box.left.col + move.col));
                    nextBoxCoords.add(new Coord(box.right.row + move.row, box.right.col + move.col));
                }
            }
            boxCords = nextBoxCoords;
        }
        boolean shouldMove = !visited.isEmpty();
        for (Coord coord : visited) {
            var next = new Coord(coord.row + move.row, coord.col + move.col);
            if (elements.get(next) == '#') {
                shouldMove = false;
                break;
            }
        }

        if (shouldMove) {
            for (Box box : boxesToMove) {
                elements.put(box.left, '.');
                elements.put(box.right, '.');
                boxesCoord.remove(box.left);
                boxesCoord.remove(box.right);
            }

            for (Box box : boxesToMove) {
                box.left = new Coord(box.left.row + move.row, box.left.col + move.col);
                box.right = new Coord(box.right.row + move.row, box.right.col + move.col);
                boxesCoord.put(box.left, box);
                boxesCoord.put(box.right, box);
                elements.put(box.left, '[');
                elements.put(box.right, ']');
            }
            robot = new Coord(robot.row + move.row, robot.col + move.col);
        }

        elements.put(robot, '@');
        return robot;
    }


    @Data
    @AllArgsConstructor
    static class Coord {
        int row;
        int col;
    }

    @AllArgsConstructor
    static class Box {
        Coord left;
        Coord right;
    }


}
