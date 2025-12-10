package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class Day10 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day10.txt");
        int total = 0;
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " ");
            String indicators = parts.getFirst().substring(1, parts.getFirst().length() - 1);
            char[] charArray = indicators.toCharArray();
            List<Boolean> state = new ArrayList<>();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c == '.') {
                    state.add(i, false);
                } else {
                    state.add(i, true);
                }
            }
            List<Button> buttons = new ArrayList<>();
            for (int i = 1; i < parts.size() - 1; i++) {
                String part = parts.get(i);
                part = part.substring(1, part.length() - 1);
                List<Integer> toggles = ParseUtils.splitByDelimiter(part, ",").stream()
                        .map(Integer::parseInt).toList();
                buttons.add(new Button(toggles));
            }
            int res = calcShortestWay(state, buttons);
            total += res - 1;
        }
        System.out.println("task1: " + total);
    }

    private static int calcShortestWay(List<Boolean> state, List<Button> buttons) {
        List<Boolean> currentState = new ArrayList<>();
        for (Boolean b : state) {
            currentState.add(false);
        }
        LinkedHashSet<List<Boolean>> statePath = new LinkedHashSet<>();
        statePath.add(currentState);
        Integer res = doCalcShortestWay(state, buttons, statePath, new HashMap<>());
        return res;
    }

    private static int doCalcShortestWay(List<Boolean> state, List<Button> buttons, LinkedHashSet<List<Boolean>> statePath,
                                         Map<List<Boolean>, Integer> cache) {
        List<Boolean> currentState = statePath.getLast();
        if (state.equals(currentState)) {
            return statePath.size();
        }
        int minPathLength = Integer.MAX_VALUE;
        for (Button button : buttons) {
            List<Boolean> nextState = new ArrayList<>(currentState);
            for (Integer indicator : button.indicators) {
                nextState.set(indicator, !nextState.get(indicator));
            }
            var nextStatePath = new LinkedHashSet<>(statePath);
            int path;
            if (nextStatePath.add(nextState)) {
                if (cache.containsKey(nextState)) {
                    path = cache.get(nextState) + statePath.size();
                } else {
                    path = doCalcShortestWay(state, buttons, nextStatePath, cache);
                    cache.put(nextState, path - statePath.size());
                }
                if (path < minPathLength) {
                    minPathLength = path;
                }
            }
        }
        return minPathLength;
    }


    record Button(List<Integer> indicators) {
    }
}
