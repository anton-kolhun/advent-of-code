package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;

import java.math.BigInteger;
import java.util.List;

public class Day16 {
    public static void main(String[] args) {
        task2();
    }

    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day16.txt");
        String encoded = lines.get(0);
        String binaryView = "";
        char[] charArray = encoded.toCharArray();
        for (int i = charArray.length - 1; i >= 0; i--) {
            int value = Integer.parseInt(String.valueOf(encoded.charAt(i)), 16);
            StringBuilder binaryVal = new StringBuilder(Integer.toBinaryString(value));
            while (binaryVal.length() < 4) {
                binaryVal.insert(0, "0");
            }
            binaryView = binaryVal + binaryView;
        }
        Result res = parseV2(binaryView);

        System.out.println("task1: " + res.versionSum);
        System.out.println("task2: " + res.value);
    }

    private static Result parseV2(String binaryView) {
        int version = Integer.parseInt(binaryView.substring(0, 3), 2);
        String typeId = binaryView.substring(3, 6);
        int typeDecimal = Integer.parseInt(typeId, 2);
        int cursor = 6;
        if (typeDecimal == 4) {
            int literalLength = 6;
            int groupLength = 5;
            boolean isLastGroup = false;
            StringBuffer groupView = new StringBuffer();
            while (!isLastGroup) {
                String group = binaryView.substring(cursor, cursor + groupLength);
                cursor += groupLength;
                if (group.startsWith("0")) {
                    isLastGroup = true;
                }
                String groupValue = group.substring(1);
                groupView.append(groupValue);
                literalLength = literalLength + groupLength;
            }
            long value = Long.parseLong(groupView.toString(), 2);
            return new Result(version, cursor, BigInteger.valueOf(value));
        } else {
            int typeLength = Integer.parseInt(binaryView.substring(cursor, cursor + 1));
            cursor += 1;
            if (typeLength == 0) {
                int subPacketsLength = Integer.parseInt(binaryView.substring(cursor, cursor + 15), 2);
                cursor += 15;
                Result total = new Result(0, 0, BigInteger.ZERO);
                BigInteger value = null;
                while (total.cursor < subPacketsLength) {
                    String subPackets = binaryView.substring(cursor + total.cursor);
                    Result res = parseV2(subPackets);
                    value = calculateValue(typeDecimal, value, res);
                    total = new Result(total.versionSum + res.versionSum, total.cursor + res.cursor, BigInteger.ZERO);
                }
                cursor = cursor + subPacketsLength;
                return new Result(version + total.versionSum, cursor, value);
            } else if (typeLength == 1) {
                int subPacketsNumber = Integer.parseInt(binaryView.substring(cursor, cursor + 11), 2);
                cursor += 11;
                Result total = new Result(0, 0, BigInteger.ZERO);
                BigInteger value = null;
                for (int times = 0; times < subPacketsNumber; times++) {
                    String subPackets = binaryView.substring(cursor);
                    Result res = parseV2(subPackets);
                    cursor = cursor + res.cursor;
                    value = calculateValue(typeDecimal, value, res);
                    total = new Result(total.versionSum + res.versionSum, cursor, BigInteger.ZERO);
                }
                return new Result(version + total.versionSum, cursor, value);
            }
        }
        return null;
    }

    private static BigInteger calculateValue(int typeDecimal, BigInteger value, Result res) {
        if (value == null) {
            return res.value();
        }
        if (typeDecimal == 0) {
            value = value.add(res.value);
        } else if (typeDecimal == 1) {
            value = value.multiply(res.value);
        } else if (typeDecimal == 2) {
            value = res.value.compareTo(value) < 0 ? res.value : value;
        } else if (typeDecimal == 3) {
            value = res.value.compareTo(value) > 0 ? res.value : value;
        } else if (typeDecimal == 5) {
            if (value.compareTo(res.value) > 0) {
                value = BigInteger.ONE;
            } else {
                value = BigInteger.ZERO;
            }
        } else if (typeDecimal == 6) {
            if (value.compareTo(res.value) < 0) {
                value = BigInteger.ONE;
            } else {
                value = BigInteger.ZERO;
            }
        } else if (typeDecimal == 7) {
            if (value.equals(res.value)) {
                value = BigInteger.ONE;
            } else {
                value = BigInteger.ZERO;
            }

        }
        return value;
    }

    private record Result(int versionSum, int cursor, BigInteger value) {
    }


}
