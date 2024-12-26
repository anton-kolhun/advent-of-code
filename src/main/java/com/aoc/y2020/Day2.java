package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.List;

public class Day2 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day2.txt");
        int niceNumber = 0;
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, ":").stream().map(String::trim).toList();
            String password = parts.get(1);
            List<String> policyParts = ParseUtils.splitByDelimiter(parts.get(0), " ").stream().map(String::trim).toList();
            String element = policyParts.get(1);
            List<String> occurStr = ParseUtils.splitByDelimiter(policyParts.get(0), "-").stream().map(String::trim).toList();
            int minUsage = Integer.parseInt(occurStr.get(0));
            int maxUsage = Integer.parseInt(occurStr.get(1));
            int usage = 0;
            for (char c : password.toCharArray()) {
                if (String.valueOf(c).equals(element)) {
                    usage++;
                }
            }
            if (usage >= minUsage && usage <= maxUsage) {
                niceNumber++;
            }

        }
        System.out.println(niceNumber);

    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day2.txt");
        int niceNumber = 0;
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, ":").stream().map(String::trim).toList();
            String password = parts.get(1);
            List<String> policyParts = ParseUtils.splitByDelimiter(parts.get(0), " ").stream().map(String::trim).toList();
            String element = policyParts.get(1);
            List<String> occurStr = ParseUtils.splitByDelimiter(policyParts.get(0), "-").stream().map(String::trim).toList();
            int minUsage = Integer.parseInt(occurStr.get(0));
            int maxUsage = Integer.parseInt(occurStr.get(1));
            int usage = 0;
            char c1 = password.charAt(minUsage - 1);
            if (String.valueOf(c1).equals(element)) {
                usage++;
            }
            char c2 = password.charAt(maxUsage - 1);
            if (String.valueOf(c2).equals(element)) {
                usage++;
            }
            if (usage == 1) {
                niceNumber++;
            }

        }
        System.out.println(niceNumber);

    }
}
