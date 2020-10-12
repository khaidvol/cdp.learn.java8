package com.epam.cdp.m2.hw2.aggregator;

import java.util.*;
import java.util.Map.Entry;

import javafx.util.Pair;

public class Java7Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {

        int total = 0;
        for (int number : numbers) {
            total += number;
        }
        return total;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {

        // store frequency of words in sorted map - key:word, value:frequency
        Map<String, Long> tempMap = new TreeMap<>();
        for(String word: words) {
            Long frequencyCounter = tempMap.get(word);
            tempMap.put(word, frequencyCounter == null ? 1 : frequencyCounter + 1);
        }

        List<Pair<String, Long>> frequencyList = new ArrayList<>();
        Entry<String, Long> mostFrequentPair = null;

        while (frequencyList.size() != limit && !tempMap.isEmpty()) {
            for(Entry<String, Long> tempEntry : tempMap.entrySet()) {
                if(mostFrequentPair == null || tempEntry.getValue() > mostFrequentPair.getValue()) {
                    mostFrequentPair = tempEntry;
                }
            }
            frequencyList.add(new Pair<>(mostFrequentPair.getKey(), mostFrequentPair.getValue()));
            tempMap.remove(mostFrequentPair.getKey());
            mostFrequentPair = null;
        }

        return frequencyList;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {

        //sort words by alphabet and then by length
        Collections.sort(words);
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length()- o2.length();
            }
        });

        //find duplicates in sorted words and store in the list
        String previousWord = "";
        List<String> duplicatesList = new ArrayList<>();

        for(String word: words) {
            word = word.toUpperCase();
            if(word.equals(previousWord) && !duplicatesList.contains(word)) duplicatesList.add(word);
            if(duplicatesList.size() == limit) break;
            previousWord = word;
        }
        return duplicatesList;
    }
}
