package com.aoc;

import java.util.ArrayList;
import java.util.List;

public class MaxSubArray {

    public static void main(String[] args) {
//        List<Integer> res = maxSubarray(List.of(-1, 2, 3, -4, 5, 10));
//        List<Integer> res = maxSubarray(List.of(2, -1, 2, 3, 4, -5));
        List<Integer> res = maxSubarray(List.of(-2, -3, -1, 0, -4, -6));

        System.out.println(res);
    }

    public static List<Integer> maxSubarray(List<Integer> arr) {

        int maxValue = Integer.MIN_VALUE;
        boolean isAnyPositive = false;
        for (Integer i : arr) {
            if (i > maxValue) {
                maxValue = i;
            }
            if (i >= 0) {
                isAnyPositive = true;
            }
        }
        if (!isAnyPositive) {
            List<Integer> res = new ArrayList<>();
            res.add(maxValue);
            res.add(maxValue);
            return res;
        }


        int sum = 0;
        List<Integer> maxPair = new ArrayList<>();
        int curMax = Integer.MIN_VALUE;
        int startIndex = 0;
        for (int index = 0; index < arr.size(); index++) {
            Integer el = arr.get(index);
            int previousSum = sum;
            if (el < 0) {
                if ((previousSum) > curMax) {
                    curMax = previousSum;
                    maxPair = new ArrayList<>();
                    maxPair.add(startIndex);
                    maxPair.add(index - 1);
                }
            }
            sum = previousSum + el;
            if (sum < 0) {
                sum = 0;
                startIndex = index + 1;
            }
        }
        if (sum > curMax) {
            curMax = sum;
            maxPair = new ArrayList<>();
            maxPair.add(startIndex);
            maxPair.add(arr.size() - 1);
        }

        List<Integer> maxSubarray = arr.subList(maxPair.get(0), maxPair.get(1) + 1);

        int sumOfSubArrayEls = maxSubarray.stream().mapToInt(value -> value).sum();

        int sumOfSubSeq = arr.stream()
                .filter(value -> value > 0)
                .mapToInt(value -> value)
                .sum();

        List<Integer> res = new ArrayList<>();
        res.add(sumOfSubArrayEls);
        res.add(sumOfSubSeq);
        return res;
    }
}
