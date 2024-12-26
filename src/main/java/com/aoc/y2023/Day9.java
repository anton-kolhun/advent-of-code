package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.List;

public class Day9 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day9.txt");
        long total = 0;
        for (String line : lines) {
            total += process(line);
        }
        System.out.println(total);
    }

    private static long process(String line) {
        List<List<Integer>> totalData = new ArrayList<>();
        List<Integer> nextLine = new ArrayList<>();
        boolean allZeroes = true;
        List<Integer> currentLine = ParseUtils.splitByDelimiter(line, " ").stream().map(Integer::parseInt).toList();
        totalData.add(currentLine);
        while (allZeroes) {
            allZeroes = false;
            for (int i = 0; i < currentLine.size() - 1; i++) {
                Integer current = currentLine.get(i);
                Integer next = currentLine.get(i + 1);
                Integer delta = next - current;
                if (delta != 0) allZeroes = true;
                nextLine.add(delta);
            }
            totalData.add(new ArrayList<>(nextLine));
            currentLine = new ArrayList<>(nextLine);
            nextLine = new ArrayList<>();
        }
        //shift data;
        long cursor = 0;
        for (int i = totalData.size() - 1; i > 0; i--) {
            List<Integer> above = totalData.get(i - 1);
            cursor = above.get(above.size() - 1) + cursor;
        }
        return cursor;

    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day9.txt");
        long total = 0;
        for (String line : lines) {
            total += processV2(line);
        }
        System.out.println(total);
    }

    private static long processV2(String line) {
        List<List<Integer>> totalData = new ArrayList<>();
        List<Integer> nextLine = new ArrayList<>();
        boolean allZeroes = true;
        List<Integer> currentLine = ParseUtils.splitByDelimiter(line, " ").stream().map(Integer::parseInt).toList();
        totalData.add(currentLine);
        while (allZeroes) {
            allZeroes = false;
            for (int i = 0; i < currentLine.size() - 1; i++) {
                Integer current = currentLine.get(i);
                Integer next = currentLine.get(i + 1);
                Integer delta = next - current;
                if (delta != 0) allZeroes = true;
                nextLine.add(delta);
            }
            totalData.add(new ArrayList<>(nextLine));
            currentLine = new ArrayList<>(nextLine);
            nextLine = new ArrayList<>();
        }
        //shift data;
        long cursor = 0;
        for (int i = totalData.size() - 1; i > 0; i--) {
            List<Integer> above = totalData.get(i - 1);
            cursor = above.get(0) - cursor;
        }
        return cursor;
    }
}
