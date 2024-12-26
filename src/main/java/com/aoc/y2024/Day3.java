package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    public static void main(String[] args) {
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day3.txt");
        final String regex = "mul\\(\\d+,\\d+\\)";
        final List<String> matches = new ArrayList<>();
        for (String line : lines) {
            final Matcher m = Pattern.compile(regex).matcher(line);
            while (m.find()) {
                matches.add(m.group(0));
            }
        }
        System.out.println(matches);

        long sum = 0;
        for (String match : matches) {
            int startBracket = match.indexOf("(");
            int commaIndex = match.indexOf(",");
            int endBracket = match.indexOf(")");
            String part1 = match.substring(startBracket + 1, commaIndex);
            String part2 = match.substring(commaIndex + 1, endBracket);
            sum = sum + Integer.parseInt(part1) * Integer.parseInt(part2);

        }
        System.out.println(sum);
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day3.txt");


        final String doRegex = "do\\(\\)";
        final String undoRegex = "don't\\(\\)";

        var state = State.DO;
        long totalSum = 0;
        for (String line : lines) {
            Map<Integer, State> indexToState = new HashMap<>();
            List<Integer> doMatches = findMatches(line, doRegex);
            List<Integer> undoMatches = findMatches(line, undoRegex);
            int doIndex = 0;
            int undoIndex = 0;
            for (int i = 0; i < line.length(); i++) {
                if (doIndex < doMatches.size() && doMatches.get(doIndex) == i) {
                    doIndex++;
                    state = State.DO;
                } else if (undoIndex < undoMatches.size() && undoMatches.get(undoIndex) == i) {
                    undoIndex++;
                    state = State.UNDO;
                }
                indexToState.put(i, state);
            }

            List<String> matches = new ArrayList<>();
            String regex = "mul\\(\\d+,\\d+\\)";
            final Matcher m = Pattern.compile(regex).matcher(line);
            while (m.find()) {
                if (indexToState.get(m.start()) == State.DO) {
                    matches.add(m.group(0));
                }
            }
            long sum = 0;
            for (String match : matches) {
                int startBracket = match.indexOf("(");
                int commaIndex = match.indexOf(",");
                int endBracket = match.indexOf(")");
                String part1 = match.substring(startBracket + 1, commaIndex);
                String part2 = match.substring(commaIndex + 1, endBracket);
                sum = sum + Long.parseLong(part1) * Long.parseLong(part2);
            }
            totalSum = totalSum + sum;
        }

        System.out.println(totalSum);


    }

    private static List<Integer> findMatches(String line, String doRegex) {
        List<Integer> doIndexes = new ArrayList<>();
        final Matcher m = Pattern.compile(doRegex).matcher(line);
        while (m.find()) {
            doIndexes.add(m.start());
        }
        return doIndexes;
    }

    enum State {
        DO, UNDO;
    }
}
