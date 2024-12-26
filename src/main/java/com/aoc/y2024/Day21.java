package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.GraphUtils;
import com.aoc.y2023.helper.GraphUtils.CoordEdge;
import com.aoc.y2023.helper.GraphUtils.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Day21 {
    static Coordinate ACoord = new Coordinate(-100, -100);

    static List<Coordinate> moves = List.of(
            new Coordinate(0, 1),
            new Coordinate(0, -1),
            new Coordinate(1, 0),
            new Coordinate(-1, 0)
    );

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1And2();
    }


    private static BigInteger calculateTotalLength(String input, int depth,
                                                   Map<Route, List<List<Coordinate>>> routesDir,
                                                   Map<Character, Coordinate> buttonToCoordDir,
                                                   Map<Route, List<List<Coordinate>>> routesNum,
                                                   Map<Character, Coordinate> buttonToCoordNum,
                                                   Map<CacheKey, BigInteger> cache,
                                                   int maxDepth) {

        char[] charArray = input.toCharArray();
        BigInteger total = BigInteger.ZERO;
        Character previous = 'A';
        for (int i = 0; i < charArray.length; i++) {
            char current = charArray[i];
            var cacheKey = new CacheKey(previous, current, depth);
            if (cache.containsKey(cacheKey)) {
                BigInteger cached = cache.get(cacheKey);
                total = total.add(cached);
                //find min?
            } else {
                String inp = String.valueOf(current);
                List<String> options;
                if (depth == 0) {
                    options = processInput(previous, inp, buttonToCoordNum, routesNum);
                } else {
                    options = processInput(previous, inp, buttonToCoordDir, routesDir);
                }
                BigInteger minLength = null;
                if (depth == maxDepth) {
                    for (String option : options) {
                        if (minLength == null || option.length() < minLength.intValue()) {
                            minLength = BigInteger.valueOf(option.length());
                        }
                    }
                    total = total.add(minLength);
                } else {
                    for (String option : options) {
                        BigInteger solutionUp = calculateTotalLength(option, depth + 1, routesDir, buttonToCoordDir, routesNum, buttonToCoordNum, cache, maxDepth);
                        if (minLength == null || solutionUp.compareTo(minLength) < 0) {
                            minLength = solutionUp;
                        }
                    }
                    cache.put(cacheKey, minLength);
                    total = total.add(minLength);
                }
            }
            previous = current;
        }

        return total;

    }

    public static void task1And2() {
        ///block for Numerical
        Map<Coordinate, Character> coordToButtonNumer = new HashMap<>();
        coordToButtonNumer.put(new Coordinate(0, 0, 11), 'A');
        coordToButtonNumer.put(new Coordinate(0, -1, 0), '0');
        coordToButtonNumer.put(new Coordinate(-1, -2, 1), '1');
        coordToButtonNumer.put(new Coordinate(-1, -1, 2), '2');
        coordToButtonNumer.put(new Coordinate(-1, 0, 3), '3');
        coordToButtonNumer.put(new Coordinate(-2, -2, 4), '4');
        coordToButtonNumer.put(new Coordinate(-2, -1, 5), '5');
        coordToButtonNumer.put(new Coordinate(-2, 0, 6), '6');
        coordToButtonNumer.put(new Coordinate(-3, -2, 7), '7');
        coordToButtonNumer.put(new Coordinate(-3, -1, 8), '8');
        coordToButtonNumer.put(new Coordinate(-3, 0, 9), '9');

        Map<Character, Coordinate> buttonToCoordNumer = new HashMap<>();
        buttonToCoordNumer.put('A', new Coordinate(0, 0, 11));
        buttonToCoordNumer.put('0', new Coordinate(0, -1, 0));
        buttonToCoordNumer.put('1', new Coordinate(-1, -2, 1));
        buttonToCoordNumer.put('2', new Coordinate(-1, -1, 2));
        buttonToCoordNumer.put('3', new Coordinate(-1, 0, 3));
        buttonToCoordNumer.put('4', new Coordinate(-2, -2, 4));
        buttonToCoordNumer.put('5', new Coordinate(-2, -1, 5));
        buttonToCoordNumer.put('6', new Coordinate(-2, 0, 6));
        buttonToCoordNumer.put('7', new Coordinate(-3, -2, 7));
        buttonToCoordNumer.put('8', new Coordinate(-3, -1, 8));
        buttonToCoordNumer.put('9', new Coordinate(-3, 0, 9));
        List<CoordEdge> edgesNumer = fillEdges(coordToButtonNumer);
        Map<Route, List<List<Coordinate>>> routesNumer = findRoutes(coordToButtonNumer, edgesNumer, buttonToCoordNumer);

        Map<Coordinate, Character> coordToButtonDirect = new HashMap<>();
        coordToButtonDirect.put(new Coordinate(0, 0, 11), 'A');
        coordToButtonDirect.put(new Coordinate(0, -1, 0), '^');
        coordToButtonDirect.put(new Coordinate(1, -1, 1), 'v');
        coordToButtonDirect.put(new Coordinate(1, -2, 2), '<');
        coordToButtonDirect.put(new Coordinate(1, 0, 3), '>');

        Map<Character, Coordinate> buttonToCoordDirect = new HashMap<>();
        buttonToCoordDirect.put('A', new Coordinate(0, 0, 11));
        buttonToCoordDirect.put('^', new Coordinate(0, -1, 0));
        buttonToCoordDirect.put('v', new Coordinate(1, -1, 1));
        buttonToCoordDirect.put('<', new Coordinate(1, -2, 2));
        buttonToCoordDirect.put('>', new Coordinate(1, 0, 3));
        List<CoordEdge> edges = fillEdges(coordToButtonDirect);
        Map<Route, List<List<Coordinate>>> routesDir = findRoutes(coordToButtonDirect, edges, buttonToCoordDirect);


        List<String> lines = FilesUtils.readFile("aoc_2024/day21.txt");

        BigInteger totalTask2 = BigInteger.ZERO;
        BigInteger totalTask1 = BigInteger.ZERO;
        for (String line : lines) {
            Long intVal = parseLine(line);
            BigInteger valTask1 = calculateTotalLength(line, 0, routesDir, buttonToCoordDirect, routesNumer,
                    buttonToCoordNumer, new HashMap<>(), 2);
            totalTask1 = totalTask1.add(valTask1.multiply(BigInteger.valueOf(intVal)));

            BigInteger valTask2 = calculateTotalLength(line, 0, routesDir, buttonToCoordDirect, routesNumer,
                    buttonToCoordNumer, new HashMap<>(), 25);
            totalTask2 = totalTask2.add(valTask2.multiply(BigInteger.valueOf(intVal)));

        }
        System.out.println("task1 = " + totalTask1);
        System.out.println("task2 = " + totalTask2);

    }

    private static Long parseLine(String line) {
        StringBuffer val = new StringBuffer();
        for (char c : line.toCharArray()) {
            if (Character.isDigit(c)) {
                if ((c == '0') && val.isEmpty()) {
                    continue;
                }
                val.append(c);
            }
        }
        return Long.parseLong(val.toString());
    }


    private static List<String> processInput(Character previous, String input,
                                             Map<Character, Coordinate> buttonToCoord,
                                             Map<Route, List<List<Coordinate>>> routes) {
        var current = buttonToCoord.get(previous);
        List<List<Coordinate>> paths = doIt(current, routes, input, new ArrayList<>(), buttonToCoord);
        for (List<Coordinate> path : paths) {
            path.add(0, current);
        }

        List<String> pathDirections = paths.stream()
                .map(path -> getDirections(new ArrayList<>(path)))
                .toList();


        return pathDirections;
    }

    private static List<String> processInput(String input,
                                             Map<Character, Coordinate> buttonToCoord,
                                             Map<Route, List<List<Coordinate>>> routes) {
        return processInput('A', input, buttonToCoord, routes);
    }


    private static List<List<Coordinate>> doIt(Coordinate cursor, Map<Route, List<List<Coordinate>>> routes,
                                               String cmd, List<Coordinate> path,
                                               Map<Character, Coordinate> buttonToCoord) {
        if (cmd.isEmpty()) {
            return List.of(path);
        }

        Character nextChar = cmd.charAt(0);
        var nextCoord = buttonToCoord.get(nextChar);
        List<List<Coordinate>> total = new ArrayList<>();
        List<List<Coordinate>> possibleRoutes = routes.get(new Route(cursor, nextCoord));
        if (possibleRoutes == null) {
            var nextPath = new ArrayList<>(path);
            nextPath.add(ACoord);
            var res = doIt(nextCoord, routes, cmd.substring(1), nextPath, buttonToCoord);
            total.addAll(res);
        } else {
            for (List<Coordinate> possibleRoute : possibleRoutes) {
                var nextPath = new ArrayList<>(path);
                nextPath.addAll(possibleRoute.subList(1, possibleRoute.size()));
                nextPath.add(ACoord);
                var res = doIt(nextCoord, routes, cmd.substring(1), nextPath, buttonToCoord);
                total.addAll(res);
            }
        }
        return total;
    }


    private static String getDirections(List<Coordinate> coords) {
        StringBuffer total = new StringBuffer();
        for (int i = 0; i < coords.size() - 2; i++) {
            Coordinate current = coords.get(i);
            Coordinate next = coords.get(i + 1);
            while (next.equals(ACoord)) {
                total.append('A');
                i = i + 1;
                next = coords.get(i + 1);
            }
            Coordinate move = new Coordinate(next.row - current.row, next.col - current.col);
            Direction dir = Direction.from(move);
            total.append(dir.sign);

        }
        total.append('A');
        return total.toString();
    }

    private static Map<Route, List<List<Coordinate>>> findRoutes(Map<Coordinate, Character> coordToButton,
                                                                 List<CoordEdge> edges, Map<Character, Coordinate> buttonToCoord) {
        Map<Route, List<List<Coordinate>>> routes = new HashMap<>();
        Collection<Character> buttons = coordToButton.values();

        for (Character button1 : buttons) {
            var b1Coord = buttonToCoord.get(button1);
            for (Character button2 : buttons) {
                if (button1.equals(button2)) {
                    continue;
                }
                var b2Coord = buttonToCoord.get(button2);
                LinkedHashSet<Coordinate> path = new LinkedHashSet<>();
                path.add(b1Coord);
                List<List<Coordinate>> paths = GraphUtils.dejkstraMultiple(edges, b1Coord, b2Coord);
                routes.put(new Route(b1Coord, b2Coord), paths);
            }

        }
        return routes;
    }

    private static List<CoordEdge> fillEdges(Map<Coordinate, Character> coordToButton) {
        List<CoordEdge> edges = new ArrayList<>();
        for (Map.Entry<Coordinate, Character> entry : coordToButton.entrySet()) {
            var from = entry.getKey();
            for (Coordinate move : moves) {
                var neib = new Coordinate(from.row + move.row, from.col + move.col);
                if (coordToButton.containsKey(neib)) {
                    edges.add(new CoordEdge(from, neib));
                }
            }
        }
        return edges;
    }


    @AllArgsConstructor
    @Data
    static class Route {
        Coordinate from;
        Coordinate to;
    }

    private enum Direction {
        RIGHT(new Coordinate(0, 1), '>'), LEFT(new Coordinate(0, -1), '<'),
        UP(new Coordinate(-1, 0), '^'), DOWN(new Coordinate(1, 0), 'v');

        private Coordinate coord;
        private Character sign;

        Direction(Coordinate coord, Character sign) {
            this.coord = coord;
            this.sign = sign;
        }

        static Direction from(Coordinate from) {
            for (Direction value : values()) {
                if (value.coord.equals(from)) {
                    return value;
                }
            }
            return null;
        }

    }

    @Data
    @AllArgsConstructor
    static class CacheKey {
        Character previous;
        Character current;
        int depth;
    }

}
