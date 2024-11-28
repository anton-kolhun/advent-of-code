package com.example.aoc_2020;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day21 {

    public static void main(String[] args) throws Exception {
        task1And2();
    }

    public static void task1And2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day21.txt");
        Map<String, Set<String>> ingrToallerg = new HashMap<>();
        Map<String, Set<String>> allergToIng = new HashMap<>();

        Map<Set<String>, Set<String>> conditions = new HashMap<>();

        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, "\\(contains");
            List<String> ingrs = ParseUtils.splitByDelimiter(parts.get(0), " ").stream().map(String::trim).toList();
            String part2 = parts.get(1).substring(0, parts.get(1).length() - 1);
            List<String> allergs = ParseUtils.splitByDelimiter(part2, ",").stream().map(String::trim).toList();
            conditions.put(new HashSet<>(ingrs), new HashSet<>(allergs));
            for (String ingredient : ingrs) {
                Set<String> in = new HashSet<>();
                in.add(ingredient);
                for (String allerg : allergs) {
                    Set<String> al = new HashSet<>();
                    al.add(allerg);
                    ingrToallerg.merge(ingredient, al, (strings, strings2) -> {
                        strings.addAll(strings2);
                        return strings;
                    });
                    allergToIng.merge(allerg, in, (strings, strings2) -> {
                        strings.addAll(strings2);
                        return strings;
                    });
                }
            }
        }

        Set<Set<Pair>> allCombinations = findCombinations(conditions, new HashSet<>(), allergToIng.size(), new HashMap<>());

        Set<String> usedIngredients = new HashSet<>();
        for (Set<Pair> result : allCombinations) {
            for (Pair pair : result) {
                usedIngredients.add(pair.ingredient);
            }
        }
        Set<String> leftIngredients = ingrToallerg.keySet();
        leftIngredients.removeAll(usedIngredients);

        int usage = 0;
        for (Set<String> strings : conditions.keySet()) {
            for (String s : leftIngredients) {
                if (strings.contains(s)) {
                    usage++;
                }
            }
        }
        System.out.println(usage);

        var pairs = allCombinations.iterator().next();
        var task2Result = pairs.stream()
                .sorted(Comparator.comparing(o -> o.allerg))
                .map(pair -> pair.ingredient)
                .reduce((s, s2) -> s + "," + s2)
                .get();
        System.out.println(task2Result);

    }

    private static Set<Set<Pair>> findCombinations(Map<Set<String>, Set<String>> conditions,
                                                   Set<Pair> current, int allergNumber, Map<Set<Pair>, Set<Set<Pair>>> cache) {

        if (current.size() == allergNumber) {
            for (Set<String> allergs : conditions.values()) {
                if (!allergs.isEmpty()) {
                    return Collections.emptySet();
                }
            }
            return Set.of(current);
        }

        Set<Set<Pair>> total = new HashSet<>();
        for (Map.Entry<Set<String>, Set<String>> entry : conditions.entrySet()) {
            Set<String> ingrs = entry.getKey();
            Set<String> allergs = entry.getValue();
            for (String ingr : ingrs) {
                for (String allerg : allergs) {
                    Set<Pair> next = new HashSet<>(current);
                    next.add(new Pair(ingr, allerg));
                    Set<Set<Pair>> res;
                    if (cache.containsKey(next)) {
                        res = cache.get(next);
                    } else {
                        Map<Set<String>, Set<String>> nextCond = getNextCondition(ingr, allerg, conditions);
                        if (nextCond == null) {
                            res = new HashSet<>();
                        } else {
                            res = findCombinations(nextCond, next, allergNumber, cache);
                        }
                        cache.put(next, res);
                    }
                    total.addAll(res);
                }
            }
        }
        return total;
    }

    private static Map<Set<String>, Set<String>> getNextCondition(String ingr, String allerg,
                                                                  Map<Set<String>, Set<String>> conditions) {
        Map<Set<String>, Set<String>> nextCond = new HashMap<>();
        for (Map.Entry<Set<String>, Set<String>> entry : conditions.entrySet()) {
            Set<String> nextIngr = new HashSet<>(entry.getKey());
            Set<String> nextAl = new HashSet<>(entry.getValue());
            if (!nextIngr.contains(ingr) && nextAl.contains(allerg)) {
                return null;
            }
            if (nextIngr.contains(ingr) && nextAl.contains(allerg)) {
                nextIngr.remove(ingr);
                nextAl.remove(allerg);
            } else {
                nextIngr.remove(ingr);
            }
            nextCond.put(nextIngr, nextAl);
        }
        return nextCond;
    }

    @Data
    @AllArgsConstructor
    private static class Pair {
        String ingredient;
        String allerg;
    }

}


