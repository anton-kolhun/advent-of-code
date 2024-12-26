package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day23 {

    public static void main(String[] args) throws Exception {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day23.txt");
        List<Integer> values = new ArrayList<>();
        Map<Integer, Element> valueToElement = new HashMap<>();
        for (char c : lines.get(0).toCharArray()) {
            values.add(Integer.parseInt(String.valueOf(c)));
        }
        Element head = new Element(values.get(0), null);
        valueToElement.put(values.get(0), head);
        Element previous = head;
        for (int i = 1; i < values.size(); i++) {
            Integer value = values.get(i);
            Element current = new Element(value, null);
            previous.next = current;
            previous = current;
            valueToElement.put(value, current);
        }
        previous.next = head;
        Element cursor = head;
        for (int move = 0; move < 100; move++) {
            Element removed3 = cursor.next;
            cursor.next = cursor.next.next.next.next;
            Element nextMoveCursor = cursor.next;
            int destination = findDestination(cursor, removed3, 9);
            cursor = valueToElement.get(destination);
            Element tail = cursor.next;
            cursor.next = removed3;
            for (int i = 0; i < 2; i++) {
                removed3 = removed3.next;
            }
            removed3.next = tail;

            cursor = nextMoveCursor;
        }
        while (cursor.value != 1) {
            cursor = cursor.next;
        }
        cursor = cursor.next;

        for (int i = 0; i < 8; i++) {
            System.out.print(cursor.value);
            cursor = cursor.next;
        }
        System.out.println();
    }

    private static int findDestination(Element cursor, Element removed3, int max) {
        Set<Integer> removedValues = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            removedValues.add(removed3.value);
            removed3 = removed3.next;
        }
        int value = findValue(removedValues, cursor.value - 1, max);
        return value;
    }

    private static int findValue(Set<Integer> removedValues, int valueToFind, int max) {
        while (removedValues.contains(valueToFind)) {
            valueToFind = valueToFind - 1;
        }
        if (valueToFind < 1) {
            valueToFind = findValue(removedValues, max, max);
        }
        return valueToFind;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Element {
        int value;
        Element next;


        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public static void task2() throws Exception {
        List<String> lines = FilesUtils.readFile("aoc_2020/day23.txt");
        List<Integer> values = new ArrayList<>();
        Map<Integer, Element> valueToElement = new HashMap<>();
        for (char c : lines.get(0).toCharArray()) {
            values.add(Integer.parseInt(String.valueOf(c)));
        }

        int max = 9;
        for (int i = 0; i < 1000000 - 9; i++) {
            values.add(max + 1);
            max = max + 1;
        }

        Element head = new Element(values.get(0), null);
        valueToElement.put(values.get(0), head);
        Element previous = head;
        for (int i = 1; i < values.size(); i++) {
            Integer value = values.get(i);
            Element current = new Element(value, null);
            previous.next = current;
            previous = current;
            valueToElement.put(value, current);
        }
        previous.next = head;

        Element cursor = head;
        for (int move = 0; move < 10000000; move++) {
            Element removed3 = cursor.next;
            Element next = cursor.next.next.next.next;
            cursor.next = cursor.next.next.next.next;
            Element nextMoveCursor = cursor.next;
            int destination = findDestination(cursor, removed3, max);
//            while (cursor.value != destination) {
//                cursor = cursor.previous;
//            }
            cursor = valueToElement.get(destination);
            Element tail = cursor.next;
            cursor.next = removed3;
            for (int i = 0; i < 2; i++) {
                removed3 = removed3.next;
            }
            removed3.next = tail;

            cursor = nextMoveCursor;
        }

        while (cursor.value != 1) {
            cursor = cursor.next;
        }
        cursor = cursor.next;

        long multiply = 1;
        for (int i = 0; i < 2; i++) {
            multiply = multiply * cursor.value;
            cursor = cursor.next;
        }
        System.out.println(multiply);

    }

}
