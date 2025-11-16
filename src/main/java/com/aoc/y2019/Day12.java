package com.aoc.y2019;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.MathUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day12 {

    public static void main(String[] args) {
        task();
    }

    public static void task() {
        List<String> lines = FilesUtils.readFile("aoc_2019/day12.txt");
        List<Moon> moons = new ArrayList<>();
        for (String line : lines) {
            line = line.substring(1, line.length() - 1);
            List<Integer> positions = ParseUtils.splitByDelimiter(line, ",")
                    .stream()
                    .map(String::trim)
                    .map(s -> ParseUtils.splitByDelimiter(s, "=").get(1))
                    .map(Integer::parseInt)
                    .toList();
            Coordinate position = new Coordinate(positions.get(0), positions.get(1), positions.get(2));
            var moon = new Moon(position, new Coordinate(0, 0, 0));
            moons.add(moon);
        }


        Set<List<Integer>> uniqueX = new LinkedHashSet<>();
        Set<List<Integer>> uniqueY = new LinkedHashSet<>();
        Set<List<Integer>> uniqueZ = new LinkedHashSet<>();
        List<Integer> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();
        List<Integer> zData = new ArrayList<>();
        for (Moon moon : moons) {
            xData.addAll(List.of(moon.position.x, moon.velocity.x));
            yData.addAll(List.of(moon.position.y, moon.velocity.y));
            zData.addAll(List.of(moon.position.z, moon.velocity.z));
        }
        uniqueX.add(xData);
        uniqueY.add(yData);
        uniqueZ.add(zData);

        List<Integer> cycle = new ArrayList<>();
        cycle.add(0);
        cycle.add(0);
        cycle.add(0);

        int counter = 0;
        while (cycle.get(0) == 0 || cycle.get(1) == 0 || cycle.get(2) == 0) {
            List<Moon> nextMoons = new ArrayList<>();
            for (int j = 0; j < moons.size(); j++) {
                Moon moon1 = moons.get(j);
                Coordinate diff = new Coordinate(0, 0, 0);
                for (int k = 0; k < moons.size(); k++) {
                    Moon moon2 = moons.get(k);
                    if (moon1 != moon2) {
                        if (moon1.position.x > moon2.position.x) {
                            diff.x = diff.x - 1;
                        } else if (moon1.position.x < moon2.position.x) {
                            diff.x = diff.x + 1;
                        }
                        if (moon1.position.y > moon2.position.y) {
                            diff.y = diff.y - 1;
                        } else if (moon1.position.y < moon2.position.y) {
                            diff.y = diff.y + 1;
                        }
                        if (moon1.position.z > moon2.position.z) {
                            diff.z = diff.z - 1;
                        } else if (moon1.position.z < moon2.position.z) {
                            diff.z = diff.z + 1;
                        }
                    }
                }
                Coordinate nextVelocity = new Coordinate(
                        moon1.velocity.x + diff.x,
                        moon1.velocity.y + diff.y,
                        moon1.velocity.z + diff.z);
                Coordinate nextPosition = new Coordinate(
                        moon1.position.x + nextVelocity.x,
                        moon1.position.y + nextVelocity.y,
                        moon1.position.z + nextVelocity.z);
                nextMoons.add(new Moon(nextPosition, nextVelocity));
            }
            moons = nextMoons;


            xData = new ArrayList<>();
            yData = new ArrayList<>();
            zData = new ArrayList<>();
            for (Moon moon : moons) {
                xData.addAll(List.of(moon.position.x, moon.velocity.x));
                yData.addAll(List.of(moon.position.y, moon.velocity.y));
                zData.addAll(List.of(moon.position.z, moon.velocity.z));
            }
            if (!uniqueX.add(xData)) {
                if (cycle.get(0) == 0) {
                    cycle.set(0, counter + 1);
                }
            }
            if (!uniqueY.add(yData)) {
                if (cycle.get(1) == 0) {
                    cycle.set(1, counter + 1);
                }
            }
            if (!uniqueZ.add(zData)) {
                if (cycle.get(2) == 0) {
                    cycle.set(2, counter + 1);
                }
            }

            if (counter == 999) {
                System.out.println("task1: " + calculateScore(nextMoons));
            }
            counter++;
        }
        BigInteger fullCycle = BigInteger.valueOf(cycle.getFirst());
        for (int i = 1; i < cycle.size(); i++) {
            fullCycle = MathUtils.lcm(fullCycle, BigInteger.valueOf(cycle.get(i)));
        }
        System.out.println("task2: " + fullCycle);
    }

    private static int calculateScore(List<Moon> moons) {
        int totalScore = 0;
        for (Moon moon : moons) {
            int potential = Math.abs(moon.position.x) + Math.abs(moon.position.y) + Math.abs(moon.position.z);
            int kinetic = Math.abs(moon.velocity.x) + Math.abs(moon.velocity.y) + Math.abs(moon.velocity.z);
            totalScore += potential * kinetic;
        }
        return totalScore;
    }


    private static class Coordinate {
        int x, y, z = 0;

        public Coordinate(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }

        @Override
        public String toString() {
            return x + "," + y + "," + z;
        }
    }

    private static class Moon {
        Coordinate position;
        Coordinate velocity;

        public Moon(Coordinate position, Coordinate velocity) {
            this.position = position;
            this.velocity = velocity;
        }

        @Override
        public String toString() {
            return position.x + "," + position.y + "," + position.z + ";;"
                    + velocity.x + "," + velocity.y + "," + velocity.z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Moon moon = (Moon) o;
            return Objects.equals(position, moon.position)
                    && Objects.equals(velocity, moon.velocity);
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(position);
            result = 31 * result + Objects.hashCode(velocity);
            return result;
        }
    }
}
