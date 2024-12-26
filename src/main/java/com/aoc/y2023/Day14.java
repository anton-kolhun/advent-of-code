package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day14 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
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

        Set<Coordinate> newRocks = new HashSet<>();

        for (int row = 0; row < lines.size(); row++){
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                var coord = new Coordinate(row, col);
                if (rocks.contains(coord)) {
                    moveRockUp(obstacles, newRocks, coord);
                }

            }
        }

        long totalSum = 0;
        for (Coordinate newRock : newRocks) {
            totalSum += lines.size() - newRock.row;
        }
        System.out.println(totalSum);
        print(newRocks, obstacles, lines);
    }

    private static void moveRockUp(Set<Coordinate> obstacles, Set<Coordinate> newRocks, Coordinate rock) {
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
