package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day14.txt");
        String code = lines.get(0);
        Map<String, String> rules = new HashMap<>();
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            List<String> parts = ParseUtils.splitByDelimiter(line, "->");
            rules.put(parts.get(0).trim(), parts.get(1).trim());
        }

        for (int steps = 0; steps < 10; steps++) {
            StringBuilder sb = new StringBuilder();
            sb.append(code.charAt(0));
            for (int i = 1; i < code.length(); i++) {
                String ruleToMatch = code.substring(i - 1, i + 1);
                String symbol = rules.get(ruleToMatch);
                if (symbol != null) {
                    sb.append(symbol);
                }
                sb.append(code.charAt(i));
            }
            code = sb.toString();
        }
        System.out.println("task1: " + calcDiff(code));
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day14.txt");
        String code = lines.get(0);
        Map<String, BigInteger> pairOccur = findPairOccur(code);
        Map<String, String> rules = new HashMap<>();
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            List<String> parts = ParseUtils.splitByDelimiter(line, "->");
            rules.put(parts.get(0).trim(), parts.get(1).trim());
        }

        for (int steps = 0; steps < 40; steps++) {
            Map<String, BigInteger> next = new HashMap<>();
            for (Map.Entry<String, BigInteger> entry : pairOccur.entrySet()) {
                if (rules.containsKey(entry.getKey())) {
                    String added = rules.get(entry.getKey());
                    next.merge(entry.getKey().charAt(0) + added, entry.getValue(), BigInteger::add);
                    next.merge(added + entry.getKey().charAt(1), entry.getValue(), BigInteger::add);
                } else {
                    next.put(entry.getKey(), entry.getValue());
                }
            }
            pairOccur = next;
        }
        BigInteger value = calcDiffTask2(pairOccur, code.charAt(code.length() - 1));
        System.out.println("task2: " + value);
    }

    private static BigInteger calcDiffTask2(Map<String, BigInteger> pairOccur, Character lastChar) {
        Map<Character, BigInteger> charOccur = new HashMap<>();
        for (Map.Entry<String, BigInteger> entry : pairOccur.entrySet()) {
            charOccur.merge(entry.getKey().charAt(0), entry.getValue(), BigInteger::add);
        }
        charOccur.merge(lastChar, BigInteger.ONE, BigInteger::add);
        BigInteger minOccur = null;
        BigInteger maxOccur = null;
        for (Map.Entry<Character, BigInteger> entry : charOccur.entrySet()) {
            BigInteger value = entry.getValue();
            if (maxOccur == null || value.compareTo(maxOccur) > 0) {
                maxOccur = value;
            }
            if (minOccur == null || value.compareTo(minOccur) < 0) {
                minOccur = value;
            }
        }
        return maxOccur.subtract(minOccur);
    }

    private static Map<String, BigInteger> findPairOccur(String code) {
        Map<String, BigInteger> occur = new HashMap<>();
        for (int i = 0; i < code.length() - 1; i++) {
            String pair = code.substring(i, i + 2);
            occur.merge(pair, BigInteger.valueOf(1), BigInteger::add);
        }
        return occur;
    }

    private static int calcDiff(String code) {
        Map<Character, Integer> charOccur = new HashMap<>();
        for (char c : code.toCharArray()) {
            charOccur.merge(c, 1, Integer::sum);
        }
        int minOccur = Integer.MAX_VALUE;
        int maxOccur = Integer.MIN_VALUE;
        for (Map.Entry<Character, Integer> entry : charOccur.entrySet()) {
            int value = entry.getValue();
            if (value > maxOccur) {
                maxOccur = value;
            }
            if (value < minOccur) {
                minOccur = value;
            }
        }
        return maxOccur - minOccur;
    }

}
