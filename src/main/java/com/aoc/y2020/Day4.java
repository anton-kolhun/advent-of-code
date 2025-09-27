package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day4 {

    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day4.txt");
        List<Passport> passports = new ArrayList<>();
        Passport cursor = new Passport();
        for (String line : lines) {
            if (line.isEmpty()) {
                passports.add(cursor);
                cursor = new Passport();
                continue;
            }
            List<String> data = ParseUtils.splitByDelimiter(line, " ");
            Map<String, String> passData = new HashMap<>();
            for (String datum : data) {
                passData.put(datum.split(":")[0], datum.split(":")[1]);
            }
            cursor.data.putAll(passData);
        }
        passports.add(cursor);
        int validTask1Counter = 0;
        int validTask2Counter = 0;
        for (Passport passport : passports) {
            if (passport.isValidTask1()) {
                validTask1Counter++;
            }
            if (passport.isValidTask2()) {
                validTask2Counter++;
            }
        }

        System.out.println("task1: " + validTask1Counter);
        System.out.println("task2: " + validTask2Counter);

    }

    private static class Passport {
        Map<String, String> data = new HashMap<>();

        Set<String> eyeColors = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

        boolean isValidTask1() {
            if (!data.containsKey("byr")) {
                return false;
            }
            if (!data.containsKey("iyr")) {
                return false;
            }
            if (!data.containsKey("eyr")) {
                return false;
            }
            if (!data.containsKey("hgt")) {
                return false;
            }
            if (!data.containsKey("hcl")) {
                return false;
            }
            if (!data.containsKey("ecl")) {
                return false;
            }
            if (!data.containsKey("pid")) {
                return false;
            }
            return true;
        }

        boolean isValidTask2() {
            String birth = data.getOrDefault("byr", "9999");
            int birthYear = Integer.parseInt(birth);
            if (birthYear < 1920 || birthYear > 2002) {
                return false;
            }

            String iyear = data.getOrDefault("iyr", "9999");
            int issueYear = Integer.parseInt(iyear);
            if (issueYear < 2010 || issueYear > 2020) {
                return false;
            }

            String eyear = data.getOrDefault("eyr", "9999");
            int expYear = Integer.parseInt(eyear);
            if (expYear < 2020 || expYear > 2030) {
                return false;
            }

            String heightStr = data.getOrDefault("hgt", "200cm");
            if (heightStr.endsWith("cm")) {
                if (heightStr.length() != 5) {
                    return false;
                }
                int height = Integer.parseInt(heightStr.substring(0, 3));
                if (height < 150 || height > 193) {
                    return false;
                }
            } else if (heightStr.endsWith("in")) {
                if (heightStr.length() != 4) {
                    return false;
                }
                int height = Integer.parseInt(heightStr.substring(0, 2));
                if (height < 59 || height > 76) {
                    return false;
                }
            } else {
                return false;
            }

            String hairColor = data.getOrDefault("hcl", "yyyy");
            if (hairColor.length() != 7) {
                return false;
            }
            if (hairColor.charAt(0) != '#') {
                return false;
            }
            char[] charArray = hairColor.toCharArray();
            for (int i = 1; i < charArray.length; i++) {
                char c = charArray[i];
                if (!Character.isDigit(c) && (c < 'a' || c > 'f')) {
                    return false;
                }
            }

            String eyeColor = data.getOrDefault("ecl", "none");
            if (!eyeColors.contains(eyeColor)) {
                return false;
            }

            String pid = data.getOrDefault("pid", "none");
            if (pid.length() != 9) {
                return false;
            }
            for (char c : pid.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }
    }

}
