package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Day13 {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1();
        task2InputForPython();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day13.txt");
        int cursor = 0;
        List<Prize> prizes = new ArrayList<>();
        while (cursor < lines.size()) {
            ButtonCfg but1 = parseButton(lines.get(cursor), 3);
            ButtonCfg but2 = parseButton(lines.get(cursor + 1), 1);

            List<String> parts = ParseUtils.splitByDelimiter(lines.get(cursor + 2), ":")
                    .stream().map(String::trim).toList();
            List<String> deltaParts = ParseUtils.splitByDelimiter(parts.get(1), ",")
                    .stream().map(String::trim).toList();
            String xDelta = ParseUtils.splitByDelimiter(deltaParts.get(0), "=")
                    .stream().map(String::trim).toList().get(1);
            String yDelta = ParseUtils.splitByDelimiter(deltaParts.get(1), "=")
                    .stream().map(String::trim).toList().get(1);
            prizes.add(new Prize(but1, but2, Integer.parseInt(xDelta), Integer.parseInt(yDelta)));
            cursor = cursor + 4;
        }

        long total = 0;
        for (Prize prize : prizes) {
            long res = calculateComb(prize);
            total += res;
        }

        System.out.println(total);
    }


    public static void task2InputForPython() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day13.txt");
        int cursor = 0;
        List<Prize> prizes = new ArrayList<>();
        while (cursor < lines.size()) {
            ButtonCfg but1 = parseButton(lines.get(cursor), 3);
            ButtonCfg but2 = parseButton(lines.get(cursor + 1), 1);

            List<String> parts = ParseUtils.splitByDelimiter(lines.get(cursor + 2), ":")
                    .stream().map(String::trim).toList();
            List<String> deltaParts = ParseUtils.splitByDelimiter(parts.get(1), ",")
                    .stream().map(String::trim).toList();
            String xDelta = ParseUtils.splitByDelimiter(deltaParts.get(0), "=")
                    .stream().map(String::trim).toList().get(1);
            String yDelta = ParseUtils.splitByDelimiter(deltaParts.get(1), "=")
                    .stream().map(String::trim).toList().get(1);
            prizes.add(new Prize(but1, but2, Integer.parseInt(xDelta), Integer.parseInt(yDelta)));
            cursor = cursor + 4;
        }

        for (Prize prize : prizes) {
            var x = new BigInteger("10000000000000").add(BigInteger.valueOf(prize.x));
            var y = new BigInteger("10000000000000").add(BigInteger.valueOf(prize.y));
            System.out.println(prize.aBut.x + " " + prize.bBut.x + " " + x);
            System.out.println(prize.aBut.y + " " + prize.bBut.y + " " + y);
            System.out.println();
        }
    }

    private static long calculateComb(Prize prize) {
        Result res = calculateCombRec(prize, prize.x, prize.y, 0, 0, 0, new HashMap());
        if (res == Result.max) {
            return 0;
        }
        return res.cost();
    }

    private static Result calculateCombRec(Prize prize, int remX, int remY, int cost, int stepsA, int stepsB, Map<CacheKey, Result> cache) {
        if (stepsA > 100 || stepsB > 100) {
            return Result.max;
        }
        if (remX == 0 && remY == 0) {
            return new Result(stepsA, stepsB);
        }
        if (remX < 0 || remY < 0) {
            return Result.max;
        }
        Result res1 = getFromCacheForA(prize, remX, remY, cost, stepsA, stepsB, cache);
        Result res2 = getFromCacheForB(prize, remX, remY, cost, stepsA, stepsB, cache);
        if (res1.cost() < res2.cost()) {
            return res1;
        } else {
            return res2;
        }
    }

    private static Result getFromCacheForA(Prize prize, int remX, int remY, int cost,
                                           int stepsA, int stepsB, Map<CacheKey, Result> cache) {
        Result res;
        CacheKey cacheKey = new CacheKey(remX - prize.aBut.x, remY - prize.aBut.y);
        if (cache.containsKey(cacheKey)) {
            Result cached = cache.get(cacheKey);
            long stepsATotal = stepsA + cached.stepsA;
            long stepsBTotal = stepsB + cached.stepsB;
            if (stepsATotal <= 100 && stepsBTotal <= 100) {
                res = new Result(stepsA + 1 + cached.stepsA, stepsB + cached.stepsB);
            } else {
                res = new Result(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        } else {
            res = calculateCombRec(prize, remX - prize.aBut.x, remY - prize.aBut.y, cost + prize.aBut.cost, stepsA + 1, stepsB, cache);
            if (res == Result.max) {
                cache.put(cacheKey, Result.max);
            } else {
                cache.put(cacheKey, new Result(res.stepsA - stepsA - 1, res.stepsB - stepsB));
            }
        }
        return res;
    }

    private static Result getFromCacheForB(Prize prize, int remX, int remY, int cost,
                                           int stepsA, int stepsB, Map<CacheKey, Result> cache) {
        Result res;
        CacheKey cacheKey = new CacheKey(remX - prize.bBut.x, remY - prize.bBut.y);
        if (cache.containsKey(cacheKey)) {
            Result value = cache.get(cacheKey);
            long stepsATotal = stepsA + value.stepsA;
            long stepsBTotal = stepsB + value.stepsB;
            if (stepsATotal <= 100 && stepsBTotal <= 100) {
                res = new Result(stepsA + value.stepsA, stepsB + 1 + value.stepsB);
            } else {
                res = new Result(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        } else {
            res = calculateCombRec(prize, remX - prize.bBut.x, remY - prize.bBut.y, cost + prize.bBut.cost, stepsA, stepsB + 1, cache);
            if (res == Result.max) {
                cache.put(cacheKey, Result.max);
            } else {
                cache.put(cacheKey, new Result(res.stepsA - stepsA, res.stepsB - stepsB - 1));
            }
        }
        return res;
    }


    private static ButtonCfg parseButton(String line, int cost) {
        List<String> parts = ParseUtils.splitByDelimiter(line, ":")
                .stream().map(String::trim).toList();
        List<String> deltaParts = ParseUtils.splitByDelimiter(parts.get(1), ",")
                .stream().map(String::trim).toList();
        String xDelta = ParseUtils.splitByDelimiter(deltaParts.get(0), "\\+")
                .stream().map(String::trim).toList().get(1);
        String yDelta = ParseUtils.splitByDelimiter(deltaParts.get(1), "\\+")
                .stream().map(String::trim).toList().get(1);
        return new ButtonCfg(Integer.parseInt(xDelta), Integer.parseInt(yDelta), cost);
    }


    @Data
    @AllArgsConstructor
    static class ButtonCfg {
        private int x;
        private int y;
        private int cost;
    }

    @Data
    @AllArgsConstructor
    static class Prize {
        private ButtonCfg aBut;
        private ButtonCfg bBut;
        int x;
        int y;

    }

    @Data
    @AllArgsConstructor
    static class CacheKey {
        int remX;
        int remY;
    }


    @Data
    @AllArgsConstructor
    static class Result {

        static Result max = new Result(Integer.MAX_VALUE, Integer.MAX_VALUE);

        long stepsA;
        long stepsB;

        private long cost() {
            return stepsA * 3 + stepsB;
        }
    }


}
