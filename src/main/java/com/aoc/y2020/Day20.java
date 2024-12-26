package com.aoc.y2020;

import com.aoc.y2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day20 {


    public static void main(String[] args) throws Exception {
//        task1();
        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2020/day20.txt");
        List<Tile> tiles = new ArrayList<>();
        Tile current = new Tile();
        int row = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                tiles.add(current);
                current = new Tile();
                continue;
            }
            if (line.startsWith("Tile")) {
                String idSt = line.split(" ")[1];
                idSt = idSt.substring(0, idSt.length() - 1);
                current.setId(Integer.parseInt(idSt));
                row = 0;
                continue;
            }

            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                current.setCoord(row, col, c);
            }
            row++;
        }
        tiles.add(current);

        int squareLength = (int) Math.sqrt(tiles.size());

        List<Tile> total = new ArrayList<>();
        for (Tile tile : tiles) {
            List<Tile> forms = createForms(tile, 10);
            total.addAll(forms);
        }

        Map<Coordinate, Tile> result = findSquare(total, squareLength);

        long multiply = 1;
        multiply = multiply * result.get(new Coordinate(0, 0)).id;
        multiply = multiply * result.get(new Coordinate(0, squareLength - 1)).id;
        multiply = multiply * result.get(new Coordinate(squareLength - 1, 0)).id;
        multiply = multiply * result.get(new Coordinate(squareLength - 1, squareLength - 1)).id;

        System.out.println(multiply);


        for (int r = 0; r < squareLength * 10; r++) {
            var tileRowIndex = r / 10;
            var relativeRow = r % 10;
            if (relativeRow == 0 || relativeRow == 9) {
                continue;
            }
            for (int col = 0; col < squareLength * 10; col++) {
                var tileColIndex = col / 10;
                var relativeCol = col % 10;
                if (relativeCol == 0 || relativeCol == 9) {
                    continue;
                }
                var tile = result.get(new Coordinate(tileRowIndex, tileColIndex));
                char c = tile.getCoords().get(new Coordinate(relativeRow, relativeCol));
                System.out.print(c);
            }
            System.out.println();
        }

    }


    public static void task2() {
        Map<Coordinate, Character> data = new HashMap<>();
        List<String> lines = FilesUtils.readFile("aoc_2020/day20_p2.txt");
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            char[] charArray = line.toCharArray();
            for (int col = 0; col < charArray.length; col++) {
                char c = charArray[col];
                data.put(new Coordinate(row, col), c);
            }
        }


        int numberOfHashes = 0;
        for (Map.Entry<Coordinate, Character> entry : data.entrySet()) {
            if (entry.getValue() == '#') {
                numberOfHashes++;
            }
        }


        List<Tile> tiles = createForms(new Tile(1, data), lines.size());


        for (Tile tile : tiles) {
            int number = findMonster(tile, lines.size());
            if (number > 0) {
                int res = numberOfHashes - 15 * number;
                System.out.println(res);
                // return;
            }
        }
    }

    private static int findMonster(Tile tile, int squareLength) {
        int monsters = 0;
        Map<Coordinate, Character> coordValues = tile.getCoords();
        for (int row = 0; row < squareLength; row++) {
            for (int col = 0; col < squareLength; col++) {
                boolean res = checkMonter(row, col, coordValues);
                if (res) {
                    monsters++;
                }
            }
        }
        return monsters;
    }

    private static boolean checkMonter(int row, int col, Map<Coordinate, Character> coordValues) {
        return (coordValues.getOrDefault(new Coordinate(row, col - 1), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row, col - 2), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row, col - 7), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row, col - 8), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row, col - 13), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row, col - 14), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row, col - 19), 'X') == '#')

                && (coordValues.getOrDefault(new Coordinate(row - 1, col - 1), 'X') == '#')

                && (coordValues.getOrDefault(new Coordinate(row + 1, col - 3), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row + 1, col - 6), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row + 1, col - 9), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row + 1, col - 12), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row + 1, col - 15), 'X') == '#')
                && (coordValues.getOrDefault(new Coordinate(row + 1, col - 18), 'X') == '#');


    }

    private static Map<Coordinate, Tile> findSquare(List<Tile> tiles, int squareLength) {
        LinkedHashMap<Coordinate, Tile> image = new LinkedHashMap();
        Map<Coordinate, Tile> res = findIt(squareLength, tiles, image, new HashSet<>());
        return res;
    }

    private static Map<Coordinate, Tile> findIt(int squareLength, List<Tile> remainingTiles,
                                                LinkedHashMap<Coordinate, Tile> image, Set<Integer> usedTileIds) {
        if (image.size() > 1) {
            var lastCoord = image.lastEntry().getKey();
            Coordinate leftBro = new Coordinate(lastCoord.row, lastCoord.col - 1);
            if (image.containsKey(leftBro)) {
                boolean isValid = validateLeft(image.lastEntry().getValue(), image.get(leftBro));
                if (!isValid) {
                    return null;
                }
            }
            Coordinate upBro = new Coordinate(lastCoord.row - 1, lastCoord.col);
            if (image.containsKey(upBro)) {
                boolean isValid = validateUp(image.lastEntry().getValue(), image.get(upBro));
                if (!isValid) {
                    return null;
                }
            }
        }

        if (usedTileIds.size() == (squareLength * squareLength)) {
            return image;
        }
        Coordinate nextCoord;
        if (image.isEmpty()) {
            nextCoord = new Coordinate(0, 0);
        } else {
            Coordinate lastCoord = image.lastEntry().getKey();
            int nextRow = lastCoord.getRow();
            int nextCol = lastCoord.getCol() + 1;
            if (nextCol == (squareLength)) {
                nextCol = 0;
                nextRow++;
            }
            nextCoord = new Coordinate(nextRow, nextCol);
        }

        for (int i = 0; i < remainingTiles.size(); i++) {
            Tile nextTile = remainingTiles.get(i);
            List<Tile> nextRemaining = new ArrayList<>(remainingTiles);
            nextRemaining.remove(i);
            var nextCoords = new LinkedHashMap<>(image);
            nextCoords.put(nextCoord, nextTile);
            var nextTileIds = new HashSet<>(usedTileIds);
            if (nextTileIds.add(nextTile.getId())) {
                var res = findIt(squareLength, nextRemaining, nextCoords, nextTileIds);
                if (res != null) {
                    return res;
                }
            }
        }

        return null;
    }

    private static boolean validateLeft(Tile current, Tile left) {
        for (int row = 0; row < 10; row++) {
            if (current.coords.get(new Coordinate(row, 0)) != left.coords.get(new Coordinate(row, 9))) {
                return false;
            }
        }
        return true;
    }

    private static boolean validateUp(Tile current, Tile up) {
        for (int col = 0; col < 10; col++) {
            if (current.coords.get(new Coordinate(0, col)) != up.coords.get(new Coordinate(9, col))) {
                return false;
            }
        }
        return true;
    }

    private static List<Tile> createForms(Tile tile, int squareLength) {
        List<Tile> total = new ArrayList<>();
        total.add(tile);
        Map<Coordinate, Character> rotate = tile.coords;
        for (int i = 0; i < 3; i++) {
            rotate = rotateRight(rotate, squareLength);
            total.add(new Tile(tile.id, rotate));
        }
        Map<Coordinate, Character> flip = flipRight(tile.coords, squareLength);
        total.add(new Tile(tile.id, flip));
        rotate = flip;
        for (int i = 0; i < 3; i++) {
            rotate = rotateRight(rotate, squareLength);
            total.add(new Tile(tile.id, rotate));
        }
        return total;
    }

    private static Map<Coordinate, Character> rotateRight(Map<Coordinate, Character> coords, int squareLength) {
        Map<Coordinate, Character> rotate = new HashMap<>();
        for (int row = 0; row < squareLength; row++) {
            for (int col = 0; col < squareLength; col++) {
                rotate.put(new Coordinate(row, col), coords.get(new Coordinate(col, squareLength - 1 - row)));
            }
        }
        return rotate;
    }

    private static Map<Coordinate, Character> flipRight(Map<Coordinate, Character> coords, int squareLength) {
        Map<Coordinate, Character> flip = new HashMap<>();
        for (int row = 0; row < squareLength; row++) {
            for (int col = 0; col < squareLength; col++) {
                flip.put(new Coordinate(row, col), coords.get(new Coordinate(row, squareLength - 1 - col)));
            }
        }
        return flip;
    }

    private static void printTile(Map<Coordinate, Character> rotate, int squareLength) {
        for (int row = 0; row < squareLength; row++) {
            for (int col = 0; col < squareLength; col++) {
                System.out.print(rotate.get(new Coordinate(row, col)));
            }
            System.out.println();
        }
    }


    @Data
    @AllArgsConstructor
    private static class Coordinate {
        int row;
        int col;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Tile {
        @Setter
        int id;
        Map<Coordinate, Character> coords = new HashMap<>();


        public void setCoord(int row, int col, Character character) {
            coords.put(new Coordinate(row, col), character);
        }
    }

    @Data
    @AllArgsConstructor
    static class CoordinatedTile {
        private Tile tile;
        private Coordinate coord;
    }
}
