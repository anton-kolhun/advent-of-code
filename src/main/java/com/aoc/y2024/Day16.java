package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Day16 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1And2();
    }


    public static void task1And2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day16.txt");
        Map<Coord, Character> charToCoord = new HashMap<>();
        Coord start;
        State startState = null;
        Coord end = null;
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                charToCoord.put(new Coord(row, col), c);
                if (c == 'S') {
                    start = new Coord(row, col);
                    startState = new State(start, Position.RIGHT);
                } else if (c == 'E') {
                    end = new Coord(row, col);
                }
            }
        }

        List<StateWrapper> stateWrappers = dejkstra(charToCoord, startState, end);

        System.out.println(stateWrappers.get(0).cost());

        Set<Coord> coords = new HashSet<>();
        for (StateWrapper stateWrapper : stateWrappers) {
            for (State state : stateWrapper.states) {
                coords.add(state.coord);
            }
        }

        System.out.println(coords.size());


    }

    private static List<StateWrapper> dejkstra(Map<Coord, Character> charToCoord, State startState, Coord end) {
        List<State> path = new ArrayList<>();
        path.add(startState);
        Map<State, List<StateWrapper>> stateToDist = new HashMap<>();
        Set<State> visited = new HashSet<>();

        stateToDist.put(startState, List.of(new StateWrapper(path)));
        while (true) {
            State min = findMin(stateToDist, visited);
            if (min.coord.equals(end)) {
                return stateToDist.get(min);
            }
            var stateCoord = new Coord(min.coord.row + min.position.coord.row, min.coord.col + min.position.coord.col);
            var state = new State(stateCoord, min.position);
            handleMove(charToCoord, stateToDist, min, state, 0);

            var rotatePos = min.position.rotateLeft();
            stateCoord = new Coord(min.coord.row + rotatePos.coord.row, min.coord.col + rotatePos.coord.col);
            state = new State(stateCoord, rotatePos);
            handleMove(charToCoord, stateToDist, min, state, 1000);

            rotatePos = min.position.rotateRight();
            stateCoord = new Coord(min.coord.row + rotatePos.coord.row, min.coord.col + rotatePos.coord.col);
            state = new State(stateCoord, rotatePos);
            handleMove(charToCoord, stateToDist, min, state, 1000);
        }
    }

    private static void handleMove(Map<Coord, Character> charToCoord,
                                   Map<State, List<StateWrapper>> stateToDist, State min, State state, int extraScore) {
        if (charToCoord.get(state.coord) == '.' || charToCoord.get(state.coord) == 'E') {
            var viaMinCost = stateToDist.get(min).get(0).cost() + 1 + extraScore;
            var curPath = stateToDist.get(state);
            long cutCost;
            if (curPath == null) {
                cutCost = Long.MAX_VALUE;
            } else {
                cutCost = curPath.get(0).cost();
            }
            if (viaMinCost < cutCost) {
                List<StateWrapper> newWrappers = new ArrayList<>();
                for (StateWrapper stateWrapper : stateToDist.get(min)) {
                    var newStates = new ArrayList<>(stateWrapper.states);
                    newStates.add(state);
                    newWrappers.add(new StateWrapper(newStates));
                }
                stateToDist.put(state, newWrappers);
            } else if (viaMinCost == cutCost) {
                List<StateWrapper> newWrappers = new ArrayList<>(stateToDist.get(state));
                for (StateWrapper stateWrapper : stateToDist.get(min)) {
                    var newStates = new ArrayList<>(stateWrapper.states);
                    newStates.add(state);
                    newWrappers.add(new StateWrapper(newStates));
                }
                stateToDist.put(state, newWrappers);
            }
        }
    }

    private static State findMin(Map<State, List<StateWrapper>> stateToDist, Set<State> visited) {
        long minDist = Long.MAX_VALUE;
        State minState = null;
        for (Map.Entry<State, List<StateWrapper>> entry : stateToDist.entrySet()) {
            if (!visited.contains(entry.getKey()) && entry.getValue().get(0).cost() < minDist) {
                minDist = entry.getValue().get(0).cost();
                minState = entry.getKey();
            }
        }
        visited.add(minState);
        return minState;
    }


    @Data
    @AllArgsConstructor
    private static class Coord {
        int row;
        int col;

        public String toString() {
            return String.format("row=%s,col=%s ", row, col);
        }
    }


    private enum Position {
        RIGHT(new Coord(0, 1)), LEFT(new Coord(0, -1)),
        UP(new Coord(-1, 0)), DOWN(new Coord(1, 0));

        private Coord coord;

        private Position(Coord coord) {
            this.coord = coord;
        }

        public Position rotateRight() {
            if (this == UP) {
                return RIGHT;
            }
            if (this == RIGHT) {
                return DOWN;
            }
            if (this == DOWN) {
                return LEFT;
            }
            if (this == LEFT) {
                return UP;
            }
            return null;
        }

        public Position rotateLeft() {
            if (this == UP) {
                return LEFT;
            }
            if (this == LEFT) {
                return DOWN;
            }
            if (this == DOWN) {
                return RIGHT;
            }
            if (this == RIGHT) {
                return UP;
            }
            return null;
        }

        public Position invert() {
            if (this == UP) {
                return DOWN;
            }
            if (this == LEFT) {
                return RIGHT;
            }
            if (this == DOWN) {
                return UP;
            }
            if (this == RIGHT) {
                return LEFT;
            }
            return null;
        }
    }

    @AllArgsConstructor
    @Data
    static class State {
        Coord coord;
        Position position;

        public String toString() {
            return coord.toString();
        }

    }

    @AllArgsConstructor
    @Data
    static class Edge {
        State from;
        State to;
        long weight;
    }

    @AllArgsConstructor
    static class StateWrapper {
        List<State> states;

        int cost() {
            int sum = 0;
            for (int i = 0; i < states.size() - 1; i++) {
                State current = states.get(i);
                State next = states.get(i + 1);
                if (current.position != next.position) {
                    sum = sum + 1000;
                }
                sum = sum + 1;
            }
            return sum;
        }

    }

}