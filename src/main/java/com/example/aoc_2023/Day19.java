package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("day19.txt");
        Map<String, Workflow> workflows = new HashMap<>();
        int row = 0;
        for (; row < lines.size(); row++) {
            String line = lines.get(row);
            if (line.isEmpty()) {
                break;
            }
            var startInd = line.indexOf("{");
            List<String> parts = List.of(line.substring(0, startInd), line.substring(startInd + 1, line.length() - 1));
            var name = parts.get(0);
            var workflow = parts.get(1);
            List<String> conds = ParseUtils.splitByDelimiter(workflow, ",");
            Workflow flow = new Workflow();
            for (String cond : conds) {
                List<String> condition = ParseUtils.splitByDelimiter(cond, ":");
                if (condition.size() == 1) {
                    Condition con = new Condition();
                    con.nextWorkflowIfMatches = condition.get(0);
                    flow.conditions.add(con);
                } else {
                    var leftPart = condition.get(0);
                    var varName = leftPart.substring(0, 1);
                    var operator = leftPart.substring(1, 2);
                    var value = leftPart.substring(2);
                    Condition con = new Condition(varName, operator, value, condition.get(1));
                    flow.conditions.add(con);
                }
            }
            workflows.put(name, flow);

        }
        row++;
        List<Parts> parts = new ArrayList<>();
        for (; row < lines.size(); row++) {
            String line = lines.get(row);
            var partsExpr = line.substring(1, line.length() - 1);
            List<String> partsStr = ParseUtils.splitByDelimiter(partsExpr, ",");
            Parts pts = new Parts();
            for (String s : partsStr) {
                Part p = new Part(s.substring(0, 1), Integer.parseInt(s.substring(2)));
                pts.parts.put(s.substring(0, 1), p);
            }
            parts.add(pts);
        }
        System.out.println(parts);


        int total = 0;
        for (Parts part : parts) {
            var cursor = workflows.get("in");
            String nextFlow = "";
            while (!nextFlow.equals("A") && !nextFlow.equals("R")) {
                for (Condition condition : cursor.conditions) {
                    if (condition.varName != null) {
                        Part part1 = part.parts.get(condition.varName);
                        var expectedValue = condition.value;
                        var actualValue = part1.value;
                        if (condition.operator.equals(">")) {
                            if (actualValue > Integer.parseInt(expectedValue)) {
                                nextFlow = condition.nextWorkflowIfMatches;
                                cursor = workflows.get(nextFlow);
                                break;
                            }
                        } else {
                            if (actualValue < Integer.parseInt(expectedValue)) {
                                nextFlow = condition.nextWorkflowIfMatches;
                                cursor = workflows.get(nextFlow);
                                break;
                            }
                        }
                        {

                        }

                    } else {
                        nextFlow = condition.nextWorkflowIfMatches;
                        cursor = workflows.get(condition.nextWorkflowIfMatches);
                        break;

                    }
                }
            }

            if (nextFlow.equals("A")) {
                total  = total + part.parts.get("x").value + part.parts.get("m").value + part.parts.get("a").value +
                        part.parts.get("s").value;
            }

        }

        System.out.println(total);


    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Workflow {
        List<Condition> conditions = new ArrayList<>();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Condition {
        String varName;
        String operator;
        String value;
        String nextWorkflowIfMatches;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private static class Part {
        String varName;
        Integer value;
    }

    private static class Parts {
        Map<String, Part> parts = new HashMap<>();
    }
}
