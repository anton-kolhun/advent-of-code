package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day14Part2 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day14.txt");

        Set<Coordinate> rocks = new HashSet<>();
        Set<Coordinate> obstacles = new HashSet<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == 'O') {
                    rocks.add(new Coordinate(row, col));
                } else if (c == '#') {
                    obstacles.add(new Coordinate(row, col));
                }
            }
        }

        Set<Coordinate> updatedRocks = new HashSet<>();
        Map<Set<Coordinate>, Integer> unique = new HashMap<>();


        int requiredStep = 174 + (1000000000 - 174) % 72;

        for (int i = 0; i < requiredStep; i++) {
            System.out.println(i + 1);
            updatedRocks = doFullCycle(lines, rocks, obstacles);
/*            if (i == 200) {
                //detect cycle length - 72;
                unique.put(updatedRocks, 200);
            }*/
            rocks = updatedRocks;
            //print(updatedRocks, obstacles, lines);
            System.out.println(" ");
//            if (unique.containsKey(updatedRocks)) {
//                //initial position 174
//                System.out.println("cycle initial position detected");
//            } else {
//                unique.put(updatedRocks, i);
//            }
        }

        long totalSum = 0;
        for (Coordinate newRock : updatedRocks) {
            totalSum += lines.size() - newRock.row;
        }
        System.out.println(totalSum);
        //print(newRocks, obstacles, lines);
    }

    private static Set<Coordinate> doFullCycle(List<String> lines, Set<Coordinate> rocks, Set<Coordinate> obstacles) {
        Set<Coordinate> newRocks = new HashSet<>();
        doMoveUp(lines, rocks, obstacles, newRocks);
//        print(newRocks, obstacles, lines);
//        System.out.println();

        rocks = newRocks;
        newRocks = new HashSet<>();
        doMoveLeft(lines, rocks, obstacles, newRocks);
//        print(newRocks, obstacles, lines);
//        System.out.println();

        rocks = newRocks;
        newRocks = new HashSet<>();
        doMoveDown(lines, rocks, obstacles, newRocks);
//        print(newRocks, obstacles, lines);
        //System.out.println();

        rocks = newRocks;
        newRocks = new HashSet<>();
        doMoveRight(lines, rocks, obstacles, newRocks);
//        print(newRocks, obstacles, lines);
        //System.out.println();
        return newRocks;
    }

    private static void doMoveUp(List<String> lines, Set<Coordinate> rocks, Set<Coordinate> obstacles, Set<Coordinate> newRocks) {
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                var coord = new Coordinate(row, col);
                if (rocks.contains(coord)) {
                    moveRockUp(obstacles, newRocks, coord, lines);
                }
            }
        }
    }

    private static void doMoveLeft(List<String> lines, Set<Coordinate> rocks, Set<Coordinate> obstacles, Set<Coordinate> newRocks) {
        for (int col = 0; col < lines.get(0).length(); col++) {
            for (int row = 0; row < lines.size(); row++) {
                var coord = new Coordinate(row, col);
                if (rocks.contains(coord)) {
                    moveRockLeft(obstacles, newRocks, coord, lines);
                }
            }
        }
    }

    private static void doMoveDown(List<String> lines, Set<Coordinate> rocks, Set<Coordinate> obstacles, Set<Coordinate> newRocks) {
        for (int row = lines.size() - 1; row >= 0; row--) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                var coord = new Coordinate(row, col);
                if (rocks.contains(coord)) {
                    moveRockDown(obstacles, newRocks, coord, lines);
                }
            }
        }
    }

    private static void doMoveRight(List<String> lines, Set<Coordinate> rocks, Set<Coordinate> obstacles, Set<Coordinate> newRocks) {
        for (int col = lines.get(0).length() - 1; col >= 0; col--) {
            for (int row = 0; row < lines.size(); row++) {
                var coord = new Coordinate(row, col);
                if (rocks.contains(coord)) {
                    moveRockRight(obstacles, newRocks, coord, lines);
                }
            }
        }
    }

    private static void moveRockUp(Set<Coordinate> obstacles, Set<Coordinate> newRocks, Coordinate rock, List<String> lines) {
        boolean isObstacle = false;
        for (int row = rock.row; row >= 0; row--) {
            if (obstacles.contains(new Coordinate(row, rock.col)) || newRocks.contains(new Coordinate(row, rock.col))) {
                newRocks.add(new Coordinate(row + 1, rock.col));
                isObstacle = true;
                break;
            }
        }
        if (!isObstacle) {
            newRocks.add(new Coordinate(0, rock.col));
        }
    }

    private static void moveRockDown(Set<Coordinate> obstacles, Set<Coordinate> newRocks, Coordinate rock, List<String> lines) {
        boolean isObstacle = false;
        for (int row = rock.row; row < lines.size(); row++) {
            if (obstacles.contains(new Coordinate(row, rock.col)) || newRocks.contains(new Coordinate(row, rock.col))) {
                newRocks.add(new Coordinate(row - 1, rock.col));
                isObstacle = true;
                break;
            }
        }
        if (!isObstacle) {
            newRocks.add(new Coordinate(lines.size() - 1, rock.col));
        }
    }

    private static void moveRockLeft(Set<Coordinate> obstacles, Set<Coordinate> newRocks, Coordinate rock, List<String> lines) {
        boolean isObstacle = false;
        for (int col = rock.col; col >= 0; col--) {
            if (obstacles.contains(new Coordinate(rock.row, col)) || newRocks.contains(new Coordinate(rock.row, col))) {
                newRocks.add(new Coordinate(rock.row, col + 1));
                isObstacle = true;
                break;
            }
        }
        if (!isObstacle) {
            newRocks.add(new Coordinate(rock.row, 0));
        }
    }

    private static void moveRockRight(Set<Coordinate> obstacles, Set<Coordinate> newRocks, Coordinate rock, List<String> lines) {
        boolean isObstacle = false;
        for (int col = rock.col; col < lines.get(0).length(); col++) {
            if (obstacles.contains(new Coordinate(rock.row, col)) || newRocks.contains(new Coordinate(rock.row, col))) {
                newRocks.add(new Coordinate(rock.row, col - 1));
                isObstacle = true;
                break;
            }
        }
        if (!isObstacle) {
            newRocks.add(new Coordinate(rock.row, lines.get(0).length() - 1));
        }
    }

    private static void print(Set<Coordinate> newRocks, Set<Coordinate> obstacles, List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            char[] charArray = line.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                var coord = new Coordinate(i, j);
                String s;
                if (newRocks.contains(coord)) {
                    s = "O";
                } else if (obstacles.contains(coord)) {
                    s = "#";
                } else {
                    s = ".";
                }
                System.out.print(s);
            }
            System.out.println();
        }


    }
}
