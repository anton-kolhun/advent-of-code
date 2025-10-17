package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.List;

public class Day18 {

    public static void main(String[] args) {
        task1();
        task2();

    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day18.txt");
        List<Item> items = new ArrayList<>();
        for (String line : lines) {
            Item head = new Item();
            Item cursor = head;
            char[] charArray = line.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c == '[') {
                    Item next = new Item();
                    next.parent = cursor;
                    if (cursor.left == null) {
                        cursor.left = next;
                        cursor = next;
                    } else {
                        cursor.right = next;
                        cursor = next;
                    }
                } else if (c == ']') {
                    cursor = cursor.parent;
                } else if (Character.isDigit(c)) {
                    cursor.value = Character.getNumericValue(c);
                } else if (c == ',') {
                    Item next = new Item();
                    next.parent = cursor.parent;
                    cursor.parent.right = next;
                    cursor = next;
                }
            }
            items.add(head);
        }
        Item total = items.getFirst();
        reduce(total);
        for (int i = 1; i < items.size(); i++) {
            Item item = items.get(i);
            total = new Item(total, item);
            reduce(total);
        }
        System.out.println("task1: " + score(total));
    }


    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day18.txt");
        List<Item> items = new ArrayList<>();
        for (String line : lines) {
            Item head = new Item();
            Item cursor = head;
            char[] charArray = line.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = charArray[i];
                if (c == '[') {
                    Item next = new Item();
                    next.parent = cursor;
                    if (cursor.left == null) {
                        cursor.left = next;
                        cursor = next;
                    } else {
                        cursor.right = next;
                        cursor = next;
                    }
                } else if (c == ']') {
                    cursor = cursor.parent;
                } else if (Character.isDigit(c)) {
                    cursor.value = Character.getNumericValue(c);
                } else if (c == ',') {
                    Item next = new Item();
                    next.parent = cursor.parent;
                    cursor.parent.right = next;
                    cursor = next;
                }
            }
            items.add(head);
        }

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                List<Item> copies = items.stream().map(Day18::copy).toList();
                if (i != j) {
                    var item = new Item(copies.get(i), copies.get(j));
                    reduce(item);
                    int score = score(item);
                    if (max < score) {
                        max = score;
                    }
                }

            }
        }
        System.out.println("task2: " + max);
    }

    private static Item copy(Item item) {
        if (item == null) {
            return null;
        }
        var left = copy(item.left);
        var right = copy(item.right);
        Item copy = new Item(left, right);
        copy.value = item.value;
        return copy;

    }

    private static int score(Item elem) {
        if (elem.value != null) {
            return elem.value;
        }
        return 3 * score(elem.left) + 2 * score(elem.right);
    }

    private static void reduce(Item elem) {
        var toExplode = findExplode(elem, 5);
        if (toExplode != null) {
            explode(toExplode);
//            System.out.println("explode: " + elem);
            reduce(elem);
        }
        var toSplit = findSplit(elem);
        if (toSplit != null) {
            split(toSplit);
//            System.out.println("split: " + elem);
            reduce(elem);
        }
    }

    private static void split(Item toSplit) {
        var parent = toSplit.parent;
        Item splitItemLeft = new Item(toSplit.value / 2);
        Item splitItemRight = new Item(toSplit.value - (toSplit.value / 2));
        var newItem = new Item(splitItemLeft, splitItemRight);
        newItem.parent = parent;
        newItem.left = splitItemLeft;
        newItem.right = splitItemRight;
        if (parent.left == toSplit) {
            parent.left = newItem;
        } else {
            parent.right = newItem;
        }

    }

    private static Item findSplit(Item elem) {
        if (elem.left != null) {
            var res = findSplit(elem.left);
            if (res != null) {
                return res;
            }
        }
        if (elem.right != null) {
            return findSplit(elem.right);
        }
        if (elem.value >= 10) {
            return elem;
        }
        return null;
    }

    private static void explode(Item toExplode) {
        addLeft(toExplode.left);
        addRight(toExplode.right);
        toExplode.left = null;
        toExplode.right = null;
        toExplode.value = 0;
    }

    private static void addLeft(Item addToLeft) {
        var cursor = addToLeft;
        while (cursor.parent != null && cursor.parent.right != cursor) {
            cursor = cursor.parent;
        }
        if (cursor.parent == null) {
            return;
        }
        var mostLeft = cursor.parent.left;
        while (mostLeft.right != null) {
            mostLeft = mostLeft.right;
        }
        mostLeft.value = mostLeft.value + addToLeft.value;
    }

    private static void addRight(Item addToRight) {
        var cursor = addToRight;
        while (cursor.parent != null && cursor.parent.left != cursor) {
            cursor = cursor.parent;
        }
        if (cursor.parent == null) {
            return;
        }
        var mostLeft = cursor.parent.right;
        while (mostLeft.left != null) {
            mostLeft = mostLeft.left;
        }
        mostLeft.value = mostLeft.value + addToRight.value;
    }

    private static Item findExplode(Item elem, int depth) {
        if (depth == 0) {
            return elem.parent;
        }

        if (elem.left != null) {
            var res = findExplode(elem.left, depth - 1);
            if (res != null) {
                return res;
            }
        }
        if (elem.right != null) {
            return findExplode(elem.right, depth - 1);
        }
        return null;
    }

    private static class Item {
        Item parent;
        Item left;
        Item right;
        Integer value;

        public Item(Item left, Item right) {
            this.left = left;
            this.right = right;
            if (left != null) {
                left.parent = this;
            }
            if (right != null) {
                right.parent = this;
            }
        }

        public Item(Integer value) {
            this.value = value;
        }

        public Item() {
        }

        @Override
        public String toString() {
            if (value != null) {
                return value.toString();
            } else {
                return "[" + left.toString() + "," + right.toString() + "]";
            }
        }
    }
}
