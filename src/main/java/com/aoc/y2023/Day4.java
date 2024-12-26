package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day4 {

    public static void main(String[] args) {
        //task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day4.txt");
        int total = 0;
        for (String line : lines) {
            List<String> groups = ParseUtils.splitByDelimiter(line, "\\|");
            String winningGroup = groups.get(0);
            String myCards = groups.get(1);
            List<String> winCards = ParseUtils.splitByDelimiter(winningGroup, ":");
            String vicCards = winCards.get(1);
            Set<String> cardsToWin = new HashSet<>(getCardsToWin(vicCards));
            // System.out.println(cardsToWin);
            List<String> cardsIHave = getCardsToWin(myCards);
            // System.out.println(cardsIHave);

            int currentScore = 0;
            for (String s : cardsIHave) {
                if (cardsToWin.contains(s)) {
                    if (currentScore == 0) {
                        currentScore = 1;
                    } else {
                        currentScore = currentScore * 2;
                    }
                }
            }
            total = total + currentScore;
        }
        System.out.println(total);

    }

    private static List<String> getCardsToWin(String winningGroup) {
        List<String> winningCards = ParseUtils.splitByDelimiter(winningGroup, " ");
        List<String> cardsToWin = winningCards.stream().map(String::trim)
                .filter(card -> !card.isEmpty())
                .toList();
        return cardsToWin;
    }


    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day4.txt");
        int maxCardNumber = lines.size() - 1;
        Map<Integer, Integer> cardToAmount = new HashMap<>();
        for (int cardNumber = 0; cardNumber < lines.size(); cardNumber++) {
            cardToAmount.merge(cardNumber, 1, Integer::sum);
            String line = lines.get(cardNumber);
            List<String> groups = ParseUtils.splitByDelimiter(line, "\\|");
            String winningGroup = groups.get(0);
            String myCards = groups.get(1);
            List<String> winCards = ParseUtils.splitByDelimiter(winningGroup, ":");
            String vicCards = winCards.get(1);
            Set<String> cardsToWin = new HashSet<>(getCardsToWin(vicCards));
            List<String> cardsIHave = getCardsToWin(myCards);


            int matches = 0;
            for (String s : cardsIHave) {
                if (cardsToWin.contains(s)) {
                    matches++;
                    int times = cardToAmount.get(cardNumber);
                    cardToAmount.merge(cardNumber + matches, times, Integer::sum);
                }
            }
        }
        long sum = cardToAmount.entrySet().stream()
                .filter(entry -> entry.getKey() <= maxCardNumber)
                .map(Map.Entry::getValue)
                .mapToInt(value -> value)
                .sum();

        System.out.println(sum);

    }

}
