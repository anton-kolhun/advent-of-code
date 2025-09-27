package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;

import java.util.HashSet;
import java.util.Set;

public class Day5 {

    public static void main(String[] args) {
        task();
    }

    public static void task() {
        Set<Integer> results = new HashSet<>();
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (String line : FilesUtils.readFile("aoc_2020/day5.txt")) {
            int low = 0;
            int high = 127;
            for (int i = 0; i < 7; i++) {
                char c = line.charAt(i);
                int mid = (high - low + 1) / 2 + low;
                if (c == 'F') {
                    high = mid - 1;
                } else {
                    low = mid;
                }
            }
            int row = low;

            char[] charArray = line.toCharArray();
            low = 0;
            high = 7;
            for (int i = 7; i < charArray.length; i++) {
                char c = line.charAt(i);
                int mid = (high - low + 1) / 2 + low;
                if (c == 'L') {
                    high = mid - 1;
                } else {
                    low = mid;
                }
            }
            int col = low;
            int res = row * 8 + col;
            if (res > max) {
                max = res;
            }
            if (res < min) {
                min = res;
            }
            results.add(res);
        }

        System.out.println("task1 = " + max);

        int myTicket = -1;
        for (int i = min; i <= max; i++) {
            if (!results.contains(i)) {
                if (results.contains(i - 1) && results.contains(i + 1)) {
                    myTicket = i;
                    break;
                }
            }
        }
        System.out.println("task2 = " + myTicket);
    }
}
