package com.epam.cdp.m2.hw2.aggregator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class Java8ParallelAggregator implements Aggregator {

  @Override
  public int sum(List<Integer> numbers) {
    return numbers.parallelStream().reduce(0, Integer::sum);
  }

  @Override
  public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
    return words.parallelStream()
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(limit)
        .map(w -> new Pair<>(w.getKey(), w.getValue()))
        .collect(Collectors.toList());
  }

  @Override
  public List<String> getDuplicates(List<String> words, long limit) {
    return words.parallelStream()
        .map(String::toUpperCase)
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
        .entrySet()
        .stream()
        .filter(w -> w.getValue() > 1)
        .map(Map.Entry::getKey)
        .sorted()
        .sorted(Comparator.comparingInt(String::length))
        .limit(limit)
        .collect(Collectors.toList());
  }
}
