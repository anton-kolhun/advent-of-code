package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day16 {

    public static void main(String[] args) {
        task1And2();

    }

    public static void task1And2() {
        List<Range> allRanges = new ArrayList<>();
        List<String> lines = FilesUtils.readFile("aoc_2020/day16.txt");
        int cursor;
        for (cursor = 0; cursor < lines.size(); cursor++) {
            String line = lines.get(cursor);
            if (line.isEmpty()) {
                break;
            }
            String rangesStr = ParseUtils.splitByDelimiter(line, ":").get(1);
            String label = ParseUtils.splitByDelimiter(line, ":").get(0);
            List<String> ranges = ParseUtils.splitByDelimiter(rangesStr, "or").stream().map(String::trim).toList();
            for (String range : ranges) {
                List<String> rangeValues = ParseUtils.splitByDelimiter(range, "-").stream().map(String::trim).toList();
                allRanges.add(new Range(Integer.parseInt(rangeValues.get(0)), Integer.parseInt(rangeValues.get(1)), label));
            }
        }
        List<Integer> myTicket = new ArrayList<>();
        cursor = cursor + 2;
        String line = lines.get(cursor);
        ParseUtils.splitByDelimiter(line, ",").forEach(value -> myTicket.add(Integer.parseInt(value)));
        cursor = cursor + 3;
        int otherTicketsLine = cursor;
        List<Integer> otherTickets = new ArrayList<>();
        for (; cursor < lines.size(); cursor++) {
            line = lines.get(cursor);
            ParseUtils.splitByDelimiter(line, ",").forEach(value -> otherTickets.add(Integer.parseInt(value)));
        }

        Set<Integer> invalidTickets = new HashSet<>();
        for (Integer otherTicket : otherTickets) {
            boolean valid = false;
            for (Range range : allRanges) {
                if (otherTicket >= range.start && otherTicket <= range.end) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                invalidTickets.add(otherTicket);
            }
        }
        int sum = invalidTickets.stream().mapToInt(Integer::intValue).sum();
        System.out.println("task1 = " + sum);


        List<List<Integer>> validTickets = new ArrayList<>();
        for (int i = otherTicketsLine; i < lines.size(); i++) {
            line = lines.get(i);
            List<Integer> xxx = ParseUtils.splitByDelimiter(line, ",")
                    .stream().map(Integer::parseInt).toList();

            boolean isValid = true;
            for (Integer integer : xxx) {
                if (invalidTickets.contains(integer)) {
                    isValid = false;
                }
            }
            if (isValid) {
                validTickets.add(xxx);
            }
        }

        Map<String, List<Integer>> total = null;
        for (List<Integer> validTicket : validTickets) {
            Map<String, List<Integer>> labelToIndex = new HashMap<>();
            for (int index = 0; index < validTicket.size(); index++) {
                Integer ticket = validTicket.get(index);
                for (Range range : allRanges) {
                    if (ticket >= range.start && ticket <= range.end) {
                        List<Integer> indexes = new ArrayList<>();
                        indexes.add(index);
                        labelToIndex.merge(range.label, indexes, (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        });
                    }
                }
            }
            Map<String, List<Integer>> result = createIntersection(total, labelToIndex);
            total = result;
        }

        //checkResult(allRanges, total, validTickets);

        Set<Description> result = findTheOnly(total, new HashSet<>());

        Map<Integer, String> indexToLabel = new HashMap<>();
        Map<String, Integer> labelToIndex = new HashMap<>();
        for (Description description : result) {
            indexToLabel.put(description.getIndex(), description.getLabel());
            labelToIndex.put(description.getLabel(), description.getIndex());
        }

        //checkResult2(allRanges, labelToIndex, validTickets);

        BigDecimal multiply = BigDecimal.ONE;
        for (int index = 0; index < myTicket.size(); index++) {
            if (indexToLabel.get(index).startsWith("departure")) {
                multiply = multiply.multiply(BigDecimal.valueOf(myTicket.get(index)));
            }
        }
        System.out.println("task2 = " + multiply);
    }

    private static void checkResult2(List<Range> allRanges, Map<String, Integer> labelToIndex, List<List<Integer>> validTickets) {
        Map<String, List<Range>> xxx = allRanges.stream().collect(Collectors.groupingBy(range -> range.label));

        for (Map.Entry<String, List<Range>> entry : xxx.entrySet()) {
            var label = entry.getKey();
            int index = labelToIndex.get(label);

            for (List<Integer> validTicket : validTickets) {
                int value = validTicket.get(index);
                boolean isGood = false;
                for (Range range : entry.getValue()) {
                    if (value >= range.start && value <= range.end) {
                        isGood = true;
                        break;
                    }
                }
                if (!isGood) {
                    System.out.println("er?rro");
                }
            }
        }
    }

    private static void checkResult(List<Range> allRanges, Map<String, List<Integer>> total, List<List<Integer>> validTickets) {
        Map<String, List<Range>> xxx = allRanges.stream().collect(Collectors.groupingBy(range -> range.label));

        for (Map.Entry<String, List<Range>> entry : xxx.entrySet()) {
            var label = entry.getKey();
            List<Integer> res = total.get(label);
            for (Integer re : res) {
                for (List<Integer> validTicket : validTickets) {
                    int value = validTicket.get(re);
                    boolean isGood = false;
                    for (Range range : entry.getValue()) {
                        if (value >= range.start && value <= range.end) {
                            isGood = true;
                            break;
                        }
                    }
                    if (!isGood) {
                        System.out.println("error?");
                    }
                }
            }
        }
    }

    private static Set<Description> findTheOnly(Map<String, List<Integer>> total, Set<Description> taken) {

        Iterator<Map.Entry<String, List<Integer>>> iterator = total.entrySet().iterator();
        if (!iterator.hasNext()) {
            return taken;
        }

        Map.Entry<String, List<Integer>> entry = iterator.next();
        Map<String, List<Integer>> nextTotal = new HashMap<>(total);
        nextTotal.remove(entry.getKey());
        for (Integer value : entry.getValue()) {
            Set<Description> nextTaken = new HashSet<>(taken);
            if (nextTaken.add(new Description(entry.getKey(), value))) {
                Set<Description> res = findTheOnly(nextTotal, nextTaken);
                if (!res.isEmpty()) {
                    return res;
                }
            }
        }
        return Collections.emptySet();
    }

    private static Map<String, List<Integer>> createIntersection(Map<String, List<Integer>> total,
                                                                 Map<String, List<Integer>> current) {
        if (total == null) {
            return current;
        }
        Map<String, List<Integer>> result = new HashMap<>();
        total.forEach((label, indexes) -> {
            if (current.containsKey(label)) {
                List<Integer> currentData = current.get(label);
                currentData.retainAll(indexes);
                result.put(label, currentData);
            }
        });
        return result;
    }

    @AllArgsConstructor
    @Data
    static class Range {
        int start;
        int end;
        String label;

        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = "label")
    static class Description {
        String label;
        int index;
    }


}
