package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day22 {


    public static void main(String[] args) throws Exception {
        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day22.txt");
        List<Element> elements = new ArrayList<>();
        Element head = new Element();
        Element cursor = head;
        Element previous = null;
        for (String line : lines) {
            if (line.isEmpty()) {
                cursor = previous;
                cursor.next = null;
                elements.add(head);
                head = new Element();
                cursor = head;
                continue;
            }
            if (line.contains("Player")) {
                continue;
            }
            cursor.value = Integer.parseInt(line);
            var nextEl = new Element();
            previous = cursor;
            cursor.next = nextEl;
            cursor = nextEl;
        }
        cursor = previous;
        cursor.next = null;
        elements.add(head);

        Element player1 = elements.get(0);
        Element player2 = elements.get(1);

        while (player1 != null && player2 != null) {
            int card1 = player1.value;
            int card2 = player2.value;
            Element next1 = player1.next;
            player1 = next1;
            Element next2 = player2.next;
            player2 = next2;
            if (card1 > card2) {
                player1 = addCards(player1, card1, card2);
            } else {
                player2 = addCards(player2, card2, card1);
            }
        }
        Element winner = (player1 != null) ? player1 : player2;
        CardInfo res = calculateScore(winner);
        System.out.println("task1: " + res.result);
    }


    private static CardInfo calculateScore(Element cursor) {
        if (cursor.next == null) {
            return new CardInfo(cursor.value, 1);
        }
        CardInfo info = calculateScore(cursor.next);
        long result = (info.depth + 1) * cursor.value + info.result;
        return new CardInfo(result, info.depth + 1);
    }

    private static Element addCards(Element player, int card1, int card2) {
        if (player == null) {
            player = new Element(card1);
            player.next = new Element(card2);
            return player;
        }

        Element head = player;
        while (player.next != null) {
            player = player.next;
        }
        player.next = new Element(card1);
        player = player.next;
        player.next = new Element(card2);
        return head;
    }

    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day22.txt");
        List<Element> elements = new ArrayList<>();
        Element head = new Element();
        Element cursor = head;
        Element previous = null;
        for (String line : lines) {
            if (line.isEmpty()) {
                cursor = previous;
                cursor.next = null;
                elements.add(head);
                head = new Element();
                cursor = head;
                continue;
            }
            if (line.contains("Player")) {
                continue;
            }
            cursor.value = Integer.parseInt(line);
            var nextEl = new Element();
            previous = cursor;
            cursor.next = nextEl;
            cursor = nextEl;
        }
        cursor = previous;
        cursor.next = null;
        elements.add(head);

        Element player1 = elements.get(0);
        Element player2 = elements.get(1);

        WinnerInfo winner = playRec(player1, player2, 1);
        CardInfo res = calculateScore(winner.player);
        System.out.println("task2: " + res.result);
    }

    private static WinnerInfo playRec(Element player1, Element player2, int recLevel) {
        Set<List<Integer>> p1Decks = new HashSet<>();
        Set<List<Integer>> p2Decks = new HashSet<>();
        while (player1 != null && player2 != null) {
/*            System.out.println();
            System.out.println("game: " + recLevel);
            print(player1, "player1");
            print(player2, "player2");*/
            var deck1 = getCards(player1);
            var deck2 = getCards(player2);
            if (!p1Decks.add(deck1) || !p2Decks.add(deck2)) {
                return new WinnerInfo(1, player1);
            }
            int card1 = player1.value;
            int card2 = player2.value;
            player1 = player1.next;
            player2 = player2.next;
            int remainingCards1 = deck1.size() - 1;
            int remainingCard2 = deck2.size() - 1;
            if (remainingCards1 < card1 || remainingCard2 < card2) {
                if (card1 > card2) {
                    player1 = addCards(player1, card1, card2);
                } else {
                    player2 = addCards(player2, card2, card1);
                }
            } else {
                Element p1Copy = copyDeck(player1, card1);
                Element p2Copy = copyDeck(player2, card2);
                var winner = playRec(p1Copy, p2Copy, recLevel + 1);
                if (winner.id == 1) {
                    addCards(player1, card1, card2);
                } else {
                    addCards(player2, card2, card1);
                }
            }
        }
        if (player1 != null) {
            return new WinnerInfo(1, player1);
        }
        return new WinnerInfo(2, player2);
    }

    private static void print(Element player, String header) {
        System.out.print(header + ": ");
        List<Integer> cards = getCards(player);
        for (Integer card : cards) {
            System.out.print(card + ",");
        }
        System.out.println();
    }

    private static Element copyDeck(Element player, int size) {
        Element head = new Element();
        var cursor = head;
        cursor.value = player.value;
        for (int i = 0; i < size - 1; i++) {
            player = player.next;
            var nextEl = new Element();
            cursor.next = nextEl;
            cursor = cursor.next;
            cursor.value = player.value;
        }
        return head;
    }

    private static List<Integer> getCards(Element next) {
        List<Integer> cards = new ArrayList<>();
        Element cursor = next;
        while (cursor != null) {
            cards.add(cursor.value);
            cursor = cursor.next;
        }
        return cards;
    }

    static class WinnerInfo {
        int id;
        Element player;

        public WinnerInfo(int id, Element player) {
            this.id = id;
            this.player = player;
        }
    }

    private static class Element {
        int value;
        Element next;

        public Element() {
        }

        public Element(int value) {
            this.value = value;
        }
    }

    private static class CardInfo {
        int depth;
        long result;

        public CardInfo(long result, int depth) {
            this.depth = depth;
            this.result = result;
        }
    }

}
