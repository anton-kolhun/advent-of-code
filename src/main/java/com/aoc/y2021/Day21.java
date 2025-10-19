package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day21 {

    static List<Integer> dices = possibleDices(3, 0);

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day21.txt");
        int starting1 = Integer.parseInt(ParseUtils.splitByDelimiter(lines.get(0), ":").get(1).trim());
        int starting2 = Integer.parseInt(ParseUtils.splitByDelimiter(lines.get(1), ":").get(1).trim());
        List<Player> players = List.of(new Player(0, starting1), new Player(0, starting2));
        int diceRolls = 0;
        int diceState = 1;
        int step = 0;
        Player winner;
        while (true) {
            int playerIndex = step % 2;
            var player = players.get(playerIndex);
            int diceSum = 0;
            for (int i = 0; i < 3; i++) {
                if (diceState == 101) {
                    diceState = 1;
                }
                diceSum = diceSum + diceState;
                diceState++;
            }
            player.space = (diceSum + player.space - 1) % 10 + 1;
            player.score = player.score + player.space;
            diceRolls += 3;
            if (player.score >= 1000) {
                winner = player;
                break;
            }
            step++;
        }
        Player loser = players.get(0) != winner ? players.get(0) : players.get(1);
        long score = loser.score * diceRolls;
        System.out.println("task1: " + score);
    }


    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day21.txt");
        int starting1 = Integer.parseInt(ParseUtils.splitByDelimiter(lines.get(0), ":").get(1).trim());
        int starting2 = Integer.parseInt(ParseUtils.splitByDelimiter(lines.get(1), ":").get(1).trim());
        List<Player> players = List.of(new Player(0, starting1), new Player(0, starting2));
        GameStats stats = playGame(0, players, new HashMap<>());
        BigInteger winnerScore = stats.p1Wins.compareTo(stats.p2Wins) > 0 ? stats.p1Wins : stats.p2Wins;
        System.out.println("task2: " + winnerScore);
    }

    private static GameStats playGame(int step, List<Player> players, Map<GameStepInfo, GameStats> cache) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.score >= 21) {
                var stats = i == 0 ? new GameStats(BigInteger.ONE, BigInteger.ZERO) : new GameStats(BigInteger.ZERO, BigInteger.ONE);
                return stats;
            }
        }

        if (cache.containsKey(new GameStepInfo(step, players))) {
            return cache.get(new GameStepInfo(step, players));
        }

        int playerIndex = step % 2;

        var player = players.get(playerIndex);
        GameStats total = new GameStats(BigInteger.ZERO, BigInteger.ZERO);

        for (Integer dice : dices) {
            Player copy = new Player(player.score, player.space);
            copy.space = (dice + copy.space - 1) % 10 + 1;
            copy.score = copy.score + copy.space;
            var copyPlayers = playerIndex == 0 ? List.of(copy, players.get(1)) : List.of(players.get(0), copy);
            GameStats stats = playGame(step + 1, copyPlayers, cache);
            total = new GameStats(total.p1Wins.add(stats.p1Wins), total.p2Wins.add(stats.p2Wins));
        }
        cache.put(new GameStepInfo(step, players), total);
        return total;
    }

    static List<Integer> possibleDices(int remainingSteps, int currentSum) {
        if (remainingSteps == 0) {
            return List.of(currentSum);
        }
        List<Integer> total = new ArrayList<>();
        for (int dice = 1; dice <= 3; dice++) {
            total.addAll(possibleDices(remainingSteps - 1, currentSum + dice));
        }
        return total;

    }

    private static class Player {
        int score;
        int space;

        public Player(int score, int space) {
            this.score = score;
            this.space = space;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Player player = (Player) o;
            return score == player.score && space == player.space;
        }

        @Override
        public int hashCode() {
            int result = score;
            result = 31 * result + space;
            return result;
        }
    }

    private static class GameStats {
        BigInteger p1Wins;
        BigInteger p2Wins;

        public GameStats(BigInteger p1Wins, BigInteger p2Wins) {
            this.p1Wins = p1Wins;
            this.p2Wins = p2Wins;
        }
    }

    private static class GameStepInfo {
        int step;
        List<Player> players;

        public GameStepInfo(int step, List<Player> players) {
            this.step = step;
            this.players = players;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GameStepInfo that = (GameStepInfo) o;
            return step == that.step && Objects.equals(players, that.players);
        }

        @Override
        public int hashCode() {
            int result = step;
            result = 31 * result + Objects.hashCode(players);
            return result;
        }
    }

}
