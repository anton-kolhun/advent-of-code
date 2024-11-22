package com.example.aoc_2020;

import com.example.aoc_2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class Day12 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        Coordinate north = new Coordinate(Position.NORTH, 0);
        Coordinate east = new Coordinate(Position.EAST, 0);
        Location currentLocation = new Location(north, east, Position.EAST);
        List<String> lines = FilesUtils.readFile("aoc_2020/day12.txt");
        for (String line : lines) {
            String dir = line.substring(0, 1);
            if (dir.equals("R") || dir.equals("L")) {
                rotateLocation(currentLocation, dir, Integer.parseInt(line.substring(1)));
                continue;
            }
            if (dir.equals("F")) {
                moveForward(currentLocation, Integer.parseInt(line.substring(1)));
                continue;
            }
            Position direction = switch (dir) {
                case "N" -> Position.NORTH;
                case "E" -> Position.EAST;
                case "S" -> Position.SOUTH;
                case "W" -> Position.WEST;
                default -> null;
            };
            moveLocation(currentLocation, direction, Integer.parseInt(line.substring(1)));
        }
        int dist = Math.abs(currentLocation.east.distance) + Math.abs(currentLocation.north.distance);
        System.out.println(dist);
    }

    private static void rotateLocation(Location currentLocation, String dir, int angle) {
//        System.out.println("before rotation currentDir= " + currentLocation.direction);
//        System.out.println("direction=" +  dir + "; angle=" + angle);
        Position direction = rotateDirection(currentLocation.direction, dir, angle);
        currentLocation.direction = direction;
//        System.out.println("after rotation currentLocation= " + currentLocation.direction);
//        System.out.println();
    }

    private static Position rotateDirection(Position currentDirection, String dir, int angle) {
        int rotations = angle / 90;
        if (dir.equals("R")) {
            for (int i = 0; i < rotations; i++) {
                currentDirection = currentDirection.rotateRight();
            }
        } else {
            for (int i = 0; i < rotations; i++) {
                currentDirection = currentDirection.rotateLeft();
            }
        }
        return currentDirection;
    }

    private static void rotateWaypoint(Waypoint waypoint, String dir, int angle) {
        var coord1 = waypoint.getCoord1();
        var coord2 = waypoint.getCoord2();
        coord1.position = rotateDirection(coord1.position, dir, angle);
        coord2.position = rotateDirection(coord2.position, dir, angle);
    }


    private static void moveLocation(Location currentLocation, Position direction, int steps) {
        if (direction == Position.NORTH) {
            currentLocation.north.distance = currentLocation.north.distance + steps;
        } else if (direction == Position.SOUTH) {
            currentLocation.north.distance = currentLocation.north.distance - steps;
        } else if (direction == Position.EAST) {
            currentLocation.east.distance = currentLocation.east.distance + steps;
        } else if (direction == Position.WEST) {
            currentLocation.east.distance = currentLocation.east.distance - steps;
        }
    }

    private static void moveWaypoint(Waypoint waypoint, Position direction, int steps) {
        var coord1 = waypoint.getCoord1();
        var coord2 = waypoint.getCoord2();

        if (coord1.position == direction) {
            coord1.distance = coord1.distance + steps;
        } else if (coord1.position == direction.invert()) {
            coord1.distance = coord1.distance - steps;
        }

        if (coord2.position == direction) {
            coord2.distance = coord2.distance + steps;
        } else if (coord2.position == direction.invert()) {
            coord2.distance = coord2.distance - steps;
        }
    }


    private static void moveForward(Location currentLocation, int steps) {
        System.out.println("before forward currentLocation= " + currentLocation);
        System.out.println("steps = " + steps);
        moveLocation(currentLocation, currentLocation.direction, steps);
        System.out.println("after forward currentLocation= " + currentLocation);
        System.out.println();
    }

    private static void moveShipToWaypoint(Location currentLocation, Waypoint waypoint, int steps) {
        for (int i = 0; i < steps; i++) {
            moveLocation(currentLocation, waypoint.coord1.position, waypoint.coord1.distance);
            moveLocation(currentLocation, waypoint.coord2.position, waypoint.coord2.distance);
        }
    }

    private enum Position {
        EAST, WEST, NORTH, SOUTH;

        public Position rotateRight() {
            if (this == NORTH) {
                return EAST;
            }
            if (this == EAST) {
                return SOUTH;
            }
            if (this == SOUTH) {
                return WEST;
            }
            if (this == WEST) {
                return NORTH;
            }
            return null;
        }

        public Position rotateLeft() {
            if (this == NORTH) {
                return WEST;
            }
            if (this == WEST) {
                return SOUTH;
            }
            if (this == SOUTH) {
                return EAST;
            }
            if (this == EAST) {
                return NORTH;
            }
            return null;
        }

        public Position invert() {
            if (this == NORTH) {
                return SOUTH;
            }
            if (this == WEST) {
                return EAST;
            }
            if (this == SOUTH) {
                return NORTH;
            }
            if (this == EAST) {
                return WEST;
            }
            return null;
        }


    }

    @Data
    @AllArgsConstructor
    private static class Coordinate {
        private Position position;
        private int distance;
    }

    @Data
    @AllArgsConstructor
    private static class Location {
        private Coordinate north;
        private Coordinate east;
        private Position direction;
    }

    @Data
    @AllArgsConstructor
    private static class Waypoint {
        private Coordinate coord1;
        private Coordinate coord2;
    }


    public static void task2() {
        Coordinate north = new Coordinate(Position.NORTH, 0);
        Coordinate east = new Coordinate(Position.EAST, 0);
        Location currentLocation = new Location(north, east, Position.EAST);
        Waypoint waypoint = new Waypoint(new Coordinate(Position.NORTH, 1), new Coordinate(Position.EAST, 10));
        List<String> lines = FilesUtils.readFile("aoc_2020/day12.txt");
        for (String line : lines) {
            String dir = line.substring(0, 1);
            if (dir.equals("R") || dir.equals("L")) {
                rotateWaypoint(waypoint, dir, Integer.parseInt(line.substring(1)));
                continue;
            }
            if (dir.equals("F")) {
                moveShipToWaypoint(currentLocation, waypoint, Integer.parseInt(line.substring(1)));
                continue;
            }
            Position direction = switch (dir) {
                case "N" -> Position.NORTH;
                case "E" -> Position.EAST;
                case "S" -> Position.SOUTH;
                case "W" -> Position.WEST;
                default -> null;
            };
            moveWaypoint(waypoint, direction, Integer.parseInt(line.substring(1)));
        }
        int dist = Math.abs(currentLocation.east.distance) + Math.abs(currentLocation.north.distance);
        System.out.println(dist);
    }

}
