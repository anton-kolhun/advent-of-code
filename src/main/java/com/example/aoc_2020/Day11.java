package com.example.aoc_2020;

import com.example.aoc_2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 {

    public static void main(String[] args) {
//        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day11.txt");
        int rows = lines.size();
        int cols = lines.get(0).length();
        Map<Seat, Character> seats = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] lineChars = line.toCharArray();
            for (int col = 0; col < lineChars.length; col++) {
                Character state = lineChars[col];
                seats.put(new Seat(row, col), state);
            }
        }

        while (true) {
            var nextSeats = new HashMap<Seat, Character>();
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    var seat = new Seat(row, col);
                    char state = seats.get(seat);
                    if (state == '.') {
                        nextSeats.put(seat, state);
                        continue;
                    } else if (state == 'L') {
                        List<Seat> neibs = getNeibs(seat);
                        boolean occupy = true;
                        for (Seat neib : neibs) {
                            if (seats.getOrDefault(neib, 'X') == '#') {
                                occupy = false;
                                break;
                            }
                        }
                        if (occupy) {
                            state = '#';
                        }
                    } else {
                        List<Seat> neibs = getNeibs(seat);
                        int occupied = 0;
                        for (Seat neib : neibs) {
                            if (seats.getOrDefault(neib, 'X') == '#') {
                                occupied++;
                            }
                        }
                        if (occupied >= 4) {
                            state = 'L';
                        }
                    }
                    nextSeats.put(seat, state);
                }
            }

            if (seats.equals(nextSeats)) {
                break;
            }
            seats = nextSeats;
//            print(seats, rows, cols);
        }

        var occupied = seats.entrySet().stream()
                .filter(seatCharacterEntry -> seatCharacterEntry.getValue() == '#')
                .toList();
        System.out.println(occupied.size());

    }

    private static void print(Map<Seat, Character> seats, int rows, int cols) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                System.out.print(seats.get(new Seat(row, col)));
            }
            System.out.println();
        }
        System.out.println();
    }


    public static void task2() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day11.txt");
        int rows = lines.size();
        int cols = lines.get(0).length();
        Map<Seat, Character> seats = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] lineChars = line.toCharArray();
            for (int col = 0; col < lineChars.length; col++) {
                Character state = lineChars[col];
                seats.put(new Seat(row, col), state);
            }
        }

        while (true) {
            var nextSeats = new HashMap<Seat, Character>();
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    var seat = new Seat(row, col);
                    char state = seats.get(seat);
                    if (state == '.') {
                        nextSeats.put(seat, state);
                        continue;
                    } else if (state == 'L') {
                        List<Seat> neibs = getNeibsPart2(seat, seats, rows, cols);
                        boolean occupy = true;
                        for (Seat neib : neibs) {
                            if (neib.state == '#') {
                                occupy = false;
                                break;
                            }
                        }
                        if (occupy) {
                            state = '#';
                        }
                    } else {
                        List<Seat> neibs = getNeibsPart2(seat, seats, rows, cols);
                        int occupied = 0;
                        for (Seat neib : neibs) {
                            if (neib.state == '#') {
                                occupied++;
                            }
                        }
                        if (occupied >= 5) {
                            state = 'L';
                        }
                    }
                    nextSeats.put(seat, state);
                }
            }

            if (seats.equals(nextSeats)) {
                break;
            }
            seats = nextSeats;
//            print(seats, rows, cols);
        }

        var occupied = seats.entrySet().stream()
                .filter(seatCharacterEntry -> seatCharacterEntry.getValue() == '#')
                .toList();
        System.out.println(occupied.size());

    }


    private static List<Seat> getNeibs(Seat seat) {
        return Arrays.asList(
                new Seat(seat.row + 1, seat.col),
                new Seat(seat.row - 1, seat.col),
                new Seat(seat.row, seat.col + 1),
                new Seat(seat.row, seat.col - 1),
                new Seat(seat.row + 1, seat.col + 1),
                new Seat(seat.row + 1, seat.col - 1),
                new Seat(seat.row - 1, seat.col + 1),
                new Seat(seat.row - 1, seat.col - 1)
        );
    }

    private static List<Seat> getNeibsPart2(Seat seat, Map<Seat, Character> seats, int rows, int cols) {
        List<Seat> moves = Arrays.asList(new Seat(1, 0), new Seat(-1, 0), new Seat(0, 1), new Seat(0, -1),
                new Seat(1, 1), new Seat(1, -1), new Seat(-1, 1), new Seat(-1, -1)
        );
        List<Seat> neibs = new ArrayList<>();
        for (Seat move : moves) {
            Seat seat1 = getFirstSeatOnDirection(seat, move, seats, rows, cols);
            neibs.add(seat1);
        }
        return neibs;

    }

    private static Seat getFirstSeatOnDirection(Seat seat, Seat move, Map<Seat, Character> seats, int rows, int cols) {
        Seat cursor = seat;
        while (true) {
            cursor = new Seat(cursor.row + move.row, cursor.col + move.col);
            if (cursor.row < 0 || cursor.row >= rows || cursor.col < 0 || cursor.col >= cols) {
                return new Seat(cursor.row, cursor.col, 'X');
            }
            var state = seats.get(cursor);
            if (state == '#' || state == 'L') {
                return new Seat(cursor.row, cursor.col, state);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = "state")
    static class Seat {
        int row;
        int col;
        Character state;

        public Seat(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}
