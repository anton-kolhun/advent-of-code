package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import com.example.aoc_2023.helper.RangeUtils.Range;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19Part2 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
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


        Range rangeX = new Range(1, 4000);
        Range rangeM = new Range(1, 4000);
        Range rangeA = new Range(1, 4000);
        Range rangeS = new Range(1, 4000);

        var ranges = Map.of("x", rangeX, "m", rangeM, "a", rangeA, "s", rangeS);

        List<WorkflowInfo> flows = new ArrayList<>();
        flows.add(new WorkflowInfo("in", ranges, List.of("in")));
        List<WorkflowInfo> totals = new ArrayList<>();
        while (!flows.isEmpty()) {
            List<WorkflowInfo> nextFlows = new ArrayList<>();
            for (WorkflowInfo flow : flows) {
                if (flow.flowName.equals("A")) {
                    totals.add(flow);
                    continue;
                } else if (flow.flowName.equals("R")) {
                    continue;
                }
                var cursor = workflows.get(flow.flowName);
                List<Condition> conditions = cursor.conditions;
                for (int i = 0; i < conditions.size(); i++) {
                    Map<String, Range> conditionRanges = new HashMap<>(flow.rangeInfo);
                    Condition condition = conditions.get(i);
                    if (!condition.varName.isEmpty()) {
                        var expectedValue = condition.value;
                        var range = conditionRanges.get(condition.varName);
                        if (!subtractRanges(i, conditions, conditionRanges)) {
                            break;
                        }
                        if (condition.operator.equals(">")) {
                            long start = Math.max(Integer.parseInt(expectedValue) + 1, range.start);
                            if (start <= range.end) {
                                var r = new Range(start, range.end);
                                merge(r, conditionRanges, condition.varName);
                                var nextPath = new ArrayList<>(flow.path);
                                nextPath.add(condition.nextWorkflowIfMatches);
                                WorkflowInfo next = new WorkflowInfo(condition.nextWorkflowIfMatches, conditionRanges, nextPath);
                                nextFlows.add(next);
                            }
                        } else {
                            long end = Math.min(Integer.parseInt(expectedValue) - 1, range.end);
                            if (range.start <= end) {
                                var r = new Range(range.start, end);
                                merge(r, conditionRanges, condition.varName);
                                var nextPath = new ArrayList<>(flow.path);
                                nextPath.add(condition.nextWorkflowIfMatches);
                                WorkflowInfo next = new WorkflowInfo(condition.nextWorkflowIfMatches, conditionRanges, nextPath);
                                nextFlows.add(next);
                            }
                        }
                    } else {
                        if (subtractRanges(i, conditions, conditionRanges)) {
                            var nextPath = new ArrayList<>(flow.path);
                            nextPath.add(condition.nextWorkflowIfMatches);
                            WorkflowInfo next = new WorkflowInfo(condition.nextWorkflowIfMatches, conditionRanges, nextPath);
                            nextFlows.add(next);
                        }
                    }
                }
            }
            flows = nextFlows;
        }

        BigInteger res = BigInteger.ZERO;
        for (WorkflowInfo flowInfo : totals) {
            var rangeCollection = flowInfo.rangeInfo;
            Range rx = rangeCollection.get("x");
            Range rm = rangeCollection.get("m");
            Range ra = rangeCollection.get("a");
            Range rs = rangeCollection.get("s");
            //BigInteger overlap = getOverlap(rangeCollection, allocated);
            BigInteger vol = BigInteger.valueOf(rx.end - rx.start + 1)
                    .multiply(BigInteger.valueOf(rm.end - rm.start + 1))
                    .multiply(BigInteger.valueOf(ra.end - ra.start + 1))
                    .multiply(BigInteger.valueOf(rs.end - rs.start + 1));
            res = res.add(vol);
        }

        for (int i = 0; i < totals.size() - 1; i++) {
            var flow1 = totals.get(i);
            for (int j = i + 1; j < totals.size(); j++) {
                var flow2 = totals.get(j);
                var x = isOverlap(flow1.rangeInfo.get("x"), flow2.rangeInfo.get("x"));
                var m = isOverlap(flow1.rangeInfo.get("m"), flow2.rangeInfo.get("m"));
                var a = isOverlap(flow1.rangeInfo.get("a"), flow2.rangeInfo.get("a"));
                var s = isOverlap(flow1.rangeInfo.get("s"), flow2.rangeInfo.get("s"));
                if (x && m && a && s) {
                    System.out.println("overlap");
                }

            }
        }

        System.out.println(res);


    }

    private static void merge(Range r, Map<String, Range> conditionRanges, String varName) {
        var other = conditionRanges.get(varName);
        var start = Math.max(r.start, other.start);
        var end = Math.min(r.end, other.end);
        conditionRanges.put(varName, new Range(start, end));
    }

    private static boolean isOverlap(Range r1, Range r2) {
        var start = Math.max(r1.start, r2.start);
        var end = Math.min(r1.end, r2.end);
        return (start <= end);
    }

    private static boolean subtractRanges(int index, List<Condition> conditions, Map<String, Range> ranges) {
        for (int i = 0; i < index; i++) {

            var prevCond = conditions.get(i);
            var range = ranges.get(prevCond.varName);
            var expectedValue = prevCond.value;
            if (prevCond.operator.equals("<")) {
                long start = Math.max(Integer.parseInt(expectedValue), range.start);
                if (start <= range.end) {
                    var r = new Range(start, range.end);
                    ranges.put(prevCond.varName, r);
                } else {
                    return false;
                }
            } else {
                long end = Math.min(Integer.parseInt(expectedValue), range.end);
                if (end >= range.start) {
                    var r = new Range(range.start, end);
                    ranges.put(prevCond.varName, r);
                } else {
                    return false;
                }
            }
        }
        return true;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    private static class Workflow {
        List<Condition> conditions = new ArrayList<>();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Condition {
        String varName = "";
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

    @AllArgsConstructor
    private static class WorkflowInfo {
        String flowName;
        Map<String, Range> rangeInfo;
        List<String> path = new ArrayList<>();
    }


}
