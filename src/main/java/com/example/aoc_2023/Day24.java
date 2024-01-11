package com.example.aoc_2023;

import com.example.aoc_2023.helper.FilesUtils;
import com.example.aoc_2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day24 {

    private static BigInteger MAX_RANGE = new BigInteger("400000000000000");
//    private static BigInteger MIN_RANGE = new BigInteger("200000000000000");
    private static BigInteger MIN_RANGE = new BigInteger("7");

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        Map<Coordinate, Velo> coordToVelo = new HashMap<>();
        List<String> lines = FilesUtils.readFile("day24_t.txt");
        List<Coordinate> allCoords = new ArrayList<>();
        for (String line : lines) {
            List<String> parts = ParseUtils.splitByDelimiter(line, " @ ");
            List<String> coordStr = ParseUtils.splitByDelimiter(parts.get(0), ",");
            List<String> veloStr = ParseUtils.splitByDelimiter(parts.get(1), ",");
            var co = new Coordinate(new BigInteger(coordStr.get(0).trim()), new BigInteger(coordStr.get(1).trim()), new BigInteger(coordStr.get(2).trim()));
            var velo = new Velo(Integer.parseInt(veloStr.get(0).trim()), Integer.parseInt(veloStr.get(1).trim()), Integer.parseInt(veloStr.get(2).trim()));
            coordToVelo.put(co, velo);
            allCoords.add(co);
        }
        int total = 0;
        int totalPossible = 0;
        for (int i = 0; i < allCoords.size() - 1; i++) {
            Coordinate current = allCoords.get(i);
            Velo curVelo = coordToVelo.get(current);
            for (int j = i + 1; j < allCoords.size(); j++) {
                Coordinate next = allCoords.get(j);
                Velo nextVelo = coordToVelo.get(next);
                BigInteger xIntertime = null;
                BigInteger yInterTime = null;
                if (current.x.compareTo(next.x) >= 0) {
                    xIntertime = intersects(curVelo, nextVelo, current, next, 0);
                } else {
                    xIntertime = intersects(nextVelo, curVelo, next, current, 0);
                }

                if (xIntertime == null) {
                    continue;
                }

                if (current.y.compareTo(next.y) >= 0) {
                    yInterTime = intersects(curVelo, nextVelo, current, next, 1);
                } else {
                    yInterTime = intersects(nextVelo, curVelo, next, current, 1);
                }
                if (yInterTime == null) {
                    continue;
                }
                totalPossible++;

                BigInteger minTimeForInter;
                if (yInterTime.compareTo(xIntertime) > 0) {
                    minTimeForInter = yInterTime;

                    BigInteger currentInterX = current.x.add(BigInteger.valueOf(curVelo.x).multiply(minTimeForInter));
                    BigInteger currentInterY = current.y.add(BigInteger.valueOf(curVelo.y).multiply(minTimeForInter));
                    BigInteger nextInterX = next.x.add(BigInteger.valueOf(nextVelo.x).multiply(minTimeForInter));
                    BigInteger nextInterY = next.y.add(BigInteger.valueOf(nextVelo.y).multiply(minTimeForInter));
                    boolean intersect = currentInterX.compareTo(MIN_RANGE) >= 0 && currentInterX.compareTo(MAX_RANGE) <= 0
                            && nextInterX.compareTo(MIN_RANGE) >= 0 && nextInterX.compareTo(MAX_RANGE) <= 0 &&
                            currentInterY.compareTo(MIN_RANGE) >= 0 && currentInterY.compareTo(MAX_RANGE) <= 0 &&
                            nextInterY.compareTo(MIN_RANGE) >= 0 && nextInterY.compareTo(MAX_RANGE) <= 0;
                    if (intersect) {
                        total++;
                    }
                }
            }
        }

        System.out.println(totalPossible);
        System.out.println(total);
    }

    private static BigInteger intersects(Velo velo1, Velo velo2, Coordinate c1, Coordinate c2, int index) {
        BigInteger c1Coord = c1.getAxis(index);
        BigInteger c2Coord = c2.getAxis(index);
        int velo1Coord = velo1.getAxis(index);
        int velo2Coord = velo2.getAxis(index);
        System.out.println(String.format("c1=[%s], v1=[%s];;; c2=[%s], v2=%s", c1Coord, velo1Coord, c2Coord, velo2Coord));
        if (velo1Coord > 0 && velo2Coord < 0) {
            System.out.println("no overlap");
            return null;
        }
        if (velo1Coord > 0 && velo2Coord > 0 && velo1Coord >= velo2Coord) {
            System.out.println("no overlap");
            return null;
        }
        if (velo1Coord < 0 && velo2Coord < 0 && (velo2Coord <= velo1Coord)) {
            System.out.println("no overlap");
            return null;
        }

        if (velo1Coord > 0 && velo2Coord > 0 && (velo1Coord < velo2Coord)) {
            BigInteger intersectionTime = c1Coord.subtract(c2Coord).divide(BigInteger.valueOf(velo2Coord - velo1Coord));
            System.out.println("overlap in " + intersectionTime);
            return intersectionTime;

        }
        if (velo1Coord < 0 && velo2Coord < 0 && (velo2Coord > velo1Coord)) {
            BigInteger intersectionTime = c1Coord.subtract(c2Coord).divide(BigInteger.valueOf(-1 * (velo1Coord - velo2Coord)));
            System.out.println("overlap in " + intersectionTime);

            return intersectionTime;

        }
        if (velo1Coord < 0 && velo2Coord > 0) {
            BigInteger intersectionTime = c1Coord.subtract(c2Coord).divide(BigInteger.valueOf(velo2Coord - velo1Coord));
            System.out.println("overlap in " + intersectionTime);
            return intersectionTime;

        }
        System.out.println("no overlap");
        return null;
    }


    private static BigInteger yIntersects(Velo velo1, Velo velo2, Coordinate c1, Coordinate c2) {
        if (velo1.y > 0 && velo2.y < 0) {
            return null;
        }
        if (velo1.y > 0 && velo2.y > 0 && velo1.y >= velo2.y) {
            return null;
        }
        if (velo1.y < 0 && velo2.y < 0 && (velo2.y <= velo1.y)) {
            return null;
        }

        if (velo1.y > 0 && velo2.y > 0 && (velo1.y < velo2.y)) {
            BigInteger intersectionTime = c1.y.subtract(c2.y).divide(BigInteger.valueOf(velo2.y - velo1.y));
            // return intersectionTime;
            // BigInteger interCoord = c1.x.add(BigInteger.valueOf(velo1.x).multiply(intersectionTime));
            return intersectionTime;
            //return interCoord.compareTo(MIN_RANGE) >= 0 && interCoord.compareTo(MAX_RANGE) <= 0;

        }
        if (velo1.y < 0 && velo2.y < 0 && (velo2.y > velo1.y)) {
            BigInteger intersectionTime = c1.y.subtract(c2.y).divide(BigInteger.valueOf(-1 * (velo1.y - velo2.y)));
            // return intersectionTime;
            BigInteger interCoord = c1.y.add(BigInteger.valueOf(velo1.y).multiply(intersectionTime));
            return intersectionTime;
            //return interCoord.compareTo(MIN_RANGE) >= 0 && interCoord.compareTo(MAX_RANGE) <= 0;

        }
        if (velo1.y < 0 && velo2.y > 0) {
            BigInteger intersectionTime = c1.y.subtract(c2.y).divide(BigInteger.valueOf(velo2.y - velo1.y));
            // return intersectionTime;
            BigInteger interCoord = c1.y.add(BigInteger.valueOf(velo1.y).multiply(intersectionTime));
            return intersectionTime;
            //return interCoord.compareTo(MIN_RANGE) >= 0 && interCoord.compareTo(MAX_RANGE) <= 0;

        }
        return null;
    }


    @ToString
    private static class Coordinate {
        private BigInteger x;
        private BigInteger y;
        private BigInteger z;

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

        public Coordinate(BigInteger x, BigInteger y, BigInteger z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BigInteger getAxis(int index) {
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
