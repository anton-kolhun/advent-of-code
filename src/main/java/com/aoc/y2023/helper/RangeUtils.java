package com.aoc.y2023.helper;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RangeUtils {

    public static void main(String[] args) {
        Range r1 = new Range(1, 5);
        Range r2 = new Range(9, 15);

        RangeLine line = new RangeLine();
//        line.addRange(r2);
//        line.addRange(r1);
        line.addRange(new Range(1, 3));
        line.addRange(new Range(40, 50));
/*        line.splitRange(new Range(30, 41));
        line.splitRange(new Range(45, 55));*/
        line.addBeyondExisting(new Range(45, 55));
        line.addBeyondExisting(new Range(25, 45));
        line.addRange(new Range(40, 50));
        line.addBeyondExisting(new Range(-5, -1));
        line.addBeyondExisting(new Range(500, 505));
        System.out.println(line);
    }


    @AllArgsConstructor
    @ToString
    public static class Range {
        public long start;
        public long end;
    }

    public static class RangeLine {
        public List<Range> ranges = new ArrayList<>();

        public void addRange(Range other) {
            if (ranges.isEmpty()) {
                ranges.add(other);
                return;
            }
            List<Range> newRanges = new ArrayList<>();
            int index = 0;
            if (other.start > ranges.get(ranges.size() - 1).end) {
                newRanges.addAll(ranges);
                newRanges.add(other);
                ranges = newRanges;
                return;
            }

            Range cursor = other;
            for (; index < ranges.size(); index++) {
                Range range = ranges.get(index);
                if (range.start > cursor.end) {
                    break;
                }

//                if (cursor.start >= range.end) {
                if (cursor.start > range.end) {
                    newRanges.add(range);
                    continue;
                }

                if (cursor.start >= range.start && cursor.end <= range.end) {
                    cursor = null;
                    break;
                }

                if (cursor.start <= range.start && cursor.end <= range.end) {
                    cursor.end = range.end;
                    continue;
                }

                if (cursor.start >= range.start && cursor.end >= range.end) {
                    cursor.start = range.start;
                    continue;
                }
            }
            if (cursor != null) newRanges.add(cursor);
            for (int i = index; i < ranges.size(); i++) {
                newRanges.add(ranges.get(i));
            }
            ranges = newRanges;
        }


        public void addWithoutMerge(Range other) {
            ranges.add(0, other);
        }

        //return
        public List<Range> splitRange(Range other) {
            if (ranges.isEmpty()) {
                ranges.add(other);
                return List.of(other);
            }
            List<Range> newRanges = new ArrayList<>();
            int index = 0;
            if (other.start > ranges.get(ranges.size() - 1).end) {
                newRanges.addAll(ranges);
                newRanges.add(other);
                ranges = newRanges;
                return List.of(other);
            }
            List<Range> addedParts = new ArrayList<>();
            Range cursor = other;
            for (; index < ranges.size(); index++) {
                Range range = ranges.get(index);
                if (range.start > cursor.end) {
                    break;
                }

                if (cursor.start > range.end) {
                    newRanges.add(range);
                    continue;
                }

                if (cursor.start >= range.start && cursor.end <= range.end) {
                    Range r1 = new Range(range.start, cursor.start - 1);
                    Range r2 = new Range(cursor.start, cursor.end);
                    Range r3 = new Range(cursor.end + 1, range.end);
                    newRanges.add(r1);
                    newRanges.add(r2);
                    newRanges.add(r3);
                    cursor = null;
                    index = index + 1;
                    break;
                }

                if (cursor.start <= range.start && cursor.end <= range.end) {
                    newRanges.add(new Range(cursor.start, cursor.end));
                    newRanges.add(new Range(cursor.end + 1, range.end));
                    cursor = null;
                    index = index + 1;
                    break;
                }

                if (cursor.start >= range.start && cursor.end >= range.end) {
                    newRanges.add(new Range(range.start, cursor.start - 1));
//                    newRanges.add(new Range(cursor.start, cursor.end));
                    //  cursor = null;
                    //index = index + 1;
                }
            }
            if (cursor != null) newRanges.add(cursor);
            for (int i = index; i < ranges.size(); i++) {
                newRanges.add(ranges.get(i));
            }
            ranges = newRanges.stream().filter(range -> range.start <= range.end).collect(Collectors.toList());
            return List.of(cursor);
        }


        public Range addBeyondExisting(Range other) {
            if (ranges.isEmpty()) {
                ranges.add(other);
                return other;
            }
            List<Range> newRanges = new ArrayList<>();
            int index = 0;
            if (other.start > ranges.get(ranges.size() - 1).end) {
                newRanges.addAll(ranges);
                newRanges.add(other);
                ranges = newRanges;
                return other;
            }

            Range cursor = other;
            for (; index < ranges.size(); index++) {
                Range range = ranges.get(index);
                if (range.start > cursor.end) {
                    break;
                }

                if (cursor.start > range.end) {
                    newRanges.add(range);
                    continue;
                }

                if (cursor.start >= range.start && cursor.end <= range.end) {
                    cursor = null;
                    break;
                }

                if (cursor.start <= range.start && cursor.end <= range.end) {
                    newRanges.add(new Range(cursor.start, range.start - 1));
                    cursor = null;
                    break;
                }

                if (cursor.start >= range.start && cursor.end >= range.end) {
                    newRanges.add(range);
                    cursor.start = range.end + 1;
                }
            }
            if (cursor != null) newRanges.add(cursor);
            for (int i = index; i < ranges.size(); i++) {
                newRanges.add(ranges.get(i));
            }
            ranges = newRanges.stream().filter(range -> range.start <= range.end).collect(Collectors.toList());
            return cursor;
        }


    }
}
