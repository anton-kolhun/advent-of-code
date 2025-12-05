package com.aoc.y2025;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day5 {

    public static void main(String[] args) {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day5.txt");
        List<Range> ranges = new ArrayList<>();
        int lineNumber = 0;
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isEmpty()) {
                lineNumber++;
                break;
            }
            List<BigInteger> pair = ParseUtils.splitByDelimiter(line, "-").stream()
                    .map(BigInteger::new)
                    .toList();
            ranges.add(new Range(pair.get(0), pair.get(1)));
        }
        List<BigInteger> values = new ArrayList<>();
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            values.add(new BigInteger(line));
        }

        int counter = 0;
        for (BigInteger value : values) {
            for (Range range : ranges) {
                if (value.compareTo(range.start) >= 0 &&
                        value.compareTo(range.end) <= 0) {
                    counter++;
                    break;
                }
            }
        }
        System.out.println("task1: " + counter);
    }


    private record Range(BigInteger start, BigInteger end) {

    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2025/day5.txt");
        List<Range> ranges = new ArrayList<>();
        int lineNumber = 0;
        for (; lineNumber < lines.size(); lineNumber++) {
            String line = lines.get(lineNumber);
            if (line.isEmpty()) {
                break;
            }
            List<BigInteger> pair = ParseUtils.splitByDelimiter(line, "-").stream()
                    .map(BigInteger::new)
                    .toList();
            ranges.add(new Range(pair.get(0), pair.get(1)));
        }


        List<Range> merged = new ArrayList<>();
        ranges.sort(Comparator.comparing(o -> o.start));

        Range cursor = ranges.get(0);
        for (int i = 1; i < ranges.size(); i++) {
            Range next = ranges.get(i);
            if (cursor.end.compareTo(next.start) < 0) {
                merged.add(cursor);
                cursor = next;
            } else if (cursor.end.compareTo(next.start) >= 0) {
                BigInteger newEnd;
                if (cursor.end.compareTo(next.end) >= 0) {
                    newEnd = cursor.end;
                } else {
                    newEnd = next.end;
                }
                cursor = new Range(cursor.start, newEnd);
            }
        }
        merged.add(cursor);

        BigInteger counter = BigInteger.ZERO;
        for (Range range : merged) {
            counter = counter.add(range.end.subtract(range.start).add(BigInteger.ONE));
        }
        System.out.println("task2: " + counter);
    }
}
