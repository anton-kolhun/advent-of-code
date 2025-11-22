package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.List;

public class Day7 {

    public static void main(String[] args) {
//        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day7.txt");
        List<Integer> rawItems = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(Integer::parseInt)
                .toList();
        List<List<Integer>> inputSets = findAllSets(List.of(0, 1, 2, 3, 4), new ArrayList<>())
                .stream()
                .toList();
        int max = Integer.MIN_VALUE;
        for (List<Integer> inputs : inputSets) {
            int currentInput = 0;
            for (int i = 0; i < inputs.size(); i++) {
                List<Integer> items = new ArrayList<>(rawItems);
                List<Integer> outputs = new ArrayList<>();
                List<Integer> currentInputs = new ArrayList<>(List.of(inputs.get(i), currentInput));
                List<Integer> results = Day5.runIntCode(currentInputs, items, outputs);
                currentInput = results.getFirst();
            }
            max = Math.max(max, currentInput);
        }
        System.out.println(max);
    }

    private static List<List<Integer>> findAllSets(List<Integer> remNumbs, List<Integer> currentNumbs) {
        if (remNumbs.isEmpty()) {
            return List.of(currentNumbs);
        }
        List<List<Integer>> total = new ArrayList<>();
        for (int i = 0; i < remNumbs.size(); i++) {
            Integer numb = remNumbs.get(i);
            List<Integer> nextNumbs = new ArrayList<>(currentNumbs);
            nextNumbs.add(numb);
            List<Integer> nextRemNumbs = new ArrayList<>(remNumbs);
            nextRemNumbs.remove(i);
            var res = findAllSets(nextRemNumbs, nextNumbs);
            total.addAll(res);
        }
        return total;
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day7.txt");
        List<Integer> rawItems = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(Integer::parseInt)
                .toList();
        List<List<Integer>> inputSets = findAllSets(List.of(9, 8, 7, 6, 5), new ArrayList<>())
                .stream()
                .toList();
        long max = Long.MIN_VALUE;
        for (List<Integer> inputs : inputSets) {
            int steps = 0;
            List<Integer> currentInputs = new ArrayList<>();
            int currentInput = 0;
//            currentInputs.add(0);
            List<Integer> outputs = new ArrayList<>();
            List<Integer> items = new ArrayList<>(rawItems);
            while (steps < 100) {
                System.out.println(steps);
                for (int i = 0; i < inputs.size(); i++) {
//                List<Integer> outputs = new ArrayList<>();
//                    if (steps == 0) {
                    currentInputs = new ArrayList<>(List.of(inputs.get(i), currentInput));
//                    }
                    List<Integer> results = runIntCode(currentInputs, items, outputs);
                    currentInput = results.getFirst();
                }
                steps++;
            }
            max = Math.max(max, outputs.getLast());
        }
        System.out.println(max);
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

    public static List<Integer> runIntCode(List<Integer> inputs, List<Integer> items, List<Integer> outputs) {
        items = new ArrayList<>(items);
        int cursor = 0;
        while (cursor < items.size()) {
            int code = items.get((cursor));
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
        return outputs;
    }

}