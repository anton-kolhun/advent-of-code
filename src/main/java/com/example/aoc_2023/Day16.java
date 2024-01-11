package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.GraphUtils.Coordinate;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day16 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("day16.txt");
        Map<Coordinate, Character> coords = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coords.put(new Coordinate(row, col), c);
            }
        }

        int max = Integer.MIN_VALUE;
        for (int row = 0; row < lines.size(); row++) {
            int res = move(new Coordinate(row, -1), Direction.RIGHT, coords, lines);
            if (res > max) {
                max = res;
            }
        }
        for (int row = 0; row < lines.size(); row++) {
            int res = move(new Coordinate(row, lines.get(0).length()), Direction.LEFT, coords, lines);
            if (res > max) {
                max = res;
            }
        }

        for (int col = 0; col < lines.get(0).length(); col++) {
            int res = move(new Coordinate(-1, col), Direction.DOWN, coords, lines);
            if (res > max) {
                max = res;
            }
        }
        for (int col = 0; col < lines.get(0).length(); col++) {
            int res = move(new Coordinate(lines.size(), col), Direction.UP, coords, lines);
            if (res > max) {
                max = res;
            }
        }
        System.out.println(max);
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("day16.txt");
        Map<Coordinate, Character> coords = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                coords.put(new Coordinate(row, col), c);
            }
        }

        move(new Coordinate(0, -1), Direction.RIGHT, coords, lines);
        //move(new Coordinate(0, -1), Direction.RIGHT, coords, lines);


    }

    private static int move(Coordinate cursor, Direction dir, Map<Coordinate, Character> coords, List<String> lines) {
        List<CoordInfo> cursors = new ArrayList<>();
        cursors.add(new CoordInfo(cursor, dir));
        Set<Coordinate> visited = new HashSet<>();
        boolean isCycle = false;
        visited.add(cursor);
        int counter = 0;
        while (!isCycle) {
            // print(cursors, lines);
            //print(visited, lines);
            List<CoordInfo> nextCursors = new ArrayList<>();
            Set<Coordinate> nextVisited = new HashSet<>(visited);
            for (CoordInfo curs : cursors) {
                Coordinate locatedAt = new Coordinate(curs.coord.row + curs.dir.getShift().row, curs.coord.col + curs.dir.getShift().col);
                if (locatedAt.row < 0 || locatedAt.row >= lines.size() || locatedAt.col < 0 || locatedAt.col >= lines.get(0).length()) {
                    continue;
                }
                nextVisited.add(locatedAt);
                Character sym = coords.get(locatedAt);
                doMove(nextCursors, curs, locatedAt, sym);
            }
            cursors = nextCursors;
            if (visited.equals(nextVisited)) {
                counter++;
                if (counter > 10) {
                    isCycle = true;
                }
            } else {
                counter = 0;
            }
            visited = new HashSet<>(nextVisited);
        }

        System.out.println(visited.size() - 1);
        return visited.size() - 1;
    }

    private static void doMove(List<CoordInfo> nextCursors, CoordInfo curs, Coordinate locatedAt, Character sym) {
        if (curs.dir == Direction.RIGHT) {
            if (sym == '-') {
                nextCursors.add(new CoordInfo(locatedAt, Direction.RIGHT));
            } else if (sym == '|') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.DOWN));
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.UP));
            } else if (sym == '/') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.UP));
            } else if (sym == '\\') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.DOWN));
            } else if (sym == '.') {
                nextCursors.add(new CoordInfo(locatedAt, Direction.RIGHT));
            }
        } else if (curs.dir == Direction.LEFT) {
            if (sym == '-') {
                nextCursors.add(new CoordInfo(locatedAt, Direction.LEFT));
            } else if (sym == '|') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.DOWN));
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.UP));
            } else if (sym == '/') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.DOWN));
            } else if (sym == '\\') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.UP));
            } else if (sym == '.') {
                nextCursors.add(new CoordInfo(locatedAt, Direction.LEFT));
            }
        } else if (curs.dir == Direction.DOWN) {
            if (sym == '-') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.LEFT));
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.RIGHT));
            } else if (sym == '|') {
                nextCursors.add(new CoordInfo(locatedAt, Direction.DOWN));
            } else if (sym == '/') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.LEFT));
            } else if (sym == '\\') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.RIGHT));
            } else if (sym == '.') {
                nextCursors.add(new CoordInfo(locatedAt, Direction.DOWN));
            }
        } else if (curs.dir == Direction.UP) {
            if (sym == '-') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.LEFT));
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.RIGHT));
            } else if (sym == '|') {
                nextCursors.add(new CoordInfo(locatedAt, Direction.UP));
            } else if (sym == '/') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.RIGHT));
            } else if (sym == '\\') {
                nextCursors.add(new CoordInfo(new Coordinate(locatedAt.row, locatedAt.col), Direction.LEFT));
            } else if (sym == '.') {
                nextCursors.add(new CoordInfo(locatedAt, Direction.UP));
            }
        }
    }

    private static void print(Set<Coordinate> visited, List<String> lines) {
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                if (visited.contains(new Coordinate(row, col))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");

                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void print(List<CoordInfo> cursors, List<String> lines) {
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                boolean isVisited = false;
                for (CoordInfo cursor : cursors) {
                    if (cursor.coord.equals(new Coordinate(row, col))) {
                        isVisited = true;
                        break;
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static enum Direction {
        RIGHT(new Coordinate(0, 1)), LEFT(new Coordinate(0, -1)), DOWN(new Coordinate(1, 0)), UP(new Coordinate(-1, 0));

        public Coordinate getShift() {
            return shift;
        }

        private Coordinate shift;

        private Direction(Coordinate coord) {
            this.shift = coord;
        }

    }

    @AllArgsConstructor
    private static class CoordInfo {
        private Coordinate coord;
        private Direction dir;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CoordInfo coordInfo = (CoordInfo) o;

            if (!Objects.equals(coord, coordInfo.coord)) return false;
            return dir == coordInfo.dir;
        }

        @Override
        public int hashCode() {
            int result = coord != null ? coord.hashCode() : 0;
            result = 31 * result + (dir != null ? dir.hashCode() : 0);
            return result;
        }
    }
}
