package com.epam.cdp.m2.hw2.aggregator;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class Java8Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        return numbers.stream()
                .reduce(0, Integer::sum);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        return words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Entry.comparingByKey())
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .map(w -> new Pair<>(w.getKey(), w.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        return words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(w -> w.getValue() > 1)
                .map(Entry::getKey)
                .sorted()
                .sorted((s1, s2) -> s1.length() - s2.length())
                .limit(limit)
                .collect(Collectors.toList());
    }
}