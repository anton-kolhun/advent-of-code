package com.example.aoc_2024;

import com.example.aoc_2023.helper.FilesUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Day22 {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        task1();
//        task2();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2024/day22.txt");
        List<Long> secrets = lines.stream().map(Long::parseLong).toList();

        Map<List<Integer>, Set<IndexData>> seqToIndexes = new HashMap<>();
        BigInteger sum = BigInteger.ZERO;
        for (int index = 0; index < secrets.size(); index++) {
            Long secret = secrets.get(index);
            BigInteger res = sellIt(BigInteger.valueOf(secret), seqToIndexes, index);
            sum = sum.add(res);
        }
        Long maxValue = Long.MIN_VALUE;

        for (Map.Entry<List<Integer>, Set<IndexData>> entry : seqToIndexes.entrySet()) {
            Set<IndexData> sets = entry.getValue();
            long summa = sets.stream().mapToLong(value -> value.price).sum();
            if (summa > maxValue) {
                maxValue = summa;
            }
        }

        System.out.println("task1 = " + sum);
        System.out.println("task2 = " + maxValue);


    }


    private static BigInteger sellIt(BigInteger secret, Map<List<Integer>, Set<IndexData>> seqToIndex, int index) {
        int i = 0;
        List<BigInteger> secrets = new ArrayList<>();
        Integer initialSecretFirst = transformToFirstDigit(List.of(secret)).get(0);
        while (i < 2000) {
            BigInteger valueUpd = secret.multiply(BigInteger.valueOf(64));
            secret = mix(secret, valueUpd);
            secret = prune(secret);
            valueUpd = secret.divide(BigInteger.valueOf(32));//rounding?
            secret = mix(secret, valueUpd);
            secret = prune(secret);
            valueUpd = secret.multiply(BigInteger.valueOf(2048));
            secret = mix(secret, valueUpd);
            secret = prune(secret);
            secrets.add(secret);
            i++;
//            System.out.println(secret.toString());
        }
        List<Integer> firstDigits = transformToFirstDigit(secrets);

        int currentPrice = initialSecretFirst;
        List<Integer> deltas = new ArrayList<>();
        for (Integer firstDigit : firstDigits) {
            int diff = firstDigit - currentPrice;
            deltas.add(diff);
            currentPrice = firstDigit;
        }
        for (int j = deltas.size() - 1; j >= 3; j--) {
            int price = firstDigits.get(j);
            Integer currentDif = deltas.get(j);
            Integer minus1 = deltas.get(j - 1);
            Integer minus2 = deltas.get(j - 2);
            Integer minus3 = deltas.get(j - 3);
            List<Integer> chain = new ArrayList<>(List.of(minus3, minus2, minus1, currentDif));

            var initialList = new HashSet<>(Set.of(new IndexData(index, price, j)));
            seqToIndex.merge(chain, initialList, (old, newL) -> {
                old.remove(new IndexData(index, 0, 0));
                old.addAll(newL);
                return old;
            });

        }
        return secret;

    }

    private static List<Integer> transformToFirstDigit(List<BigInteger> secrets) {
        return secrets.stream()
                .map(secret -> {
                    var strVal = secret.toString();
                    return Integer.parseInt(String.valueOf(strVal.charAt(strVal.length() - 1)));
                })
                .toList();
    }

    private static BigInteger mix(BigInteger secret, BigInteger result) {
        return secret.xor(result);
    }

    private static BigInteger prune(BigInteger value) {
        return value.mod(BigInteger.valueOf(16777216));
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = {"price", "indexInSecret"})
    static class IndexData {
        Integer index;
        int price;
        int indexInSecret;
    }

}
