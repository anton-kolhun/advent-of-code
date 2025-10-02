package com.aoc.y2020;

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
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day19.txt");
        Map<String, Tree> branches = new HashMap<>();
        int lineNumber = 0;
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isEmpty()) {
                break;
            }
            List<String> elements = ParseUtils.splitByDelimiter(line, ":");
            String el = elements.get(0);
            String value = elements.get(1).trim();
            Tree branch;
            if (branches.containsKey(el)) {
                branch = branches.get(el);
            } else {
                branch = new Tree();
                branch.label = el;
                branches.put(el, branch);
            }
            if (value.contains("\"")) {
                value = value.replace("\"", "");
                branch.value = value;
            } else if (value.contains("|")) {
                List<String> parts = ParseUtils.splitByDelimiter(value, "\\|");
                fillKids(parts, branches, branch);
            } else {
                fillKids(List.of(value), branches, branch);
            }
        }

        List<String> linesToMatch = new ArrayList<>();
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            linesToMatch.add(line);
        }

        List<String> vals = expandTree(branches, branches.get("0"));
        linesToMatch.retainAll(vals);
        System.out.println("task1: " + linesToMatch.size());
    }

    private static List<String> expandTree(Map<String, Tree> branches, Tree cursor) {
        if (cursor.value != null) {
            return List.of(cursor.value);
        }
        List<String> results = new ArrayList<>();
        for (List<Tree> kidSet : cursor.children) {
            List<String> currentRes = new ArrayList<>();
            currentRes.add("");
            for (Tree branch : kidSet) {
                List<String> result = expandTree(branches, branch);
                currentRes = merge(currentRes, result);
            }
            results.addAll(currentRes);
        }
        return results;
    }

    private static List<String> merge(List<String> currentRes, List<String> result) {
        List<String> merged = new ArrayList<>();
        for (String currentRe : currentRes) {
            for (String s : result) {
                String mergedStr = currentRe + s;
                merged.add(mergedStr);
            }
        }
        return merged;
    }

    private static void fillKids(List<String> kidSets, Map<String, Tree> branches, Tree currentBranch) {
        for (String part : kidSets) {
            List<Tree> kids = new ArrayList<>();
            part = part.trim();
            List<String> els = ParseUtils.splitByDelimiter(part, " ");
            for (String s : els) {
                Tree branch;
                if (branches.containsKey(s)) {
                    branch = branches.get(s);
                } else {
                    branch = new Tree();
                    branch.label = s;
                    branches.put(s, branch);
                }
                kids.add(branch);
            }
            currentBranch.children.add(kids);
        }
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day19_p2.txt");
        Set<String> nodes = new HashSet<>();
        Map<String, Tree> branches = new HashMap<>();
        int lineNumber = 0;
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isEmpty()) {
                break;
            }
            List<String> elements = ParseUtils.splitByDelimiter(line, ":");
            String el = elements.get(0);
            nodes.add(el);
            String value = elements.get(1).trim();
            Tree branch;
            if (branches.containsKey(el)) {
                branch = branches.get(el);
            } else {
                branch = new Tree();
                branch.label = el;
                branches.put(el, branch);
            }
            if (value.contains("\"")) {
                value = value.replace("\"", "");
                branch.value = value;
            } else if (value.contains("|")) {
                List<String> parts = ParseUtils.splitByDelimiter(value, "\\|");
                fillKids(parts, branches, branch);
            } else {
                fillKids(List.of(value), branches, branch);
            }
        }
        lineNumber++;
        List<String> linesToMatch = new ArrayList<>();
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            linesToMatch.add(line);
        }
        List<String> vals42 = expandTree(branches, branches.get("42"));
        List<String> vals31 = expandTree(branches, branches.get("31"));
        //treat 42 as "A", 31 as "B"
        //0 = 8 11 = [42]+ [42 31]+. E.g. 42 42 31 or 42 42 42 31 31 ...
        List<String> transformedToMatch = transform(linesToMatch, new HashSet<>(vals42), new HashSet<>(vals31));

        int counter = 0;
        for (int i = 0; i < transformedToMatch.size(); i++) {
            String value = transformedToMatch.get(i);
            char mode = 'A';
            int aNum = 0;
            int bNum = 0;
            boolean match = true;
            for (char c : value.toCharArray()) {
                if (c == 'A') {
                    if (mode == 'B') {
                        match = false;
                        break;
                    }
                    aNum++;
                }
                if (c == 'B') {
                    mode = 'B';
                    bNum++;
                }
            }
            if (match && bNum > 0 && (aNum > bNum)) {
                counter++;
            }
        }
        System.out.println("task2: " + counter);
    }

    private static List<String> transform(List<String> linesToMatch, Set<String> vals42, Set<String> vals31) {
        int length = vals42.iterator().next().length();
        List<String> matches = new ArrayList<>();
        for (String toMatch : linesToMatch) {
            List<String> parts = new ArrayList<>();
            int cursor = 0;
            while (cursor < toMatch.length()) {
                parts.add(toMatch.substring(cursor, cursor + length));
                cursor = cursor + length;
            }
            StringBuilder newView = new StringBuilder();
            boolean match = true;
            for (String part : parts) {
                if (vals42.contains(part)) {
                    newView.append("A");
                } else if (vals31.contains(part)) {
                    newView.append("B");
                } else {
                    match = false;
                    break;
                }
            }
            if (match) {
                matches.add(newView.toString());
            }
        }
        return matches;
    }

    private static class Tree {
        String label;
        String value;
        List<List<Tree>> children = new ArrayList<>();

        public Tree() {
        }
    }


}
