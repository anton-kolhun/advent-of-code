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

public class Day17Part2 {


    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day17.txt");
        Map<CoordPart2, Integer> states = new HashMap<>();
        int z = 0;
        int w = 0;
        for (int x = 0; x < lines.size(); x++) {
            String line = lines.get(x);
            char[] charArray = line.toCharArray();
            for (int y = 0; y < charArray.length; y++) {
                char state = charArray[y];
                if (state == '#') {
                    states.put(new CoordPart2(x, y, z, w), 1);
                } else {
                    states.put(new CoordPart2(x, y, z, w), 0);
                }
            }
        }

        for (int cycle = 0; cycle < 6; cycle++) {
            var nextState = new HashMap<CoordPart2, Integer>();
            Set<CoordPart2> newNeibs = new HashSet<>();
            for (Map.Entry<CoordPart2, Integer> entry : states.entrySet()) {
                CoordPart2 CoordPart2 = entry.getKey();
                List<CoordPart2> neighbours = getNeighbours(CoordPart2);
                newNeibs.addAll(neighbours);
                int totalActive = 0;
                for (CoordPart2 neighbour : neighbours) {
                    totalActive += states.getOrDefault(neighbour, 0);
                }

                if (entry.getValue() == 0) {
                    if (totalActive == 3) {
                        nextState.put(CoordPart2, 1);
                    } /*else {
                        nextState.put(CoordPart2, 0);
                    }*/
                } else {
                    totalActive = totalActive - 1; //decrement self value
                    if (totalActive == 3 || totalActive == 2) {
                        nextState.put(CoordPart2, 1);
                    } /*else {
                        nextState.put(CoordPart2, 0);
                    }*/
                }
            }

            for (CoordPart2 newNeib : newNeibs) {
                if (!states.containsKey(newNeib)) {
                    var neibs2 = getNeighbours(newNeib);
                    int totalActive = 0;
                    for (CoordPart2 neighbour : neibs2) {
                        totalActive += states.getOrDefault(neighbour, 0);
                    }
                    if (totalActive == 3) {
                        nextState.put(newNeib, 1);
                    }
                }
            }
            states = nextState;
        }
        System.out.println(states.size());

    }

    private static List<CoordPart2> getNeighbours(CoordPart2 CoordPart2) {
        List<CoordPart2> neighbours = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    for (int w = -1; w <= 1; w++) {
                        neighbours.add(new CoordPart2(CoordPart2.getX() + x, CoordPart2.getY() + y, CoordPart2.getZ() + z, CoordPart2.getW() + w));
                    }
                }
            }
        }
        return neighbours;
    }


    @Data
    @AllArgsConstructor
    static class CoordPart2 {
        private int x;
        private int y;
        private int z;
        private int w;
    }
}
