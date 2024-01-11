package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 {

    public static void main(String[] args) {
        //task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("day6.txt");
        List<RaceInfo> races = new ArrayList<>();
        List<String> timing = ParseUtils.splitByDelimiter(ParseUtils.splitByDelimiter(lines.get(0), ":").get(1), " ");
        timing = timing.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        List<String> distances = ParseUtils.splitByDelimiter(ParseUtils.splitByDelimiter(lines.get(1), ":").get(1), " ");
        distances = distances.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        for (int i = 0; i < timing.size(); i++) {
            Long time = Long.parseLong(timing.get(i));
            Long dist = Long.parseLong(distances.get(i));
            races.add(new RaceInfo(time, dist));
        }

        long total = 1;
        for (RaceInfo race : races) {
            long options = calculateOptions(race);
            total = total * options;
        }
        System.out.println(total);

    }





    public static void task2() {
        List<String> lines = FilesUtils.readFile("day6.txt");
        List<String> timing = ParseUtils.splitByDelimiter(ParseUtils.splitByDelimiter(lines.get(0), ":").get(1), " ");
        timing = timing.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        String timeStr = timing.stream().filter(s -> !s.isEmpty()).reduce((s, s2) -> s + s2).get();
        Long time = Long.parseLong(timeStr);

        List<String> distances = ParseUtils.splitByDelimiter(ParseUtils.splitByDelimiter(lines.get(1), ":").get(1), " ");
        String distance = distances.stream().filter(s -> !s.isEmpty()).reduce((s, s2) -> s + s2).get();
        Long dist = Long.parseLong(distance);
        var race = new RaceInfo(time, dist);

        long options = calculateOptions(race);
        System.out.println(options);

    }





    private static long calculateOptions(RaceInfo race) {
        int startTime = 1;
        int options = 0;
        for (; startTime <= race.time; startTime++) {
            long secondsLeft = race.time - startTime;
            int speed = startTime;
            long dist = secondsLeft * speed;
            if (dist > race.distToBeat) {
                options++;
            }
        }
        return options;
    }

    @AllArgsConstructor
    private static class RaceInfo {
        Long time;
        Long distToBeat;
    }
}
