package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day9 {


    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day9.txt");
        String input = lines.get(0);
        Map<Integer, String> indexToValue = new HashMap<>();
        List<Integer> freeSpaces = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        char[] charArray = input.toCharArray();
        int currentFileIndex = 0;
        int cursor = 0;
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            if ((i % 2) == 0) {
                for (int j = 0; j < Integer.parseInt(String.valueOf(c)); j++) {
                    sb.append(currentFileIndex);
                    indexToValue.put(cursor, String.valueOf(currentFileIndex));
                    cursor++;
                }
                currentFileIndex++;
            } else {
                freeSpaces.add(i);
                for (int j = 0; j < Integer.parseInt(String.valueOf(c)); j++) {
                    sb.append(".");
                    indexToValue.put(cursor, ".");
                    cursor++;
                }
            }
        }


       // System.out.println(sb.toString());

        Map<Integer, String> updatedMap = new HashMap<>();

        int leftIndex = 0;
        int rightIndex = indexToValue.size() - 1;
        StringBuffer updated = new StringBuffer();
        while (leftIndex <= rightIndex) {
            if (indexToValue.get(leftIndex).equals(String.valueOf('.'))) {
                while (indexToValue.get(rightIndex).equals(String.valueOf('.'))) {
                    rightIndex--;
                }
                if (leftIndex > rightIndex) {
                    break;
                }
                updated.append(indexToValue.get(rightIndex));
                updatedMap.put(leftIndex, indexToValue.get(rightIndex));
                rightIndex--;

            } else {
                updated.append(indexToValue.get(leftIndex));
                updatedMap.put(leftIndex, indexToValue.get(leftIndex));
            }
            leftIndex++;
        }


        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < updatedMap.size(); i++) {
            sum = sum.add(BigInteger.valueOf(Long.parseLong(String.valueOf(updatedMap.get(i))) * i));
        }

        System.out.println(sum.toString());

    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day9.txt");
        String input = lines.get(0);
        Map<Integer, String> indexToValue = new HashMap<>();
        Map<String, Integer> blockToSize = new HashMap<>();
        char[] charArray = input.toCharArray();
        int currentFileIndex = 0;
        int cursor = 0;
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            if ((i % 2) == 0) {
                for (int j = 0; j < Integer.parseInt(String.valueOf(c)); j++) {
                    indexToValue.put(cursor, String.valueOf(currentFileIndex));
                    cursor++;
                }
                blockToSize.put(String.valueOf(currentFileIndex), Integer.parseInt(String.valueOf(c)));
                currentFileIndex++;
            } else {
                for (int j = 0; j < Integer.parseInt(String.valueOf(c)); j++) {
                    indexToValue.put(cursor, ".");
                    cursor++;
                }
            }
        }
        //printMap(indexToValue);
        int rightIndex = indexToValue.size() - 1;
        while (rightIndex >= 0) {
            while (indexToValue.get(rightIndex).contains(".")) {
                rightIndex--;
            }
            String blockValue = indexToValue.get(rightIndex);
            int blockSize = blockToSize.get(blockValue);
            insertBlock(blockSize, indexToValue, 0, rightIndex, blockValue);
            rightIndex = rightIndex - blockSize;
        }

        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < indexToValue.size(); i++) {
            if (".".equals(indexToValue.get(i))) {
                continue;
            }
            sum = sum.add(BigInteger.valueOf(Long.parseLong(String.valueOf(indexToValue.get(i))) * i));
        }
        System.out.println(sum);
    }


    private static void insertBlock(int blockSize, Map<Integer, String> updatedMap, int leftIndex, int rightIndex,
                                    String blockValue) {

        if (leftIndex > rightIndex) {
            return;
        }
        while (!updatedMap.get(leftIndex).equals(".")) {
            leftIndex++;
            if (leftIndex > rightIndex) {
                return;
            }
        }
        int currentGap = 1;
        while (updatedMap.get(leftIndex + currentGap).equals(".")) {
            currentGap++;
        }
        if (blockSize <= currentGap) {
            for (int i = 0; i < blockSize; i++) {
                updatedMap.put(leftIndex, blockValue);
                updatedMap.put(rightIndex, ".");
                leftIndex++;
                rightIndex--;
            }
        } else {
            insertBlock(blockSize, updatedMap, leftIndex + currentGap, rightIndex, blockValue);
        }
    }

    private static void printMap(Map<Integer, String> updatedMap) {
        int size = updatedMap.size();
        for (int i = 0; i < size; i++) {
            System.out.print(updatedMap.get(i));
        }

        System.out.println();
    }


}
