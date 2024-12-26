package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day15Part2 {

    public static void main(String[] args) {
        task2();
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day15.txt");
        System.out.println(lines);
        List<String> items = ParseUtils.splitByDelimiter(lines.get(0), ",");
        long sum = 0;
        Map<Integer, BoxInfo> boxes = new HashMap<>();
        for (String item : items) {
            process(item, boxes);
        }

        long total = 0;
        for (Map.Entry<Integer, BoxInfo> entry : boxes.entrySet()) {
            int boxIndex = entry.getKey() + 1;
            int boxScore = 0;
            List<BoxLabel> labels = entry.getValue().labels;
            for (int labelIndex = 0; labelIndex < labels.size(); labelIndex++) {
                BoxLabel label = labels.get(labelIndex);
                int labelScore = boxIndex * (labelIndex + 1) * Integer.parseInt(label.score);
                boxScore += labelScore;
            }
            total += boxScore;
        }
        System.out.println(total);


    }

    private static void process(String str, Map<Integer, BoxInfo> boxes) {
        if (str.contains("=")) {
            List<String> pair = ParseUtils.splitByDelimiter(str, "=");
            int index = applyHash(pair.get(0));
            BoxInfo box = boxes.get(index);
            if (box == null) {
                box = new BoxInfo(0, new ArrayList());
                boxes.put(index, box);
            }
            List<BoxLabel> labels = box.labels;
            var boxLabelToAdd = new BoxLabel(pair.get(0), pair.get(1));
            int i = 0;
            boolean exists = false;
            for (i = 0; i < labels.size(); i++) {
                BoxLabel label = labels.get(i);
                if (label.equals(boxLabelToAdd)) {
                    exists = true;
                    label.score = boxLabelToAdd.score;
                    break;
                }
            }
            if (!exists) {
                box.labels.add(new BoxLabel(pair.get(0), pair.get(1)));
            }
        } else {
            List<String> pair = ParseUtils.splitByDelimiter(str, "-");
            int index = applyHash(pair.get(0));
            BoxInfo box = boxes.get(index);
            if (box == null) {
                box = new BoxInfo(0, new ArrayList());
                boxes.put(index, box);
            }
            BoxLabel boxLabel = new BoxLabel(pair.get(0));
            box.labels.remove(boxLabel);
        }

    }

    private static int applyHash(String str) {
        int current = 0;
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            int val = (int) c;
            current = current + val;
            current = current * 17;
            current = current % 256;
        }
        return current;
    }

    @AllArgsConstructor
    private static class BoxInfo {
        private int index;
        private List<BoxLabel> labels = new ArrayList<>();
    }

    @AllArgsConstructor
    private static class BoxLabel {
        private String value;
        private String score;

        public BoxLabel(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BoxLabel boxLabel = (BoxLabel) o;

            return Objects.equals(value, boxLabel.value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

}
