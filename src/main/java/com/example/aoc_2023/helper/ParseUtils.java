package com.example.aoc_2023.helper;

import java.util.Arrays;
import java.util.List;

public class ParseUtils {

    public static List<String> splitByDelimiter(String str, String delimiter) {
        return Arrays.asList(str.split(delimiter));
    }

}
