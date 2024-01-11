package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;

import java.util.List;

public class Day2 {

    private static int BLUE_MAX = 14;
    private static int RED_MAX = 12;
    private static int GREEN_MAX = 13;

    public static void main(String[] args) {
//        task1();
        task2();
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("day2.txt");
        int total = 0;
        for (String line : lines) {
            //String[] data = line.split(" ");
//            List<String> data = ParseUtils.splitByDelimiter(line, " ");

            String colorInfo = line.substring(line.indexOf(":") + 1);
            List<String> colorRounds = ParseUtils.splitByDelimiter(colorInfo, ";");

            int maxBlue = Integer.MIN_VALUE;
            int maxRed = Integer.MIN_VALUE;
            int maxGreen = Integer.MIN_VALUE;
            for (String colorRound : colorRounds) {
                var info = calcRound(colorRound);
                if (info.blue > maxBlue) {
                    maxBlue = info.blue;
                }
                if (info.red > maxRed) {
                    maxRed = info.red;
                }
                if (info.green > maxGreen) {
                    maxGreen = info.green;
                }
            }
            int multiple = maxBlue * maxRed * maxGreen;

            total += multiple;
        }
        System.out.println(total);

    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("day2.txt");
        int total = 0;
        for (String line : lines) {
            String[] data = line.split(" ") ;
            ParseUtils.splitByDelimiter(line, " ");
            int index = Integer.parseInt(data[1].substring(0, data[1].length() - 1));
            int currentIndex = 3;
            boolean shouldCalculate = true;

            String colorInfo = line.substring(line.indexOf(":") + 1);

            String[] colorRounds = colorInfo.split(";");

            for (String colorRound : colorRounds) {
                var info = calcRound(colorRound);
                if (info.blue > BLUE_MAX || info.red > RED_MAX || info.green > GREEN_MAX) {
                    shouldCalculate = false;
                }
            }

            if (shouldCalculate)
                total += index;
        }
        System.out.println(total);
    }


    private static ColorInfo calcRound(String roundData) {
        String[] data = roundData.split(",");

        int blue = 0;
        int red = 0;
        int green = 0;
        for (int i = 0; i < data.length; i++) {
            String[] pair = data[i].trim().split(" ");
            String color = pair[1];
            int amount = Integer.parseInt(pair[0]);
            if (color.equals("red")) {
                red = amount;
            } else if (color.equals("blue")) {
                blue = amount;
            } else if (color.equals("green")) {
                green = amount;
            }
        }

        return new ColorInfo(blue, red, green);
    }

    @AllArgsConstructor
    private static class ColorInfo {
        private int blue;
        private int red;
        private int green;
    }
}
