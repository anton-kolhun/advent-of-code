package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.List;

public class Day5 {


    public static void main(String[] args) {
        List<String> lines = FilesUtils.readFile("aoc_2019/day5.txt");
        List<Integer> items = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(Integer::parseInt)
                .toList();
        int res = runIntCode(items, new ArrayList<>(List.of(0)));
        System.out.println("task1: " + res);
        res = runIntCode(items, new ArrayList<>(List.of(5)));
        System.out.println("task2: " + res);
    }

    private static int processOperation(int code, int addr1, int addr2, int addr3,
                                        List<Integer> items, int cursor, List<Integer> inputs, List<Integer> outputs) {
        if (code == 1) {
            Integer result = items.get(addr1) + items.get(addr2);
            items.set(addr3, result);
            cursor = cursor + 4;
        } else if (code == 2) {
            Integer result = items.get(addr1) * items.get(addr2);
            items.set(addr3, result);
            cursor = cursor + 4;
        } else if (code == 3) {
            items.set(addr1, inputs.removeFirst());
            cursor = cursor + 2;
        } else if (code == 4) {
            cursor = cursor + 2;
            outputs.add(items.get(addr1));
        } else if (code == 5) {
            if (items.get(addr1) != 0) {
                cursor = items.get(addr2);
            } else {
                cursor = cursor + 3;
            }
        } else if (code == 6) {
            if (items.get(addr1) == 0) {
                cursor = items.get(addr2);
            } else {
                cursor = cursor + 3;
            }
        } else if (code == 7) {
            if (items.get(addr1) < items.get(addr2)) {
                items.set(addr3, 1);
            } else {
                items.set(addr3, 0);
            }
            cursor = cursor + 4;
        } else if (code == 8) {
            if (items.get(addr1).equals(items.get(addr2))) {
                items.set(addr3, 1);
            } else {
                items.set(addr3, 0);
            }
            cursor = cursor + 4;
        } else if (code == 99) {
            return Integer.MAX_VALUE;
        } else {
            String codeStr = String.valueOf(code);
            String opCodeStr = codeStr.substring(codeStr.length() - 2);
            int opCode = Integer.parseInt(opCodeStr);
            List<Integer> modes = new ArrayList<>();
            for (char c : codeStr.substring(0, codeStr.length() - 2).toCharArray()) {
                modes.add(Character.getNumericValue(c));
            }
            modes = modes.reversed();
            if (modes.size() == 2) {
                modes.add(0);
            }
            if (modes.get(0) == 1) {
                addr1 = cursor + 1;
            }
            if (modes.size() > 1 && modes.get(1) == 1) {
                addr2 = cursor + 2;
            }
            cursor = processOperation(opCode, addr1, addr2, addr3, items, cursor, inputs, outputs);
        }
        return cursor;
    }

    public static int runIntCode(List<Integer> items, List<Integer> inputs) {
        items = new ArrayList<>(items);
        int cursor = 0;
        List<Integer> outputs = new ArrayList<>();
        while (cursor < items.size()) {
            int code = items.get(cursor);
            int addr1 = -1;
            if ((cursor + 1) < items.size()) {
                addr1 = items.get(cursor + 1);
            }
            int addr2 = -1;
            if ((cursor + 2) < items.size()) {
                addr2 = items.get(cursor + 2);
            }
            int addr3 = -1;
            if ((cursor + 3) < items.size()) {
                addr3 = items.get(cursor + 3);
            }
            cursor = processOperation(code, addr1, addr2, addr3, items, cursor, inputs, outputs);
        }
        return outputs.getLast();
    }
}
