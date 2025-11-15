package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils.Coordinate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 {

    public static void main(String[] args) {
        task();
    }

    public static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day10.txt");
        List<Coordinate> asteroids = new ArrayList<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == '#') {
                    asteroids.add(new Coordinate(row, col));
                }
            }
        }
//        print(asteroids);

        int maxCounter = Integer.MIN_VALUE;
        Coordinate maxCoord = null;
        for (int i = 0; i < asteroids.size(); i++) {
            Coordinate ast1 = asteroids.get(i);
            int counter = 0;
            for (int j = 0; j < asteroids.size(); j++) {
                Coordinate ast2 = asteroids.get(j);
                boolean isGood = noAsteroidsOnMyWay(ast1, ast2, new HashSet<>(asteroids));
                if (isGood) {
                    counter++;
                }
            }
            if (counter > maxCounter) {
                maxCounter = counter;
                maxCoord = ast1;
            }
        }
        System.out.println("task1: " + maxCounter);

        asteroids.remove(maxCoord);
        Coordinate superMax = maxCoord;
        asteroids = asteroids.stream()
                .sorted((o1, o2) -> {
                    double angle1 = getAngle(o1, superMax);
                    double angle2 = getAngle(o2, superMax);
                    if (angle1 < angle2) {
                        return -1;
                    } else if (angle1 > angle2) {
                        return 1;
                    } else {
                        double dist1 = getDistance(o1, superMax);
                        double dist2 = getDistance(o2, superMax);
                        if (dist1 < dist2) {
                            return -1;
                        } else if (dist1 > dist2) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                })
                .toList();


        var coord = getNthVisitedCoordinate(asteroids, superMax, 200);
        int res = coord.row + coord.col * 100;
        System.out.println("task2: " + res);
    }

    private static Coordinate getNthVisitedCoordinate(List<Coordinate> asteroids, Coordinate superMax, int n) {
        List<Coordinate> toCheck = new ArrayList<>(asteroids);
        int counter = 0;
        while (!toCheck.isEmpty()) {
            List<Coordinate> nextToCheck = new ArrayList<>();
            Set<Coordinate> visited = new HashSet<>();
            for (Coordinate asteroid : toCheck) {
                boolean isGood = noAsteroidsOnMyWay(superMax, asteroid, visited);
                if (isGood) {
                    visited.add(asteroid);
                    counter++;
                    if (counter == n) {
                        return asteroid;
                    }
                } else {
                    nextToCheck.add(asteroid);
                }
            }
            toCheck = nextToCheck;
        }
        return null;
    }

    private static double getDistance(Coordinate o1, Coordinate superMax) {
        double diffRow = Math.abs(o1.row - superMax.row);
        double diffCol = Math.abs(o1.col - superMax.col);
        return Math.sqrt(diffRow * diffRow + diffCol * diffCol);
    }

    private static double getAngle(Coordinate o1, Coordinate superMax) {
        double deltaRow = superMax.row - o1.row;
        double deltaCol = -superMax.col + o1.col;
        double val = Math.atan2(deltaCol, deltaRow);
        double angle = Math.toDegrees(val);
        if (angle < 0) {
            return 360 + angle;
        }
        return angle;
    }

    private static void print(List<Coordinate> asteroids) {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                if (asteroids.contains(new Coordinate(row, col))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    private static boolean noAsteroidsOnMyWay(Coordinate ast1, Coordinate ast2, Set<Coordinate> asteroids) {
        if (ast1.equals(ast2)) {
            return false;
        }
        int rowDiff = Math.abs(ast1.row - ast2.row);
        int colDiff = Math.abs(ast1.col - ast2.col);
        BigInteger step = BigInteger.valueOf(rowDiff).gcd(BigInteger.valueOf(colDiff));
        int rowStep = rowDiff / step.intValue();
        int colStep = colDiff / step.intValue();
        if (rowDiff == 0) {
            rowStep = 0;
        }
        if (colDiff == 0) {
            colStep = 0;
        }
        int rowKoef;
        if (ast1.row < ast2.row) {
            rowKoef = 1;
        } else {
            rowKoef = -1;
        }
        int colKoef;
        if (ast1.col < ast2.col) {
            colKoef = 1;
        } else {
            colKoef = -1;
        }
        int currentRow = ast1.row;
        int currentCol = ast1.col;
        while (!new Coordinate(currentRow, currentCol).equals(ast2)) {
            currentRow = currentRow + rowStep * rowKoef;
            currentCol = currentCol + colStep * colKoef;
            var next = new Coordinate(currentRow, currentCol);
            if (!next.equals(ast2) && asteroids.contains(next)) {
                return false;
            }
        }
        return true;
    }

}
