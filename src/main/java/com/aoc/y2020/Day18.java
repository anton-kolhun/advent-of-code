package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;

import java.math.BigInteger;
import java.util.List;

public class Day18 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day18.txt");
        BigInteger sum = BigInteger.ZERO;
        for (String line : lines) {
            Cursor cursor = new Cursor(BigInteger.ZERO, 0);
            calculateValue(line, cursor);
            sum = sum.add(cursor.currentVal);
        }
        System.out.println("task1: " + sum);
    }

    private static void calculateValue(String line, Cursor cursor) {
        char currentSign = '+';
        while (cursor.position < line.length()) {
            int currentNumber;
            char c = line.charAt(cursor.position);
            if (c == '(') {
                Cursor nested = new Cursor(BigInteger.ZERO, cursor.position + 1);
                calculateValue(line, nested);
                cursor.position = nested.position;
                if (currentSign == '+') {
                    cursor.currentVal = cursor.currentVal.add(nested.currentVal);
                } else {
                    cursor.currentVal = cursor.currentVal.multiply(nested.currentVal);
                }
                continue;
            }
            if (c == ')') {
                cursor.position++;
                return;
            }
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while ((cursor.position < line.length()) && Character.isDigit(line.charAt(cursor.position))) {
                    sb.append(line.charAt(cursor.position));
                    cursor.position++;
                }
                currentNumber = Integer.parseInt(sb.toString());
                if (currentSign == '+') {
                    cursor.currentVal = cursor.currentVal.add(BigInteger.valueOf(currentNumber));
                } else {
                    cursor.currentVal = cursor.currentVal.multiply(BigInteger.valueOf(currentNumber));
                }
                continue;
            }
            if (c == '+') {
                currentSign = '+';
            }
            if (c == '*') {
                currentSign = '*';
            }
            cursor.position++;
        }
    }

    static class Cursor {
        BigInteger currentVal;
        int position;

        public Cursor(BigInteger currentVal, int position) {
            this.currentVal = currentVal;
            this.position = position;
        }
    }


    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day18.txt");
        BigInteger sum = BigInteger.ZERO;
        for (String line : lines) {
            line = rewrite(line);
            Cursor cursor = new Cursor(BigInteger.ZERO, 0);
            calculateValue(line, cursor);
            sum = sum.add(cursor.currentVal);
        }
        System.out.println("task2: " + sum);
    }

    private static String rewrite(String line) {
        line = line.replace(" ", "");
        StringBuilder modified = new StringBuilder(line);
        int cursor = 0;
        while (cursor < modified.length()) {
            for (; cursor < modified.length(); cursor++) {
                if (modified.charAt(cursor) == '+') {
                    wrapWithBrackets(cursor, modified);
                    cursor++;
                }
            }
        }
        return modified.toString();
    }

    private static void wrapWithBrackets(int cursor, StringBuilder modified) {
        insertClosingBracket(cursor, modified);
        insertOpeningBracket(cursor, modified);
    }

    private static void insertClosingBracket(int cursor, StringBuilder modified) {
        cursor = cursor + 1;
        char value = modified.charAt(cursor);
        if (Character.isDigit(value)) {
            while (cursor < modified.length() && Character.isDigit(modified.charAt(cursor))) {
                cursor++;
            }
            modified.insert(cursor, ")");
            return;
        }
        if (value == '(') {
            doInsertClosingBracket(cursor + 1, modified);
        }
    }


    private static void doInsertClosingBracket(int cursor, StringBuilder modified) {
        int innerBrackets = 0;
        for (; cursor < modified.length(); cursor++) {
            char c = modified.charAt(cursor);
            if (c == '(') {
                innerBrackets++;
            }
            if (c == ')') {
                if (innerBrackets == 0) {
                    modified.insert(cursor + 1, ")");
                    return;
                }
                innerBrackets--;
            }
        }
    }

    private static void insertOpeningBracket(int cursor, StringBuilder modified) {
        cursor = cursor - 1;
        char value = modified.charAt(cursor);
        if (Character.isDigit(value)) {
            while (cursor >= 0 && Character.isDigit(modified.charAt(cursor))) {
                cursor--;
            }
            modified.insert(cursor + 1, "(");
            return;
        }
        if (value == ')') {
            doInsertOpeningBracket(cursor - 1, modified);
        }
    }

    private static void doInsertOpeningBracket(int cursor, StringBuilder modified) {
        int innerBrackets = 0;
        for (; cursor >= 0; cursor--) {
            char c = modified.charAt(cursor);
            if (c == ')') {
                innerBrackets++;
            }
            if (c == '(') {
                if (innerBrackets == 0) {
                    modified.insert(cursor + 1, "(");
                    return;
                }
                innerBrackets--;
            }
        }
    }
}
