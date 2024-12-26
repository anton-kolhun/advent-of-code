package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day19 {

    public static void main(String[] args) {
        task1And2();
    }

    public static void task1And2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day19.txt");

        Set<String> towels = new HashSet(
                ParseUtils.splitByDelimiter(lines.get(0), ",")
                        .stream().map(String::trim)
                        .toList());
        long counterTask1 = 0;
        long counterTask2 = 0;
        for (int line = 2; line < lines.size(); line++) {
            String design = lines.get(line);
            var res = checkDesign(design, towels, new ArrayList<>(), new HashMap<>());
            counterTask2 += res;
            if (res > 0) {
                counterTask1++;
            }
        }
        System.out.println(counterTask1);
        System.out.println(counterTask2);

    }


    private static long checkDesign(String design, Set<String> towels, List<String> used, Map<String, Long> cache) {
        String concat = concat(used);
        if (design.equals(concat)) {
            return 1;
        }
        if (!design.startsWith(concat)) {
            return 0;
        }
        long total = 0;
        for (String towel : towels) {
            List<String> nextUsed = new ArrayList<>(used);
            nextUsed.add(towel);
            long res;
            if (cache.containsKey(concat(nextUsed))) {
                res = cache.get(concat(nextUsed));
            } else {
                res = checkDesign(design, towels, nextUsed, cache);
                if (res == 0) {
                    cache.put(concat(nextUsed), 0l);
                } else {
                    cache.put(concat(nextUsed), res);
                }
            }
            total = total + res;
        }
        return total;
    }

    static String concat(List<String> towels) {
        String concat;
        if (towels.isEmpty()) {
            concat = "";
        } else {
            concat = towels.stream().reduce((s1, s2) -> s1 + s2).get();
        }
        return concat;
    }
}
