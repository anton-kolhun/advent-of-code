package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day6.txt");
        int sum  = 0;
        Set<Character> currentGroup = new HashSet<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                sum = sum + currentGroup.size();
                currentGroup.clear();
                continue;
            }
            for (char c : line.toCharArray()) {
                currentGroup.add(c);
            }
        }
        sum = sum + currentGroup.size();
        System.out.println("task1: " + sum);
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day6.txt");
        int sum  = 0;
        Set<Character> currentGroup = new HashSet<>();
        for (String line : lines) {
            if (line.isEmpty()) {
                currentGroup.remove('?');
                sum = sum + currentGroup.size();
                currentGroup.clear();
                continue;
            }
            if (currentGroup.isEmpty()) {
                for (char c : line.toCharArray()) {
                    currentGroup.add(c);
                }
            } else {
                Set<Character> lineChars = new HashSet<>();
                for (char c : line.toCharArray()) {
                    lineChars.add(c);
                }
                lineChars.retainAll(currentGroup);
                currentGroup = lineChars;
                currentGroup.add('?');
            }
        }
        currentGroup.remove('?');
        sum = sum + currentGroup.size();
        System.out.println("task2: " + sum);
    }

}