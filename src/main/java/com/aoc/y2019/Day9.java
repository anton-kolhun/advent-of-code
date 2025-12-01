package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day9 {

    public static void main(String[] args) {
        task1();
    }

    private static void task1() {

        List<String> lines = FilesUtils.readFile("aoc_2019/day9.txt");
        List<BigInteger> rawItems = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(BigInteger::new)
                .toList();
        Map<BigInteger, BigInteger> items = new HashMap<>();
        for (int i = 0; i < rawItems.size(); i++) {
            items.put(BigInteger.valueOf(i), rawItems.get(i));
        }

        List<BigInteger> outputs = new ArrayList<>();
        List<BigInteger> inputs = new ArrayList<>();
        inputs.add(BigInteger.ONE);
        RelativeBaseState relativeBaseState = new RelativeBaseState(BigInteger.ZERO);
        runIntCode(inputs, items, outputs, relativeBaseState);

    }

    private static int processOperation(int code, BigInteger addr1, BigInteger addr2,
                                        BigInteger addr3, Map<BigInteger, BigInteger> items, int cursor,
                                        List<BigInteger> inputs, List<BigInteger> outputs,
                                        RelativeBaseState relativeBase) {
        if (code == 1) {
            BigInteger result = items.get(addr1).add(items.get(addr2));
            items.put(addr3, result);
            cursor = cursor + 4;
        } else if (code == 2) {
            BigInteger result = items.get(addr1).multiply(items.get(addr2));
            items.put(addr3, result);
            cursor = cursor + 4;
        } else if (code == 3) {
            BigInteger removed = inputs.removeFirst();
            items.put(addr1, removed);
            cursor = cursor + 2;
        } else if (code == 4) {
            cursor = cursor + 2;
            outputs.add(items.get(addr1));
        } else if (code == 5) {
            if (items.get(addr1).compareTo(BigInteger.ZERO) != 0) {
                cursor = items.getOrDefault(addr2, BigInteger.ZERO).compareTo(BigInteger.ZERO);
            } else {
                cursor = cursor + 3;
            }
        } else if (code == 6) {
            if (items.getOrDefault(addr1, BigInteger.ZERO).compareTo(BigInteger.ZERO) == 0) {
                cursor = items.get(addr2).intValue();
            } else {
                cursor = cursor + 3;
            }
        } else if (code == 7) {
            if (items.getOrDefault(addr1, BigInteger.ZERO).compareTo(items.getOrDefault(addr2, BigInteger.ZERO)) < 0) {
                items.put(addr3, BigInteger.ONE);
            } else {
                items.put(addr3, BigInteger.ZERO);
            }
            cursor = cursor + 4;
        } else if (code == 8) {
            if (items.getOrDefault(addr1, BigInteger.ZERO).equals(items.getOrDefault(addr2, BigInteger.ZERO))) {
                items.put(addr3, BigInteger.ONE);
            } else {
                items.put(addr3, BigInteger.ZERO);
            }
            cursor = cursor + 4;
        } else if (code == 9) {
            relativeBase.value = relativeBase.value.add(items.get(addr1));
            cursor = cursor + 2;
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
                addr1 = BigInteger.valueOf(cursor + 1);
            }
            if (modes.size() > 1 && modes.get(1) == 1) {
                addr2 = BigInteger.valueOf(cursor + 2);
            }
            cursor = processOperation(opCode, addr1, addr2, addr3, items, cursor, inputs, outputs, relativeBase);
        }
        return cursor;
    }

    public static List<BigInteger> runIntCode(List<BigInteger> inputs, Map<BigInteger, BigInteger> items, List<BigInteger> outputs,
                                              RelativeBaseState relativeBase) {
        items = new HashMap<>(items);
        int cursor = 0;
        while (cursor < items.size()) {
            int code = items.getOrDefault(BigInteger.valueOf(cursor), BigInteger.ZERO).intValue();
            BigInteger addr1 = BigInteger.valueOf(-1);
            if ((cursor + 1) < items.size()) {
                addr1 = items.getOrDefault(BigInteger.valueOf(cursor + 1), BigInteger.ZERO);
            }
            BigInteger addr2 = BigInteger.valueOf(-1);
            if ((cursor + 2) < items.size()) {
                addr2 = items.getOrDefault(BigInteger.valueOf(cursor + 2), BigInteger.ZERO);
            }
            BigInteger addr3 = BigInteger.valueOf(-1);
            if ((cursor + 3) < items.size()) {
                addr3 = items.getOrDefault(BigInteger.valueOf(cursor + 3), BigInteger.ZERO);
            }
            cursor = processOperation(code, addr1, addr2, addr3, items, cursor, inputs, outputs, relativeBase);
        }
        return outputs;
    }

    public static class RelativeBaseState {
        BigInteger value;

        public RelativeBaseState(BigInteger relativeBase) {
            this.value = relativeBase;
        }
    }

}
