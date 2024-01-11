package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

public class Day1 {

    public static void main(String[] args) {
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("day1.txt");
        int total = 0;
        for (String line : lines) {
            Character firstDigit = null;
            char lastDigit = 'n';
            for (char c : line.toCharArray()) {
                if (Character.isDigit(c)) {
                    if (firstDigit == null) {
                        firstDigit = c;
                    }
                    lastDigit = c;
                }
            }
            String numb = new String(new char[]{firstDigit, lastDigit});
            total += Integer.parseInt(numb);
        }
        System.out.println(total);
    }

    public static void task2() {
        var digits = Map.of("one", '1', "two", '2', "three", '3', "four", '4', "five", '5',
                "six", '6', "seven", '7', "eight", '8', "nine", '9');
        List<String> lines = FilesUtils.readFile("day1.txt");
        int total = 0;
        for (String line : lines) {
            CharInfo firstDigit = null;
            CharInfo lastDigit = null;
            char[] charArray = line.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (Character.isDigit(c)) {
                    if (firstDigit == null) {
                        firstDigit = new CharInfo(c, i);
                    }
                    lastDigit = new CharInfo(c, i);
                }
            }
            for (Map.Entry<String, Character> entry : digits.entrySet()) {
                int firstInd = line.indexOf(entry.getKey());
                int lastInd = line.lastIndexOf(entry.getKey());
                if (firstInd >= 0 && firstInd < firstDigit.index) {
                    firstDigit.index = firstInd;
                    firstDigit.c = entry.getValue();
                }
                if (lastInd >= 0 && lastInd > lastDigit.index) {
                    lastDigit.index = lastInd;
                    lastDigit.c = entry.getValue();
                }
            }
            String numb = new String(new char[]{firstDigit.c, lastDigit.c});
            total += Integer.parseInt(numb);
        }
        System.out.println(total);

    }

    @AllArgsConstructor
    @Data
    private static class CharInfo {
        Character c;
        int index;

    }
}
