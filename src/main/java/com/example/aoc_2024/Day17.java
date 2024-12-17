package com.example.aoc_2024;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Day17 {

    private static ExecutorService es = Executors.newFixedThreadPool(10);


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1();
        task2();
    }


    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day17.txt");
        Map<String, Integer> register = new HashMap<>();
        int line;
        for (line = 0; line < lines.size(); line++) {
            String l = lines.get(line);
            if (l.isEmpty()) {
                line++;
                break;
            }
            var parts = ParseUtils.splitByDelimiter(l, ":").stream().map(String::trim).toList();
            var registerParts = ParseUtils.splitByDelimiter(parts.get(0), " ").stream().map(String::trim).toList();
            register.put(registerParts.get(1), Integer.parseInt(parts.get(1)));

        }

        var programStr = ParseUtils.splitByDelimiter(lines.get(line), ":").stream().map(String::trim).toList();
        List<Integer> program = ParseUtils.splitByDelimiter(programStr.get(1), ",")
                .stream().map(Integer::parseInt).toList();

        StringBuffer output = new StringBuffer();
        int pointer = 0;
        var registerCursor = new HashMap<>(register);
        while (pointer < program.size()) {
            Integer instruction = program.get(pointer);
            Integer operand = program.get(pointer + 1);
            pointer = processInstruction(registerCursor, instruction, operand, output, pointer);
        }
        System.out.println(output.substring(0, output.length() - 1));

    }

    private static int processInstruction(Map<String, Integer> register,
                                          Integer instr, Integer op, StringBuffer output, int currentPointer) {
        if (instr == 0) {
            var numerator = register.get("A");
            double denomValue = Math.pow(2, combo(op, register));
            int res = (int) (numerator / denomValue);
            register.put("A", res);
        }
        if (instr == 1) {
            int res = register.get("B") ^ op;
            register.put("B", res);
        }
        if (instr == 2) {
            int res = combo(op, register) % 8;
            register.put("B", res);
        }

        if (instr == 3) {
            if (register.get("A") == 0) {
                return currentPointer + 2;
            }
            currentPointer = op;
            return currentPointer;
        }

        if (instr == 4) {
            int res = register.get("B") ^ register.get("C");
            register.put("B", res);
        }

        if (instr == 5) {
            int res = combo(op, register) % 8;
            output.append(res + ",");
        }

        if (instr == 6) {
            var numerator = register.get("A");
            double denomValue = Math.pow(2, combo(op, register));
            int res = (int) (numerator / denomValue);
            register.put("B", res);
        }

        if (instr == 7) {
            var numerator = register.get("A");
            double denomValue = Math.pow(2, combo(op, register));
            int res = (int) (numerator / denomValue);
            register.put("C", res);
        }


        return currentPointer + 2;

    }


    private static int combo(Integer op, Map<String, Integer> register) {
        if (op <= 3) {
            return op;
        }
        if (op == 4) {
            return register.get("A");
        }
        if (op == 5) {
            return register.get("B");
        }
        if (op == 6) {
            return register.get("C");
        }
        throw new RuntimeException("error");
    }


    public static void task2() {

        List<Integer> suffix = List.of(4, 3, 1, 6, 5, 5, 3, 0);
        // this is a result captured  for suffix  [4,3,1,6,5,5,3,0]
        BigInteger aCursor = new BigInteger("14772387");

        //then each follow-up result with such suffix starts at index = index * 8 ( there are 8 digits remaining: 16 - 8)
        for (int i = 0; i < 8; i++) {
            aCursor = aCursor.multiply(BigInteger.valueOf(8));
        }

        while (true) {
            BigInteger a = aCursor;
            BigInteger b = BigInteger.ZERO;
            BigInteger c = BigInteger.ZERO;
            List<Integer> values = new ArrayList<>();
            List<Integer> expected = List.of(2, 4, 1, 1, 7, 5, 0, 3, 4, 3, 1, 6, 5, 5, 3, 0);
//            List<Integer> expected = List.of(7,0,0,0,3,7,3);
            while (!a.equals(BigInteger.ZERO)) {
                b = a.mod(BigInteger.valueOf(8l));
                b = b.xor(BigInteger.valueOf(1l));
                c = a.divide(BigInteger.valueOf(2).pow(b.intValue()));
                a = a.divide(BigInteger.valueOf(8l));
                b = b.xor(c);
                b = b.xor(BigInteger.valueOf(6l));
//                System.out.print(b.mod(BigInteger.valueOf(8l)) + ",");
                values.add(b.mod(BigInteger.valueOf(8l)).intValue());
                if (values.size() > expected.size() || !values.getLast().equals(expected.get(values.size() - 1))) {
                    break;
                }
            }
//            if (values.size() >= 8 && values.subList(values.size() - 8, values.size()).equals(suffix)) {
//                System.out.println(values);
//                System.out.println(aCursor);
//            }
            if (values.equals(expected)) {
                System.out.println(aCursor);
                return;
            }
            aCursor = aCursor.add(BigInteger.ONE);
        }
    }


}
