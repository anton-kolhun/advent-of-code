package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day24V2 {

    private static BigDecimal MAX_RANGE = new BigDecimal("400000000000000");
    private static BigDecimal MIN_RANGE = new BigDecimal("200000000000000");
//    private static BigDecimal MAX_RANGE = new BigDecimal("27");
//    private static BigDecimal MIN_RANGE = new BigDecimal("7");

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        Map<Coordinate, Velo> coordToVelo = new HashMap<>();
        List<String> lines = FilesUtils.readFile("aoc_2023/day24.txt");
        List<Coordinate> allCoords = new ArrayList<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " @ ");
            List<String> coordStr = ParseUtils.splitByDelimiter(parts.get(0), ",");
            List<String> veloStr = ParseUtils.splitByDelimiter(parts.get(1), ",");
            var co = new Coordinate(new BigDecimal(coordStr.get(0).trim()), new BigDecimal(coordStr.get(1).trim()), new BigDecimal(coordStr.get(2).trim()));
            var velo = new Velo(Integer.parseInt(veloStr.get(0).trim()), Integer.parseInt(veloStr.get(1).trim()), Integer.parseInt(veloStr.get(2).trim()));
            coordToVelo.put(co, velo);
            allCoords.add(co);
        }
        int total = 0;
        for (int i = 0; i < allCoords.size() - 1; i++) {
            Coordinate current = allCoords.get(i);
            Velo curVelo = coordToVelo.get(current);
            Coordinate currentC2 = new Coordinate(current.x.add(BigDecimal.valueOf(curVelo.x)), current.y.add(BigDecimal.valueOf(curVelo.y)), current.z);
            BigDecimal k1 = currentC2.y.subtract(current.y).divide(currentC2.x.subtract(current.x), 100, RoundingMode.DOWN);
            BigDecimal b1 = current.y.subtract(current.x.multiply(k1));

            for (int j = i + 1; j < allCoords.size(); j++) {
                Coordinate next = allCoords.get(j);
                Velo nextVelo = coordToVelo.get(next);
                Coordinate nextC2 = new Coordinate(next.x.add(BigDecimal.valueOf(nextVelo.x)), next.y.add(BigDecimal.valueOf(nextVelo.y)), next.z);
                BigDecimal k2 = nextC2.y.subtract(next.y).divide(nextC2.x.subtract(next.x), 100, RoundingMode.DOWN);
                BigDecimal b2 = next.y.subtract(next.x.multiply(k2));
                if (!k1.equals(k2)) {
                    BigDecimal x0 = b2.subtract(b1).divide(k1.subtract(k2), 100, RoundingMode.DOWN);
                    BigDecimal y0 = k1.multiply(x0).add(b1);

                    int comparison = current.x.compareTo(currentC2.x);
                    if (current.x.compareTo(x0) != comparison) {
                        continue;
                    }

                    comparison = current.y.compareTo(currentC2.y);
                    if (current.y.compareTo(y0) != comparison) {
                        continue;
                    }

                    comparison = next.x.compareTo(nextC2.x);
                    if (next.x.compareTo(x0) != comparison) {
                        continue;
                    }

                    comparison = next.y.compareTo(nextC2.y);
                    if (next.y.compareTo(y0) != comparison) {
                        continue;
                    }


                    if ((x0.compareTo(MIN_RANGE) >= 0 && x0.compareTo(MAX_RANGE) <= 0) &&
                            (y0.compareTo(MIN_RANGE) >= 0 && y0.compareTo(MAX_RANGE) <= 0)) {
                        total++;
                    }
                }

            }
        }

        System.out.println(total);
    }


    @ToString
    private static class Coordinate {
        private BigDecimal x;
        private BigDecimal y;
        private BigDecimal z;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;

            if (!Objects.equals(x, that.x)) return false;
            if (!Objects.equals(y, that.y)) return false;
            return Objects.equals(z, that.z);
        }

        @Override
        public int hashCode() {
            int result = x != null ? x.hashCode() : 0;
            result = 31 * result + (y != null ? y.hashCode() : 0);
            result = 31 * result + (z != null ? z.hashCode() : 0);
            return result;
        }

        public Coordinate(BigDecimal x, BigDecimal y, BigDecimal z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BigDecimal getAxis(int index) {
            if (index == 0) {
                return x;
            }
            if (index == 1) {
                return y;
            }
            if (index == 2) {
                return z;
            }
            return null;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class Velo {
        private int x;
        private int y;
        private int z;


        public int getAxis(int index) {
            if (index == 0) {
                return x;
            }
            if (index == 1) {
                return y;
            }
            if (index == 2) {
                return z;
            }
            return 0;
        }

    }
}
