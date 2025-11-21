package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.List;

public class Day8 {

    public static void main(String[] args) {
        task();

    }

    public static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day8.txt");
        int width = 25;
        int height = 6;
        int blockSize = width * height;
        List<List<Integer>> blocks = new ArrayList<>();
        List<Integer> cursor = new ArrayList<>();
        char[] charArray = lines.get(0).toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (i % blockSize == 0) {
                blocks.add(cursor);
                cursor = new ArrayList<>();
            }
            cursor.add(Character.getNumericValue(charArray[i]));
        }
        blocks.add(cursor);
        blocks.removeFirst();
        long minBlockNumber = Integer.MAX_VALUE;
        int minBlockIndex = -1;
        for (int i = 0; i < blocks.size(); i++) {
            List<Integer> block = blocks.get(i);
            long zeroes = block.stream().filter(value -> value == 0).count();
            if (zeroes < minBlockNumber) {
                minBlockNumber = zeroes;
                minBlockIndex = i;
            }
        }

        List<Integer> minBlock = blocks.get(minBlockIndex);
        long oneNumb = minBlock.stream().filter(value -> value == 1).count();
        long twoNumb = minBlock.stream().filter(value -> value == 2).count();
        long result = oneNumb * twoNumb;
        System.out.println("task1: " + result);

        List<Integer> finalBlock = new ArrayList<>();
        for (int i = 0; i < blockSize; i++) {
            boolean isTrans = true;
            for (List<Integer> block : blocks) {
                if (block.get(i) != 2) {
                    finalBlock.add(block.get(i));
                    isTrans = false;
                    break;
                }
            }
            if (isTrans) {
                finalBlock.add(2);
            }
        }

        System.out.println("task2: FGJUZ");
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                String print = "";
                if (finalBlock.get(row * width + col) == 0) {
                    print = " ";
                } else {
                    print = "#";
                }
                System.out.print(print);
            }
            System.out.println();
        }
    }

}
