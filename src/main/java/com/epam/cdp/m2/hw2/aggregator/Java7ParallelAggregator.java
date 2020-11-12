package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class Java7ParallelAggregator implements Aggregator {

  // temporary maps are used for common write by different "forks" in inner classes
  protected static Map<String, Long> tempFrequencyMap = new HashMap<>();
  protected static Map<String, Long> tempDuplicatesMap = new HashMap<>();

  @Override
  public int sum(List<Integer> numbers) {
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    return forkJoinPool.invoke(new SumRecursiveTask(numbers, 0, numbers.size()));
  }

  @Override
  public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {

    ForkJoinPool forkJoinPool = new ForkJoinPool();
    forkJoinPool.invoke(new FrequencyRecursiveAction(words));

    List<Pair<String, Long>> frequencyList = new ArrayList<>();
    for (Map.Entry<String, Long> tempEntry : tempFrequencyMap.entrySet()) {
      frequencyList.add(new Pair<>(tempEntry.getKey(), tempEntry.getValue()));
    }

    frequencyList.sort(
        new Comparator<Pair<String, Long>>() {
          @Override
          public int compare(final Pair<String, Long> o1, Pair<String, Long> o2) {
            return o1.getKey().compareTo(o2.getKey());
          }
        });

    frequencyList.sort(
        new Comparator<Pair<String, Long>>() {
          @Override
          public int compare(final Pair<String, Long> o1, Pair<String, Long> o2) {
            return (int) (o2.getValue() - o1.getValue());
          }
        });

    if (!frequencyList.isEmpty() && frequencyList.size() > limit) {
      frequencyList = frequencyList.subList(0, (int) limit);
    }

    // clear static var for future usage
    tempFrequencyMap.clear();

    return frequencyList;
  }

  @Override
  public List<String> getDuplicates(List<String> words, long limit) {

    ForkJoinPool forkJoinPool = new ForkJoinPool();
    forkJoinPool.invoke(new DuplicatesRecursiveAction(words));

    List<String> duplicatesList = new ArrayList<>();
    for (String key : tempDuplicatesMap.keySet()) {
      if (tempDuplicatesMap.get(key) > 1) duplicatesList.add(key);
    }

    Collections.sort(duplicatesList);
    duplicatesList.sort(
        new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
            return o1.length() - o2.length();
          }
        });

    if (!duplicatesList.isEmpty() && duplicatesList.size() > limit) {
      duplicatesList = duplicatesList.subList(0, (int) limit);
    }
    // clear static var for future usage
    tempDuplicatesMap.clear();

    return duplicatesList;
  }

  /** Recursive Task for sum() method */
  class SumRecursiveTask extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 300_000;
    private List<Integer> list;
    private int begin;
    private int end;

    public SumRecursiveTask(List<Integer> list, int begin, int end) {
      this.list = list;
      this.begin = begin;
      this.end = end;
    }

    @Override
    protected Integer compute() {
      if ((end - begin) < THRESHOLD) {
        int sum = 0;
        for (int i = begin; i < end; i++) {
          sum += list.get(i);
        }

        return sum;
      } else {
        int middle = ((end + begin) / 2);
        SumRecursiveTask t1 = new SumRecursiveTask(list, begin, middle);
        t1.fork();
        SumRecursiveTask t2 = new SumRecursiveTask(list, middle, end);
        t2.fork();

        return t1.join() + t2.join();
      }
    }
  }

  /** Recursive Action for getDuplicates() methods */
  class FrequencyRecursiveAction extends RecursiveAction {

    private static final int THRESHOLD = 300_000;
    private final List<String> words;

    public FrequencyRecursiveAction(List<String> words) {
      this.words = words;
    }

    @Override
    protected void compute() {
      if ((words.size()) < THRESHOLD) {
        for (String word : words) {
          Long wordOccurrenceCounter = Java7ParallelAggregator.tempFrequencyMap.get(word);
          Java7ParallelAggregator.tempFrequencyMap.put(
              word, wordOccurrenceCounter == null ? 1 : wordOccurrenceCounter + 1);
        }
      } else {
        FrequencyRecursiveAction t1 =
            new FrequencyRecursiveAction(words.subList(0, words.size() / 2));
        FrequencyRecursiveAction t2 =
            new FrequencyRecursiveAction(words.subList(words.size() / 2, words.size()));
        t1.fork().join();
        t2.fork().join();
      }
    }
  }

  /** Recursive Action for getDuplicates() methods */
  class DuplicatesRecursiveAction extends RecursiveAction {

    private static final int THRESHOLD = 300_000;
    private List<String> words;

    public DuplicatesRecursiveAction(List<String> words) {
      this.words = words;
    }

    @Override
    protected void compute() {
      if ((words.size()) < THRESHOLD) {
        for (String word : words) {
          word = word.toUpperCase();
          Long wordOccurrenceCounter = Java7ParallelAggregator.tempDuplicatesMap.get(word);
          Java7ParallelAggregator.tempDuplicatesMap.put(
              word, wordOccurrenceCounter == null ? 1 : wordOccurrenceCounter + 1);
        }
      } else {
        DuplicatesRecursiveAction t1 =
            new DuplicatesRecursiveAction(words.subList(0, words.size() / 2));
        DuplicatesRecursiveAction t2 =
            new DuplicatesRecursiveAction(words.subList(words.size() / 2, words.size()));
        t1.fork().join();
        t2.fork().join();
      }
    }
  }
}
