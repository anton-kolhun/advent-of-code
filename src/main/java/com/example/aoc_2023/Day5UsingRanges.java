package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.aoc_2023.helper.RangeUtils.Range;
import static com.example.aoc_2023.helper.RangeUtils.RangeLine;


public class Day5UsingRanges {

    public static void main(String[] args) {
        task2();
    }

    //TODO: it's not working. should be fixed
    public static void task2() {
        RangeCol toSoil = new RangeCol();
        RangeCol toFertilizer = new RangeCol();
        RangeCol toWater = new RangeCol();
        RangeCol toLight = new RangeCol();
        RangeCol toTemperature = new RangeCol();
        RangeCol toHumidity = new RangeCol();
        RangeCol toLocation = new RangeCol();
        List<String> lines = FilesUtils.readFile("day5_t.txt");
        String line1 = lines.get(0);
        List<String> initialStrs = ParseUtils.splitByDelimiter(ParseUtils.splitByDelimiter(line1, ":").get(1), " ");
        initialStrs = initialStrs.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        RangeCol initial = new RangeCol();
        for (int i = 1; i < initialStrs.size(); i++) {
            if ((i % 2) != 0) {
                long startWith = Long.parseLong(initialStrs.get(i - 1));
                long length = Long.parseLong(initialStrs.get(i));
//                for (int step = 0; step < length; step++) {
                initial.ranges.add(new RangeInfo(length, startWith));
//                }
            }
        }
        var cursor = toSoil;
        for (int row = 2; row < lines.size(); row++) {
            String line = lines.get(row);
            if (line.isEmpty()) continue;
            if (line.contains("seed-to-soil")) {
                cursor = toSoil;
                continue;
            }
            if (line.contains("soil-to-fertilizer")) {
                cursor = toFertilizer;
                continue;
            } else if (line.contains("fertilizer-to-water")) {
                cursor = toWater;
                continue;
            } else if (line.contains("water-to-light")) {
                cursor = toLight;
                continue;
            } else if (line.contains("light-to-temperature")) {
                cursor = toTemperature;
                continue;
            } else if (line.contains("temperature-to-humidity")) {
                cursor = toHumidity;
                continue;
            } else if (line.contains("humidity-to-location")) {
                cursor = toLocation;
                continue;
            }
            List<String> strs = ParseUtils.splitByDelimiter(line, " ");
            List<Long> numbers = strs.stream().map(Long::parseLong).toList();
            var r = new RangeInfo();
            r.length = numbers.get(2);
            r.source = numbers.get(1);
            r.destination = numbers.get(0);
            cursor.ranges.add(r);
        }

        RangeLine line = new RangeLine();
        splitRanges(line, toSoil.ranges);
        splitRanges(line, toFertilizer.ranges);
        splitRanges(line, toWater.ranges);
        splitRanges(line, toLight.ranges);
        splitRanges(line, toTemperature.ranges);
        splitRanges(line, toHumidity.ranges);
        splitRanges(line, toLocation.ranges);

        List<RangeCol> allRangeCols = Arrays.asList(toSoil, toFertilizer, toWater, toLight,
                toTemperature, toHumidity, toLocation);

        long result = calculateViaRanges(line.ranges, initial, allRangeCols);
        System.out.println("range res = " + result);

        long min = Long.MAX_VALUE;
        long minIndex = -1;
        for (RangeInfo range : initial.ranges) {
            for (long i = range.source; i < range.source + range.length; i++) {
                Long res = convert(allRangeCols, i);
                if (res < min) {
                    min = res;
                    minIndex = i;
                }
            }
        }
        System.out.println("non range esult = " + min + "; minIndex = " + minIndex);

        long previous = -1;
        for (int i = 0; i <= 100; i++) {
            long res = convert(allRangeCols, i);
            if (res != previous + 1) {
                System.out.print(i + " ");
            }
            previous = res;
        }
        System.out.println();
    }


    private static long calculateViaRanges(List<Range> ranges, RangeCol source, List<RangeCol> cols) {
        long min2 = Integer.MAX_VALUE;
        for (Range range : ranges) {
            boolean isGood = false;
            for (RangeInfo rangeInfo : source.ranges) {
                if (range.start >= rangeInfo.source && range.start <= rangeInfo.source + rangeInfo.length) {
                    isGood = true;
                    break;
                }
            }
            if (isGood) {
                long res = convert(cols, range.start);
                if (res < min2) {
                    min2 = res;
                }
            }
        }

        for (RangeInfo range : source.ranges) {
            long res = convert(cols, range.source);
            if (res < min2) {
                min2 = res;
            }
        }

        return min2;
    }

    private static Long convert(List<RangeCol> cols, long i) {
        Long res = i;
        for (RangeCol col : cols) {
            res = col.convert(res);
        }
        return res;
    }

    private static void splitRanges(RangeLine line, List<RangeInfo> ranges) {
        for (int i = 0; i < ranges.size(); i++) {
            RangeInfo range = ranges.get(i);
            line.addBeyondExisting(new Range(range.source, range.source + range.length));
        }
         System.out.println("done");
    }


    @AllArgsConstructor
    @NoArgsConstructor
    private static class RangeInfo {
        long length;
        long source;
        long destination;

        private long convert(long src) {
            if (src < source || src > (source + length)) {
                return src;
            }
            return destination + (src - source);
        }

        public RangeInfo(long length, long source) {
            this.length = length;
            this.source = source;
        }
    }

    private static class RangeCol {
        List<RangeInfo> ranges = new ArrayList<>();

        private long convert(long src) {
            for (RangeInfo range : ranges) {
                long res = range.convert(src);
                if (res != src) {
                    return res;
                }
            }
            return src;

        }
    }

}
