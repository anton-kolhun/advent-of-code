package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;

import java.util.List;

public class Day25 {

    public static void main(String[] args) {
        task();
    }

    private static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day25.txt");
        long pk1 = Long.parseLong(lines.get(0));
        long pk2 = Long.parseLong(lines.get(1));
        long sn = 7;
        int loop_size = 0;
        long value = 1;
        while (value != pk1) {
            value = value * sn;
            value = value % 20201227;
            loop_size++;
        }
        value = 1;
        sn = pk2;
        for (int i = 0; i < loop_size; i++) {
            value = value * sn;
            value = value % 20201227;
        }
        System.out.println(value);
    }

}
