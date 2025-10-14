package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Day22 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day22.txt");
        Map<Coordinate, String> coordStates = new HashMap<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " ");
            String state = parts.get(0).trim();
            parts = ParseUtils.splitByDelimiter(parts.get(1), ",");
            List<String> xParts = ParseUtils.splitByDelimiter(parts.get(0), "=");
            xParts = ParseUtils.splitByDelimiter(xParts.get(1), "\\..");
            int x1 = Integer.parseInt(xParts.get(0));
            int x2 = Integer.parseInt(xParts.get(1));
            List<String> yParts = ParseUtils.splitByDelimiter(parts.get(1), "=");
            yParts = ParseUtils.splitByDelimiter(yParts.get(1), "\\..");
            int y1 = Integer.parseInt(yParts.get(0));
            int y2 = Integer.parseInt(yParts.get(1));
            List<String> zParts = ParseUtils.splitByDelimiter(parts.get(2), "=");
            zParts = ParseUtils.splitByDelimiter(zParts.get(1), "\\..");
            int z1 = Integer.parseInt(zParts.get(0));
            int z2 = Integer.parseInt(zParts.get(1));

            List<Coordinate> coordinates = fillCoords(x1, x2, y1, y2, z1, z2);
            for (Coordinate coordinate : coordinates) {
                coordStates.put(coordinate, state);
            }
        }
        long enabledCounter = coordStates.values().stream().filter(s -> s.equals("on")).count();
        System.out.println("task1: " + enabledCounter);
    }

    private static void task2() {
        // Inclusion–exclusion principle:
       // |A u B u C| = |A u B| + |A u C| + |B u C| - (|A n B| + |A n C| + |B n C|) + |A n B n C |
        List<String> lines = FilesUtils.readFile("aoc_2021/day22.txt");
        List<Cube> cubes = new ArrayList<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " ");
            String state = parts.get(0).trim();
            parts = ParseUtils.splitByDelimiter(parts.get(1), ",");
            List<String> xParts = ParseUtils.splitByDelimiter(parts.get(0), "=");
            xParts = ParseUtils.splitByDelimiter(xParts.get(1), "\\..");
            int x1 = Integer.parseInt(xParts.get(0));
            int x2 = Integer.parseInt(xParts.get(1));
            List<String> yParts = ParseUtils.splitByDelimiter(parts.get(1), "=");
            yParts = ParseUtils.splitByDelimiter(yParts.get(1), "\\..");
            int y1 = Integer.parseInt(yParts.get(0));
            int y2 = Integer.parseInt(yParts.get(1));
            List<String> zParts = ParseUtils.splitByDelimiter(parts.get(2), "=");
            zParts = ParseUtils.splitByDelimiter(zParts.get(1), "\\..");
            int z1 = Integer.parseInt(zParts.get(0));
            int z2 = Integer.parseInt(zParts.get(1));

            int xMin = Math.min(x1, x2);
            int xMax = Math.max(x1, x2);
            int yMin = Math.min(y1, y2);
            int yMax = Math.max(y1, y2);
            int zMin = Math.min(z1, z2);
            int zMAx = Math.max(z1, z2);
            Cube newCube = new Cube(xMin, xMax, yMin, yMax, zMin, zMAx, 1);
            List<Cube> toAddCubes = new ArrayList<>();
            for (Cube cube : cubes) {
                Optional<Cube> intersection = getIntersection(cube, newCube);
                intersection.ifPresent(toAddCubes::add);
            }
            cubes.addAll(toAddCubes);
            if (state.equals("on")) {
                cubes.add(newCube);
            }

        }
        BigInteger enabledCounter = calcTotal(cubes);
        System.out.println("task2: " + enabledCounter);
    }

    private static Optional<Cube> getIntersection(Cube cube, Cube newCube) {
        int xMin = Math.max(cube.xStart, newCube.xStart);
        int xMax = Math.min(cube.xEnd, newCube.xEnd);
        int yMin = Math.max(cube.yStart, newCube.yStart);
        int yMax = Math.min(cube.yEnd, newCube.yEnd);
        int zMin = Math.max(cube.zStart, newCube.zStart);
        int zMax = Math.min(cube.zEnd, newCube.zEnd);
        if (xMin > xMax || yMin > yMax || zMin > zMax) {
            return Optional.empty();
        }
        return Optional.of(new Cube(xMin, xMax, yMin, yMax, zMin, zMax, -cube.sign * newCube.sign));
    }


    private static BigInteger calcTotal(List<Cube> cubes) {
        BigInteger sum = BigInteger.ZERO;
        for (Cube cube : cubes) {
            BigInteger value = wrapWithBigInt(cube.xEnd - cube.xStart + 1)
                    .multiply(wrapWithBigInt(cube.yEnd - cube.yStart + 1))
                    .multiply(wrapWithBigInt(cube.zEnd - cube.zStart + 1))
                    .multiply(wrapWithBigInt(cube.sign));
            sum = sum.add(value);
        }
        return sum;
    }

    private static BigInteger wrapWithBigInt(int value) {
        return BigInteger.valueOf(value);
    }


    private static List<Coordinate> fillCoords(int x1, int x2, int y1, int y2, int z1, int z2) {
        int xMin = Math.min(x1, x2);
        if (xMin < -50) {
            xMin = -50;
        }
        int xMax = Math.max(x1, x2);
        if (xMax > 50) {
            xMax = 50;
        }
        int yMin = Math.min(y1, y2);
        if (yMin < -50) {
            yMin = -50;
        }
        int yMax = Math.max(y1, y2);
        if (yMax > 50) {
            yMax = 50;
        }
        int zMin = Math.min(z1, z2);
        if (zMin < -50) {
            zMin = -50;
        }
        int zMAx = Math.max(z1, z2);
        if (zMin > 50) {
            zMAx = 50;
        }
        List<Coordinate> coordinates = new ArrayList<>();
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMAx; z++) {
                    coordinates.add(new Coordinate(x, y, z));
                }
            }
        }
        return coordinates;
    }


    private static class Coordinate {
        int x;
        int y;
        int z;

        public Coordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }

    private static class Cube {
        int xStart;
        int yStart;
        int zStart;
        int xEnd;
        int yEnd;
        int zEnd;
        int sign;

        public Cube(int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd, int sign) {
            this.sign = sign;
            this.xStart = xStart;
            this.yStart = yStart;
            this.zStart = zStart;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
            this.zEnd = zEnd;
        }
    }

}
