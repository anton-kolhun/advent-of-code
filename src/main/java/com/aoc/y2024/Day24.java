package com.aoc.y2024;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Day24 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1And2();
    }

    public static void task1And2() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day24_with_fix.txt");
        Map<String, String> wiresToValue = new HashMap<>();
        int row = 0;
        for (row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            if (line.isEmpty()) {
                row++;
                break;
            }
            List<String> parts = ParseUtils.splitByDelimiter(line, ": ");
            wiresToValue.put(parts.get(0), parts.get(1));
        }


        List<Formula> formulas = new ArrayList<>();
        for (int r = row; r < lines.size(); r++) {
            String line = lines.get(r);
            List<String> parts = ParseUtils.splitByDelimiter(line, " -> ");
            String output = parts.get(1);
            List<String> formulaParts = ParseUtils.splitByDelimiter(line, " ");
            String op1 = formulaParts.get(0);
            String operator = formulaParts.get(1);
            String op2 = formulaParts.get(2);
            formulas.add(new Formula(op1, op2, operator, output));
        }

        List<Formula> remaining = new ArrayList<>(formulas);
        while (!remaining.isEmpty()) {
            List<Formula> nextRem = new ArrayList<>();
            for (Formula formula : formulas) {
                String val1 = wiresToValue.get(formula.getOperand1());
                if (val1 == null) {
                    nextRem.add(formula);
                    continue;
                }
                String val2 = wiresToValue.get(formula.getOperand2());
                if (val2 == null) {
                    nextRem.add(formula);
                    continue;
                }
                String value = handleFormula(val1, val2, formula.getOperator());
                wiresToValue.put(formula.output, value);
            }
            remaining = nextRem;
        }
        Map<Integer, String> zValues = new HashMap<>();
        for (Map.Entry<String, String> entry : wiresToValue.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (k.startsWith("z")) {
                Integer seqNum = getSeqNumber(k);
                zValues.put(seqNum, v);
            }
        }

        int cursor = 0;
        StringBuffer zRes = new StringBuffer();
        while (true) {
            String res = zValues.get(cursor);
            if (res == null) {
                break;
            }
            zRes.append(res);
            cursor++;
        }
        zRes = zRes.reverse();

        System.out.println(Long.parseLong(zRes.toString(), 2));

        //part2 start
        Map<String, Tree> elToTree = buildTree(formulas);
        printTree(elToTree);
        //analyze the tree. it has the convention as follows:
//        Xi Yi  <el>>.OR <el>>.XOR
//        <el>>.AND <el>>.AND Xi Yi
//        find one by one mismatches from top-down. they go as follows:
//        1) kgj -> z26
//        2) vvw -> chv
//        3) jpj -> z12
//        4) rts -> z07


        String xRes = getValuesByChar(wiresToValue, "x");
        String yRes = getValuesByChar(wiresToValue, "y");

        boolean isValid = Long.parseLong(xRes, 2) + Long.parseLong(yRes, 2) == Long.parseLong(zRes.toString(), 2);
        System.out.println(isValid);

        var result = new ArrayList<>(List.of("kgj", "z26", "vvw", "chv", "jpj", "z12", "rts", "z07"));
        result.sort(Comparator.naturalOrder());
        var str = result.stream().reduce((s1, s2) -> s1 + "," + s2).get();
        System.out.println(str);

    }

    private static Integer getSeqNumber(String k) {
        k = k.substring(1);
        StringBuilder value = new StringBuilder();
        boolean digitFound = false;
        for (char c : k.toCharArray()) {
            if (c == '0' && !digitFound) {
                continue;
            }
            value.append(c);
            digitFound = true;
        }
        if (value.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value.toString());
    }

    private static String handleFormula(String val1, String val2, String operator) {
        var v1 = Long.parseLong(val1, 2);
        var v2 = Long.parseLong(val2, 2);
        long output;
        if (operator.equals("AND")) {
            output = v1 & v2;
        } else if (operator.equals("OR")) {
            output = v1 | v2;
        } else {
            output = v1 ^ v2;
        }

        return String.valueOf(output);

    }


    private static String getValuesByChar(Map<String, String> wiresToValue, String type) {
        Map<Integer, String> zValues = new HashMap<>();
        for (Map.Entry<String, String> entry : wiresToValue.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (k.startsWith(type)) {
                Integer seqNum = getSeqNumber(k);
                zValues.put(seqNum, v);
            }
        }

        int cursor = 0;
        StringBuffer total = new StringBuffer();
        while (true) {
            String res = zValues.get(cursor);
            if (res == null) {
                break;
            }
            total.append(res);
            cursor++;
        }
        total = total.reverse();
//        System.out.println(total);
        return total.toString();
    }

    @AllArgsConstructor
    @Data
    static class Formula {
        String operand1;
        String operand2;
        String operator;
        String output;
    }


    private static void printTree(Map<String, Tree> elToTree) {
        Tree head = elToTree.get("z45");
        List<Tree> toScan = new ArrayList<>();
        toScan.add(head);
        while (!toScan.isEmpty()) {
            var nextToScan = new ArrayList<Tree>();
            for (Tree tree : toScan) {
                if (tree.left != null) {
                    nextToScan.add(tree.left);
                }
                if (tree.right != null) {
                    nextToScan.add(tree.right);
                }
                System.out.print(tree.val);
                if (tree.operator != null) {
                    System.out.print("." + tree.operator);
                }
                System.out.print(" ");
            }
            System.out.println();
            toScan = nextToScan;
        }
    }

    private static Map<String, Tree> buildTree(List<Formula> formulas) {

        Map<String, Tree> elementToLoc = new HashMap<>();
        for (Formula formula : formulas) {

            Tree tree;
            if (elementToLoc.containsKey(formula.output)) {
                tree = elementToLoc.get(formula.output);
            } else {
                tree = new Tree(formula.output);
                elementToLoc.put(formula.output, tree);
            }
            tree.operator = formula.operator;

            Tree left;
            if (elementToLoc.containsKey(formula.operand1)) {
                left = elementToLoc.get(formula.operand1);
            } else {
                left = new Tree(formula.operand1);
                elementToLoc.put(left.val, left);

            }
            tree.left = left;

            Tree right;
            if (elementToLoc.containsKey(formula.operand2)) {
                right = elementToLoc.get(formula.operand2);
            } else {
                right = new Tree(formula.operand2);
                elementToLoc.put(right.val, right);
            }
            tree.right = right;
        }
        return elementToLoc;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class Tree {
        Tree left;
        Tree right;
        String operator;
        String val;


        public Tree(String val) {
            this.val = val;
        }
    }
}
