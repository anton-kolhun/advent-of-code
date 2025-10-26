package com.aoc.y2021;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day19 {

    private static Set<Integer> scannerStart = Collections.emptySet();
    private static List<List<String>> combins = getCombinations(List.of(new Pair("x", "-x"), new Pair("y", "-y"), new Pair("z", "-z")),
            new ArrayList<>());

    public static void main(String[] args) {
        task1();
    }

    private static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2021/day19.txt");
        List<Scanner> scanners = new ArrayList<>();
        Scanner currentScanner = null;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("scanner")) {
                currentScanner = new Scanner(new ArrayList<>(), scanners.size(), new Coordinate(0, 0, 0));
                scanners.add(currentScanner);
                continue;
            }
            if (line.isEmpty()) {
                continue;
            }
            List<Integer> coords = ParseUtils.splitByDelimiter(line, ",").stream().map(Integer::parseInt).toList();
            currentScanner.beacons().add(new Coordinate(coords.get(0), coords.get(1), coords.get(2)));
        }

        var firstScanner = scanners.removeFirst();
        List<Scanner> result = getMatchingRotatedScanners(scanners, List.of(firstScanner));
        Set<Coordinate> totalBeacons = new HashSet<>();
        for (Scanner scanner : result) {
            totalBeacons.addAll(scanner.beacons());
        }
        System.out.println("task1: " + totalBeacons.size());

        int maxDist = calculateMaxDistance(result);
        System.out.println("task2: " + maxDist);
    }

    private static int calculateMaxDistance(List<Scanner> result) {
        int maxDistance = 0;
        for (int i = 0; i < result.size() - 1; i++) {
            Scanner scanner1 = result.get(i);
            for (int j = i + 1; j < result.size(); j++) {
                Scanner scanner2 = result.get(j);
                int distance = Math.abs(scanner2.start.x - scanner1.start.x) +
                        Math.abs(scanner2.start.y - scanner1.start.y) +
                        Math.abs(scanner2.start.z - scanner1.start.z);
                maxDistance = Math.max(maxDistance, distance);
            }
        }
        return maxDistance;
    }

    private static List<Scanner> getMatchingRotatedScanners(List<Scanner> remScanners, List<Scanner> currentScanners) {
        if (remScanners.isEmpty()) {
            return currentScanners;
        }
        for (Scanner remScanner : remScanners) {
            List<Scanner> views = getScannerViews(remScanner);
            for (Scanner scannerToAdd : views) {
                List<List<Scanner>> nextScannersList = getAlignedScanners(currentScanners, scannerToAdd);
                for (List<Scanner> nextScanners : nextScannersList) {
                    List<Scanner> nextRemScanners = new ArrayList<>(remScanners);
                    nextRemScanners.remove(remScanner);
                    var res = getMatchingRotatedScanners(nextRemScanners, nextScanners);
                    if (!res.isEmpty()) {
                        return res;
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static List<List<Scanner>> getAlignedScanners(List<Scanner> currentScanners, Scanner scannerToAdd) {
        List<List<Scanner>> total = new ArrayList<>();
        for (Scanner currentScanner : currentScanners) {
            for (Coordinate currentCoord : currentScanner.beacons()) {
                for (Coordinate toAddCoord : scannerToAdd.beacons()) {
                    Coordinate shift = new Coordinate(currentCoord.x - toAddCoord.x, currentCoord.y - toAddCoord.y(), currentCoord.z - toAddCoord.z());
                    List<Coordinate> withShift = scannerToAdd.beacons().stream()
                            .map(toAdd -> new Coordinate(toAdd.x + shift.x(), toAdd.y + shift.y(), toAdd.z() + shift.z()))
                            .collect(Collectors.toList());
                    var scannerWithShift = new Scanner(withShift, scannerToAdd.index, shift);
                    boolean match = shouldOverlap(currentScanner, scannerWithShift);
                    if (match) {
                        List<Scanner> nextTaken = new ArrayList<>(currentScanners);
                        nextTaken.add(scannerWithShift);
                        total.add(nextTaken);
                    }
                }
            }
        }
        return total;
    }

    private static boolean shouldOverlap(Scanner takenScanner, Scanner scannerWithShift) {
        int counter = 0;
        for (Coordinate beacon : takenScanner.beacons) {
            if (scannerWithShift.beacons.contains(beacon)) {
                counter++;
            }
        }
        if (counter >= 12) {
            return true;
        }
        return false;
    }

    private static List<Scanner> getScannerViews(Scanner scanner) {
        List<Scanner> views = new ArrayList<>();
        for (List<String> combin : combins) {
            Scanner scannerView = new Scanner(new ArrayList<>(), scanner.index, new Coordinate(0, 0, 0));
            for (Coordinate beacon : scanner.beacons()) {
                List<Integer> data = getData(combin, beacon);
                scannerView.beacons().add(new Coordinate(data.get(0), data.get(1), data.get(2)));
            }
            views.add(scannerView);
        }
        return views;
    }

    private static List<Integer> getData(List<String> combin, Coordinate beacon) {
        return combin.stream().map(s -> {
            int value;
            if (s.contains("x")) {
                value = beacon.x;
            } else if (s.contains("y")) {
                value = beacon.y;
            } else {
                value = beacon.z;
            }
            if (s.contains("-")) {
                value = -value;
            }
            return value;
        }).toList();
    }

    private static List<List<String>> getCombinations(List<Pair> remaining, List<String> current) {
        if (current.size() == 3) {
            return List.of(current);
        }
        List<List<String>> total = new ArrayList<>();
        for (Pair pair : remaining) {
            List<Pair> nextPair = new ArrayList<>(remaining);
            nextPair.remove(pair);
            List<String> next = new ArrayList<>(current);
            next.add(pair.left);
            total.addAll(getCombinations(nextPair, next));
            next = new ArrayList<>(current);
            next.add(pair.right);
            total.addAll(getCombinations(nextPair, next));
        }
        return total;
    }

    private record Pair(String left, String right) {
    }

    private record Scanner(List<Coordinate> beacons, int index, Coordinate start) {

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (Coordinate beacon : beacons()) {
                sb.append(beacon.x).append(",")
                        .append(beacon.y).append(",")
                        .append(beacon.z);
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    private record Coordinate(int x, int y, int z) {

    }
}
