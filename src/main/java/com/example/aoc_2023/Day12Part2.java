package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Day12Part2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task2();
    }

    public static void task2() throws ExecutionException, InterruptedException {
        List<String> lines = FilesUtils.readFile("day12.txt");
        BigInteger total = BigInteger.ZERO;
        for (String line : lines) {
            BigInteger sum = processLine(line);
            total = total.add(sum);
        }
        System.out.println(total);
    }

    private static BigInteger processLine(String line) {
        List<String> data = ParseUtils.splitByDelimiter(line, " ");
        List<String> groupsStr = ParseUtils.splitByDelimiter(data.get(1), ",");
        List<Integer> groups = groupsStr.stream().map(Integer::parseInt).toList();
        List<Integer> newGroups = new ArrayList<>(groups);
        for (int i = 0; i < 4; i++) {
            newGroups.addAll(groups);
        }
        String concat = data.get(0);
        for (int i = 0; i < 4; i++) {
            concat = concat + "?" + data.get(0);
        }
        Map<CacheKey, BigInteger> cache = new HashMap<>();
        BigInteger res = doCheck(concat, 0, newGroups, 0, new ArrayList<>(), cache);
        // System.out.println(res.size());
        return res;
    }

    private static BigInteger doCheck(String line, int cursorLine, List<Integer> groups,
                                      int cursorGroup, List<Character> current, Map<CacheKey, BigInteger> cache) {

        if (cursorLine > line.length()) {
            return BigInteger.ZERO;
        } else if (cursorLine == line.length()) {
            if (cursorGroup == groups.size()) {
                return BigInteger.ONE;
            } else {
                return BigInteger.ZERO;
            }
        }
//        if (cursorGroup >= groups.size()) {
//            return Collections.emptyList();
//        }

        char cur = line.charAt(cursorLine);
        BigInteger total = BigInteger.ZERO;
        if (cur == '?') {
            List<Character> next = new ArrayList<>(current);
            next.add('.');
            var key = new CacheKey(cursorLine + 1, cursorGroup);
            BigInteger res1;
            if (cache.containsKey(key)) {
                res1 = cache.get(key);
            } else {
                res1 = doCheck(line, cursorLine + 1, groups, cursorGroup, next, cache);
                cache.put(key, res1);
            }
            total = total.add(res1);
            BigInteger res = procDash(line, cursorLine, groups, cursorGroup, current, cache);
            total = total.add(res);
        } else if (cur == '.') {
            List<Character> next = new ArrayList<>(current);
            next.add('.');

            BigInteger res;
            var key = new CacheKey(cursorLine + 1, cursorGroup);
            if (cache.containsKey(key)) {
                res = cache.get(key);
            } else {
                res = doCheck(line, cursorLine + 1, groups, cursorGroup, next, cache);
                cache.put(key, res);
            }
            total = total.add(res);
        } else if (cur == '#') {
            BigInteger res = procDash(line, cursorLine, groups, cursorGroup, current, cache);
            total = total.add(res);
        }
        return total;
    }

    private static BigInteger procDash(String line, int cursorLine, List<Integer> groups,
                                       int cursorGroup, List<Character> current, Map<CacheKey, BigInteger> cache) {
        if (cursorGroup >= groups.size()) {
            return BigInteger.ZERO;
        }
        BigInteger total = BigInteger.ZERO;
        char cur;
        int groupSize = groups.get(cursorGroup);
        var next = new ArrayList<>(current);
        if (current.size() + groupSize > line.length()) {
            return total;
        }
        for (int i = 0; i < groupSize; i++) {
            cur = line.charAt(cursorLine + i);
            if (!(cur == '?' || cur == '#')) {
                return total;
            }
            next.add('#');
        }

        BigInteger res2;
        if (cursorGroup < groups.size() - 1) {
            if (current.size() + groupSize + 1 > line.length()) {
                return total;
            }
            cur = line.charAt(cursorLine + groupSize);
            if (!(cur == '?' || cur == '.')) {
                return total;
            }
            next.add('.');

            var key = new CacheKey(cursorLine + groupSize + 1, cursorGroup + 1);
            if (cache.containsKey(key)) {
                res2 = cache.get(key);
            } else {
                res2 = doCheck(line, cursorLine + groupSize + 1, groups, cursorGroup + 1, next, cache);
                cache.put(key, res2);
            }
        } else {
            var key = new CacheKey(cursorLine + groupSize, cursorGroup + 1);
            if (cache.containsKey(key)) {
                res2 = cache.get(key);
            } else {
                res2 = doCheck(line, cursorLine + groupSize, groups, cursorGroup + 1, next, cache);
                cache.put(key, res2);
            }
        }
        total = total.add(res2);
        return total;
    }


    private static class CacheKey {
        private int lineCursor;
        private int cursorGroup;

        public CacheKey(int lineCursor, int cursorGroup) {
            this.lineCursor = lineCursor;
            this.cursorGroup = cursorGroup;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (lineCursor != cacheKey.lineCursor) return false;
            return cursorGroup == cacheKey.cursorGroup;
        }

        @Override
        public int hashCode() {
            int result = lineCursor;
            result = 31 * result + cursorGroup;
            return result;
        }
    }
}

