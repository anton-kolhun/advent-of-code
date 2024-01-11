package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    public static void task1() {
        Map<Character, Integer> charToVal = new HashMap<>(Map.of('2', 2, '3', 3, '4', 4, '5', 5, '6', 6, '7', 7, '8', 8, '9', 9));
        charToVal.putAll(Map.of('T', 10, 'J', 11, 'Q', 12, 'K', 13, 'A', 14));
        List<String> lines = FilesUtils.readFile("day7.txt");
        List<HandT1> hands = new ArrayList<>();
        for (String line : lines) {
            List<String> data = ParseUtils.splitByDelimiter(line, " ");
            var hand = new HandT1(data.get(0), Integer.parseInt(data.get(1)));
            hands.add(hand);
        }

        hands.sort((hand1, hand2) -> {

            List<CardInfo> cards1 = hand1.cardInfos;
            List<CardInfo> cards2 = hand2.cardInfos;
            for (int i = 0; i < cards1.size(); i++) {
                CardInfo cardInfo1 = cards1.get(i);
                CardInfo cardInfo2 = cards2.get(i);
                if (cardInfo1.occurrence != cardInfo2.occurrence) {
                    return cardInfo1.occurrence - cardInfo2.occurrence;
                }
            }


            char[] charArray1 = hand1.cards.toCharArray();
            char[] charArray2 = hand2.cards.toCharArray();

            for (int i = 0; i < charArray1.length; i++) {
                char h1C = charArray1[i];
                char h2C = charArray2[i];
                if (!charToVal.get(h1C).equals(charToVal.get(h2C))) {
                    return charToVal.get(h1C) - charToVal.get(h2C);
                }
            }
            return 0;
        });
        int sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            HandT1 hand = hands.get(i);
            int score = hand.bid * (i + 1);
            sum += score;
        }
        System.out.println(sum);
    }


    public static void task2() {
        Map<Character, Integer> charToVal = new HashMap<>(Map.of('2', 2, '3', 3, '4', 4, '5', 5, '6', 6, '7', 7, '8', 8, '9', 9));
        charToVal.putAll(Map.of('T', 10, 'J', 1, 'Q', 12, 'K', 13, 'A', 14));
        List<String> lines = FilesUtils.readFile("day7.txt");
        List<HandT2> hands = new ArrayList<>();
        for (String line : lines) {
            List<String> data = ParseUtils.splitByDelimiter(line, " ");
            var hand = new HandT2(data.get(0), Integer.parseInt(data.get(1)));
            hands.add(hand);
        }

        hands.sort((hand1, hand2) -> {

            List<CardInfo> cards1 = hand1.cardInfos;
            List<CardInfo> card2 = hand2.cardInfos;
            for (int i = 0; i < cards1.size(); i++) {
                CardInfo cardInfo1 = cards1.get(i);
                CardInfo cardInfo2 = card2.get(i);
                if (cardInfo1.occurrence != cardInfo2.occurrence) {
                    return cardInfo1.occurrence - cardInfo2.occurrence;
                }
            }


            char[] charArray1 = hand1.cards.toCharArray();
            char[] charArray2 = hand2.cards.toCharArray();

            for (int i = 0; i < charArray1.length; i++) {
                char h1C = charArray1[i];
                char h2C = charArray2[i];
                if (!charToVal.get(h1C).equals(charToVal.get(h2C))) {
                    return charToVal.get(h1C) - charToVal.get(h2C);
                }
            }
            return 0;
        });
        long sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            HandT2 hand = hands.get(i);
            int score = hand.bid * (i + 1);
            sum += score;
           // System.out.println(hand.cards);
        }
        System.out.println(sum);
    }


    @AllArgsConstructor
    private static class HandT1 {
        private String cards;
        private Integer bid;
        private List<CardInfo> cardInfos = new ArrayList<>();


        public HandT1(String cards, Integer bid) {
            this.cards = cards;
            this.bid = bid;
            Map<Character, Integer> charToOccur = new HashMap<>();
            for (char c : cards.toCharArray()) {
                charToOccur.merge(c, 1, Integer::sum);
            }
            for (Map.Entry<Character, Integer> entry : charToOccur.entrySet()) {
                cardInfos.add(new CardInfo(entry.getKey(), entry.getValue()));

            }
            cardInfos.sort(Comparator.comparingInt((CardInfo o) -> o.occurrence).reversed());

        }
    }

    @AllArgsConstructor
    private static class HandT2 {
        private String cards;
        private Integer bid;
        private List<CardInfo> cardInfos = new ArrayList<>();


        public HandT2(String cards, Integer bid) {
            this.cards = cards;
            this.bid = bid;
            Map<Character, Integer> charToOccur = new HashMap<>();
            for (char c : cards.toCharArray()) {
                charToOccur.merge(c, 1, Integer::sum);
            }
            int jacksAmount = 0;
            for (Map.Entry<Character, Integer> entry : charToOccur.entrySet()) {
                if (entry.getKey() != 'J') {
                    cardInfos.add(new CardInfo(entry.getKey(), entry.getValue()));
                } else {
                    jacksAmount = jacksAmount + entry.getValue();
                }
            }
            cardInfos.sort(Comparator.comparingInt((CardInfo o) -> o.occurrence).reversed());
            if (cardInfos.size() == 0) {
                cardInfos.add(new CardInfo('J', 5));
            } else {
                CardInfo info = cardInfos.get(0);
                info.occurrence = info.occurrence + jacksAmount;
            }


        }
    }

    @AllArgsConstructor
    private static class CardInfo {
        Character card;
        int occurrence;
    }


}
