package com.example.aoc_2023.helper;

import java.math.BigInteger;

public class MathUtils {

    public static BigInteger lcm(BigInteger number1, BigInteger number2) {
        BigInteger mul = number1.multiply(number2);
        BigInteger gcd = number1.gcd(number2);
        BigInteger lcm = mul.divide(gcd);
        return lcm;
    }
}
