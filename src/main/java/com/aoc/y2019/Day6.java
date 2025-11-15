package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 {

    public static void main(String[] args) {
        task();
    }

    public static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day6.txt");
        Map<String, List<String>> nodeToOrbits = new HashMap<>();
        Map<String, String> orbitToNodes = new HashMap<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, "\\)");
            nodeToOrbits.merge(parts.get(0), List.of(parts.get(1)), (oldVals, newVals) -> {
                List<String> merged = new ArrayList<>(oldVals);
                merged.addAll(newVals);
                return merged;
            });
            orbitToNodes.put(parts.get(1), parts.get(0));
        }

        int totalOrbits = 0;
        for (Map.Entry<String, String> entry : orbitToNodes.entrySet()) {
            int val = getPath(entry.getKey(), orbitToNodes).size() - 1;
            totalOrbits += val;
        }
        System.out.println("task1: " + totalOrbits);

        List<String> youPath = getPath("YOU", orbitToNodes);
        List<String> sanPath = getPath("SAN", orbitToNodes);
        int res = findClosestOrbit(youPath, sanPath);
        System.out.println("task2: " + res);

    }

    private static int findClosestOrbit(List<String> youPath, List<String> sanPath) {
        int youDist = -1;
        String common = "";
        for (int i = 0; i < youPath.size(); i++) {
            String youItem = youPath.get(i);
            if (sanPath.contains(youItem)) {
                youDist = i;
                common = youItem;
                break;
            }
        }
        int sanDist = -1;
        for (int i = 0; i < sanPath.size(); i++) {
            if (sanPath.get(i).equals(common)) {
                sanDist = i;
                break;
            }
        }
        return youDist + sanDist - 2;
    }

    private static List<String> getPath(String orbit, Map<String, String> orbitToNodes) {
        List<String> path = new ArrayList<>();
        path.add(orbit);
        if (!orbitToNodes.containsKey(orbit)) {
            return path;
        }

        List<String> indirect = getPath(orbitToNodes.get(orbit), orbitToNodes);
        path.addAll(indirect);
        return path;
    }
}
