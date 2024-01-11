package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Day5 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        task1();
        task2();

    }

    public static void task1() {
        RangeCol toSoil = new RangeCol();
        RangeCol toFertilizer = new RangeCol();
        RangeCol toWater = new RangeCol();
        RangeCol toLight = new RangeCol();
        RangeCol toTemperature = new RangeCol();
        RangeCol toHumidity = new RangeCol();
        RangeCol toLocation = new RangeCol();
        List<String> lines = FilesUtils.readFile("day5.txt");
        String line1 = lines.get(0);
        List<String> initialStrs = ParseUtils.splitByDelimiter(ParseUtils.splitByDelimiter(line1, ":").get(1), " ");
        List<Long> initial = initialStrs.stream().filter(s -> !s.isEmpty()).map(Long::parseLong).toList();
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


        long min = Long.MAX_VALUE;
        for (Long value : initial) {
            Long v1 = toSoil.convert(value);
            Long v2 = toFertilizer.convert(v1);
            Long v3 = toWater.convert(v2);
            Long v4 = toLight.convert(v3);
            Long v5 = toTemperature.convert(v4);
            Long v6 = toHumidity.convert(v5);
            Long v7 = toLocation.convert(v6);
            if (v7 < min) {
                min = v7;
            }
        }
        System.out.println(min);

    }


    public static void task2() throws ExecutionException, InterruptedException {
        RangeCol toSoil = new RangeCol();
        RangeCol toFertilizer = new RangeCol();
        RangeCol toWater = new RangeCol();
        RangeCol toLight = new RangeCol();
        RangeCol toTemperature = new RangeCol();
        RangeCol toHumidity = new RangeCol();
        RangeCol toLocation = new RangeCol();
        List<String> lines = FilesUtils.readFile("day5.txt");
        String line1 = lines.get(0);
        List<String> initialStrs = ParseUtils.splitByDelimiter(ParseUtils.splitByDelimiter(line1, ":").get(1), " ");
        initialStrs = initialStrs.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        RangeCol initial = new RangeCol();
        for (int i = 1; i < initialStrs.size(); i++) {
            if ((i % 2) != 0) {
                long startWith = Long.parseLong(initialStrs.get(i - 1));
                long length = Long.parseLong(initialStrs.get(i));
                initial.ranges.add(new RangeInfo(length, startWith));
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


        var executors = Executors.newFixedThreadPool(10);
        List<Future<Long>> results = new ArrayList<>();
        for (RangeInfo range : initial.ranges) {
            Future<Long> res = executors.submit(() -> {
                long min = Long.MAX_VALUE;
                for (long i = range.source; i < range.source + range.length; i++) {
                    Long v1 = toSoil.convert(i);
                    Long v2 = toFertilizer.convert(v1);
                    Long v3 = toWater.convert(v2);
                    Long v4 = toLight.convert(v3);
                    Long v5 = toTemperature.convert(v4);
                    Long v6 = toHumidity.convert(v5);
                    Long v7 = toLocation.convert(v6);
                    if (v7 < min) {
                        min = v7;
                    }
                }
                System.out.println(min);
                return min;
            });
            results.add(res);
        }
        long min = Integer.MAX_VALUE;
        for (Future<Long> result : results) {
            Long res = result.get();
            if (res < min) {
                min = res;
            }
        }
        System.out.println(min);
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
