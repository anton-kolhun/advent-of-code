package com.example.aoc_2024;

import com.example.aoc_2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day6 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day6.txt");
        Set<Coord> obstacles = new HashSet<>();
        Guard guard = null;
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == '#') {
                    obstacles.add(new Coord(row, col));
                } else if (c == '^') {
                    guard = new Guard(new Coord(row, col), Position.UP);
                } else if (c == 'v') {
                    guard = new Guard(new Coord(row, col), Position.DOWN);
                } else if (c == '>') {
                    guard = new Guard(new Coord(row, col), Position.RIGHT);
                } else if (c == '<') {
                    guard = new Guard(new Coord(row, col), Position.LEFT);
                }
            }
        }
        guard.setSize(lines.size(), lines.get(0).length());
        guard.setObstacles(obstacles);
        Set<Coord> usedCoord = new HashSet<>();
        usedCoord.add(guard.coord);
        while ((guard = guard.move()) != null) {
            usedCoord.add(guard.coord);
        }
        System.out.println(usedCoord.size());
    }


    public static void print(Set<Coord> obstacles, Guard guard, int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                var coord = new Coord(row, col);
                if (obstacles.contains(coord)) {
                    System.out.print("#");
                } else if (guard.coord.equals(coord)) {
                    System.out.print("G");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void print(Set<Coord> obstacles, Guard guard, int rows, int cols, Guard possibleObstacle) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                var coord = new Coord(row, col);
                if (obstacles.contains(coord)) {
                    System.out.print("#");
                } else if (guard.coord.equals(coord)) {
                    System.out.print("G");
                } else if (possibleObstacle.coord.equals(coord)) {
                    System.out.print("0");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }

        System.out.println();

    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day6.txt");
        Set<Coord> obstacles = new HashSet<>();
        Guard guard = null;
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                if (c == '#') {
                    obstacles.add(new Coord(row, col));
                } else if (c == '^') {
                    guard = new Guard(new Coord(row, col), Position.UP);
                } else if (c == 'v') {
                    guard = new Guard(new Coord(row, col), Position.DOWN);
                } else if (c == '>') {
                    guard = new Guard(new Coord(row, col), Position.RIGHT);
                } else if (c == '<') {
                    guard = new Guard(new Coord(row, col), Position.LEFT);
                }
            }
        }
        guard.setSize(lines.size(), lines.get(0).length());
        guard.setObstacles(obstacles);
        var initial = guard;
        Set<Guard> guards = new HashSet<>();
        guards.add(guard);
        Set<Coord> loops = new HashSet<>();

        Set<Coord> usedCoord = new HashSet<>();
        usedCoord.add(guard.coord);
        while (true) {
            boolean isCycle = guard.tryPathIfObstacleInFront(initial);
            if (isCycle) {
                loops.add(guard.tryMove().coord);
            }

            guard = guard.move();
            guards.add(guard);
            if (guard == null) {
                break;
            }
            usedCoord.add(guard.coord);
        }

        System.out.println(loops.size());
        checkResult(loops, initial);
    }

    private static void checkResult(Set<Coord> loopObstacles, Guard initial) {
        for (Coord newObstacle : loopObstacles) {
            var newGuard = new Guard(initial.coord, initial.position, initial.rows, initial.cols, initial.obstacles);
            var newObstacles = new HashSet<>(initial.obstacles);
            newObstacles.add(newObstacle);
            newGuard.setObstacles(newObstacles);
            Set<Guard> usedGuards = new HashSet<>();
            usedGuards.add(newGuard);
            boolean isCorrect = false;
            while ((newGuard = newGuard.move()) != null) {
                if (!usedGuards.add(newGuard)) {
                    isCorrect = true;
                    break;
                }
            }
            if (!isCorrect) {
                System.out.println(newObstacle);
            }
        }
    }


    @Data
    @AllArgsConstructor
    static class Coord {
        int row;
        int col;
    }

    private enum Position {
        RIGHT, LEFT, DOWN, UP;

        public Position rotateRight() {
            if (this == DOWN) {
                return LEFT;
            }
            if (this == RIGHT) {
                return DOWN;
            }
            if (this == UP) {
                return RIGHT;
            }
            if (this == LEFT) {
                return UP;
            }
            return null;
        }

    }

    @Data
    @AllArgsConstructor
    static class Guard {

        Coord coord;
        Position position;
        int rows;
        int cols;
        Set<Coord> obstacles;

        public void setSize(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        public void setObstacles(Set<Coord> obstacles) {
            this.obstacles = new HashSet<>(obstacles);
        }

        public Guard(Coord coord, Position position) {
            this.coord = coord;
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Guard guard = (Guard) o;
            return Objects.equals(coord, guard.coord) && position == guard.position;
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(coord);
            result = 31 * result + Objects.hashCode(position);
            return result;
        }

        public Guard move() {
            Coord nextCoord = null;
            if (position == Position.UP) {
                nextCoord = new Coord(coord.row - 1, coord.col);
            } else if (position == Position.DOWN) {
                nextCoord = new Coord(coord.row + 1, coord.col);
            } else if (position == Position.RIGHT) {
                nextCoord = new Coord(coord.row, coord.col + 1);
            } else if (position == Position.LEFT) {
                nextCoord = new Coord(coord.row, coord.col - 1);
            }
            if (nextCoord.col >= cols || nextCoord.col < 0 ||
                    nextCoord.row >= rows || nextCoord.row < 0) {
                return null;
            }
            if (obstacles.contains(nextCoord)) {
                position = position.rotateRight();
                nextCoord = coord;
            }
            return new Guard(nextCoord, position, rows, cols, obstacles);
        }

        public boolean tryPathIfObstacleInFront(Guard initial) {
            var tryGuard = tryMove();
            if (tryGuard == null) {
                return false;
            }
            if (tryGuard.coord.equals(coord)) {
                return false;
            }
            var possibleObstacles = new HashSet<>(obstacles);
            possibleObstacles.add(tryGuard.coord);
            Set<Guard> usedGuards = new HashSet<>();
            var guard = new Guard(initial.coord, initial.position, initial.rows, initial.cols, possibleObstacles);
            usedGuards.add(guard);
            while ((guard = guard.move()) != null) {
                if (!usedGuards.add(guard)) {
                    return true;
                }
            }
            return false;
        }

        public Guard tryMove() {
            var posGuard = new Guard(this.coord, this.position, rows, cols, obstacles);
            return posGuard.move();
        }
    }
}
