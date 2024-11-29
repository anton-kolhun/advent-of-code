package com.example.aoc_2020;

import com.example.aoc_2023.helper.CRT;
import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day13 {

    public static void main(String[] args) throws Exception {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day13.txt");
        int busNumb = Integer.parseInt(lines.get(0));
        String tableStr = lines.get(1);
        List<Integer> times = ParseUtils.splitByDelimiter(tableStr, ",").stream()
                .filter(s -> !s.contains("x"))
                .map(Integer::parseInt)
                .toList();

        System.out.println(times);

        Integer minWaitingTime = Integer.MAX_VALUE;
        Integer minBus = 1;
        for (Integer time : times) {
            int pastBus = busNumb % time;
            int waitingTime = time - pastBus;
            if (waitingTime < minWaitingTime) {
                minWaitingTime = waitingTime;
                minBus = time;
            }
        }
        System.out.println(minWaitingTime * minBus);
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day13.txt");
        String tableStr = lines.get(1);
        List<String> times = ParseUtils.splitByDelimiter(tableStr, ",").stream()
                .toList();


        List<Integer> values = new ArrayList<>();
        values.add(Integer.parseInt(times.get(0)));

        Map<Integer, Integer> dependencies = new HashMap<>();

        for (int i = 1; i < times.size(); i++) {
            String value = times.get(i);
            if (!value.equals("x")) {
                dependencies.put(Integer.parseInt(value), i);
            }
        }
        System.out.println(dependencies);

        System.out.println("x = " + 0 + " mod " + times.get(0));
        List<BigInteger> aList = new ArrayList<>();
        List<BigInteger> nList = new ArrayList<>();
        aList.add(BigInteger.ZERO);
        nList.add(BigInteger.valueOf(Integer.parseInt(times.get(0))));
        for (Map.Entry<Integer, Integer> entry : dependencies.entrySet()) {
            int key = entry.getKey();
            int val = key - entry.getValue();
            while (val < 0) {
                val = key + val;
            }

            aList.add(BigInteger.valueOf(val));
            nList.add(BigInteger.valueOf(key));
            System.out.println("x = " + (val) + " mod " + key);
        }
        var result = CRT.chinese_remainder_theorem(aList, nList);
        System.out.println(result);

//        long cursor = values.get(0);
//        long res = -1;
//        while (true) {
//            boolean match = true;
//            for (Map.Entry<Integer, Integer> entry : dependencies.entrySet()) {
//                long shift = cursor + entry.getValue();
//                if (shift % entry.getKey() != 0) {
//                    match = false;
//                    break;
//                }
//            }
//            if (match) {
//                res = cursor;
//                break;
//            }
//            cursor = cursor + values.get(0);
//        }
//        System.out.println(res);

    }

}
