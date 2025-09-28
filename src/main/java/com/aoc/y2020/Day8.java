package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day8 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day8.txt");
        List<Instruction> instructions = new ArrayList<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " ");
            Instruction instr = new Instruction(parts.get(0), Integer.parseInt(parts.get(1)));
            instructions.add(instr);
        }
        int cursor = 0;
        int accum = 0;
        Set<Instruction> visited = new HashSet<>();
        while (true) {
            Instruction nextInstr = instructions.get(cursor);
            if (!visited.add(nextInstr)) {
                break;
            }
            if (nextInstr.type.equals("nop")) {
                cursor = cursor + 1;
            } else if (nextInstr.type.equals("acc")) {
                accum = accum + nextInstr.value;
                cursor = cursor + 1;
            } else {
                cursor = cursor + nextInstr.value;
            }
        }
        System.out.println("task1 = " + accum);
    }

    static class Instruction {
        String type;
        int value;

        public Instruction(String type, int value) {
            this.type = type;
            this.value = value;
        }
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day8.txt");
        List<Instruction> instructions = new ArrayList<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " ");
            Instruction instr = new Instruction(parts.get(0), Integer.parseInt(parts.get(1)));
            instructions.add(instr);
        }

        int accum = -1;
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction.type.equals("jmp")) {
                List<Instruction> copy = new ArrayList<>(instructions);
                copy.set(i, new Instruction("nop", instruction.value));
                accum = calculateAccum(copy);
                if (accum > 0) {
                    break;
                }
            } else if (instruction.type.equals("nop")) {
                List<Instruction> copy = new ArrayList<>(instructions);
                copy.set(i, new Instruction("jmp", instruction.value));
                accum = calculateAccum(copy);
                if (accum > 0) {
                    break;
                }
            }
        }
        System.out.println("task2 = " + accum);
    }

    private static int calculateAccum(List<Instruction> instructions) {
        int cursor = 0;
        int accum = 0;
        Set<Instruction> visited = new HashSet<>();
        boolean isGood = true;
        while (true) {
            Instruction nextInstr = instructions.get(cursor);
            if (!visited.add(nextInstr)) {
                isGood = false;
                break;
            }
            if (nextInstr.type.equals("nop")) {
                cursor = cursor + 1;
            } else if (nextInstr.type.equals("acc")) {
                accum = accum + nextInstr.value;
                cursor = cursor + 1;
            } else {
                cursor = cursor + nextInstr.value;
            }
            if (cursor == instructions.size()) {
                break;
            }
        }
        if (isGood) {
            return accum;
        }
        return -1;
    }
}
