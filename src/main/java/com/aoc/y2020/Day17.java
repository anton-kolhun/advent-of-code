package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day17 {


    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day17.txt");
        Map<Coord, Integer> states = new HashMap<>();
        int z = 0;
        for (int x = 0; x < lines.size(); x++) {
            String line = lines.get(x);
            char[] charArray = line.toCharArray();
            for (int y = 0; y < charArray.length; y++) {
                char state = charArray[y];
                if (state == '#') {
                    states.put(new Coord(x, y, z), 1);
                } else {
                    states.put(new Coord(x, y, z), 0);
                }
            }
        }

        for (int cycle = 0; cycle < 6; cycle++) {
            var nextState = new HashMap<Coord, Integer>();
            Set<Coord> newNeibs = new HashSet<>();
            for (Map.Entry<Coord, Integer> entry : states.entrySet()) {
                Coord coord = entry.getKey();
                List<Coord> neighbours = getNeighbours(coord);
                newNeibs.addAll(neighbours);
                int totalActive = 0;
                for (Coord neighbour : neighbours) {
                    totalActive += states.getOrDefault(neighbour, 0);
                }

                if (entry.getValue() == 0) {
                    if (totalActive == 3) {
                        nextState.put(coord, 1);
                    } /*else {
                        nextState.put(coord, 0);
                    }*/
                } else {
                    totalActive = totalActive - 1; //decrement self value
                    if (totalActive == 3 || totalActive == 2) {
                        nextState.put(coord, 1);
                    } /*else {
                        nextState.put(coord, 0);
                    }*/
                }
            }

            for (Coord newNeib : newNeibs) {
                if (!states.containsKey(newNeib)) {
                    var neibs2 = getNeighbours(newNeib);
                    int totalActive = 0;
                    for (Coord neighbour : neibs2) {
                        totalActive += states.getOrDefault(neighbour, 0);
                    }
                    if (totalActive == 3 ) {
                        nextState.put(newNeib, 1);
                    }
                }
            }
            states = nextState;
        }
        System.out.println(states.size());

    }

    private static List<Coord> getNeighbours(Coord coord) {
        List<Coord> neighbours = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    neighbours.add(new Coord(coord.getX() + x, coord.getY() + y, coord.getZ() + z));
                }
            }
        }
        return neighbours;
    }


    @Data
    @AllArgsConstructor
    static class Coord {
        private int x;
        private int y;
        private int z;
    }
}
