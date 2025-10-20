package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day4 {

    public static void main(String[] args) {
        task1();
        task2();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day4.txt");
        List<Integer> draws = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(Integer::parseInt).toList();
        int lineNumber = 2;
        List<Board> boards = new ArrayList<>();
        Board currentBoard = new Board();
        for (; lineNumber < lines.size(); lineNumber++) {
            if (lines.get(lineNumber).isEmpty()) {
                boards.add(currentBoard);
                currentBoard = new Board();
                continue;
            }
            List<Integer> row = ParseUtils.splitByDelimiter(lines.get(lineNumber), " ").stream()
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt).toList();
            currentBoard.rows.add(row);
        }
        boards.add(currentBoard);

        int score = getWinnerScore(draws, boards);
        System.out.println("task1: " + score);
    }

    private static int getWinnerScore(List<Integer> draws, List<Board> boards) {
        List<Integer> currentDraw = new ArrayList<>();
        for (Integer draw : draws) {
            currentDraw.add(draw);
            for (Board board : boards) {
                //check rows
                for (List<Integer> row : board.rows) {
                    var copy = new ArrayList<>(row);
                    copy.removeAll(currentDraw);
                    if (copy.isEmpty()) {
                        return calcScore(draw, board, currentDraw);
                    }
                }
                //check columns
                for (int col = 0; col < board.rows.getFirst().size(); col++) {
                    var colValues = new ArrayList<>();
                    for (List<Integer> rowValues : board.rows) {
                        colValues.add(rowValues.get(col));
                    }
                    colValues.removeAll(currentDraw);
                    if (colValues.isEmpty()) {
                        return calcScore(draw, board, currentDraw);
                    }
                }
            }
        }
        return -1;
    }

    private static int calcScore(Integer draw, Board board, List<Integer> currentDraw) {
        Set<Integer> drawSet = new HashSet<>(currentDraw);
        int sum = 0;
        for (List<Integer> rowValues : board.rows) {
            for (Integer value : rowValues) {
                if (!drawSet.contains(value)) {
                    sum = sum + value;
                }
            }
        }
        return draw * sum;
    }


    private static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day4.txt");
        List<Integer> draws = ParseUtils.splitByDelimiter(lines.get(0), ",").stream()
                .map(Integer::parseInt).toList();
        int lineNumber = 2;
        List<Board> boards = new ArrayList<>();
        Board currentBoard = new Board();
        for (; lineNumber < lines.size(); lineNumber++) {
            if (lines.get(lineNumber).isEmpty()) {
                boards.add(currentBoard);
                currentBoard = new Board();
                continue;
            }
            List<Integer> row = ParseUtils.splitByDelimiter(lines.get(lineNumber), " ").stream()
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt).toList();
            currentBoard.rows.add(row);
        }
        boards.add(currentBoard);

        Board lastBoardToWin = getLastBoardToWin(draws, boards);
        int value = getWinnerScore(draws, List.of(lastBoardToWin));
        System.out.println("task2: " + value);
    }

    private static Board getLastBoardToWin(List<Integer> draws, List<Board> boards) {
        List<Integer> currentDraw = new ArrayList<>();
        Set<Integer> winnerIndexes = new HashSet<>();
        for (Integer draw : draws) {
            currentDraw.add(draw);
            for (int boardIndex = 0; boardIndex < boards.size(); boardIndex++) {
                Board board = boards.get(boardIndex);
                //check rows
                for (List<Integer> row : board.rows) {
                    var copy = new ArrayList<>(row);
                    copy.removeAll(currentDraw);
                    if (copy.isEmpty()) {
                        winnerIndexes.add(boardIndex);
                        if (winnerIndexes.size() == boards.size()) {
                            return boards.get(boardIndex);
                        }
                    }
                }

                //check columns
                for (int col = 0; col < board.rows.getFirst().size(); col++) {
                    var colValues = new ArrayList<>();
                    for (List<Integer> rowValues : board.rows) {
                        colValues.add(rowValues.get(col));
                    }
                    colValues.removeAll(currentDraw);
                    if (colValues.isEmpty()) {
                        winnerIndexes.add(boardIndex);
                        if (winnerIndexes.size() == boards.size()) {
                            return boards.get(boardIndex);
                        }
                    }
                }

            }
        }
        return null;
    }

    private static class Board {
        List<List<Integer>> rows = new ArrayList<>();
    }

}
