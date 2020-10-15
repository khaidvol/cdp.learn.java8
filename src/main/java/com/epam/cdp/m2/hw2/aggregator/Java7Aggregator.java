package com.epam.cdp.m2.hw2.aggregator;

import java.util.*;
import java.util.Map.Entry;

import javafx.util.Pair;

import javax.print.attribute.HashAttributeSet;

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

        // store frequency of words in map - key: word, value: frequency
        Map<String, Long> tempMap = new HashMap<>();
        for(String word: words) {
            Long frequencyCounter = tempMap.get(word);
            tempMap.put(word, frequencyCounter == null ? 1 : frequencyCounter + 1);
        }

        List<Pair<String, Long>> frequencyList = new ArrayList<>();
        List<Entry<String, Long>> tempList = new ArrayList<>();

        tempList.addAll(tempMap.entrySet());

        tempList.sort(Entry.comparingByKey());
        tempList.sort(Entry.comparingByValue(Comparator.reverseOrder()));

        for(Entry <String, Long> tempEntry: tempList) {
            frequencyList.add(new Pair<>(tempEntry.getKey(), tempEntry.getValue()));
        }

        if(!frequencyList.isEmpty() && frequencyList.size() > limit) {
            frequencyList = frequencyList.subList(0, (int) limit);
        }

        return frequencyList;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {

        Map<String, Long> tempMap = new HashMap<>();
        List<String> duplicatesList = new ArrayList<>();

        for(String word: words) {
            word = word.toUpperCase();
            Long frequencyCounter = tempMap.get(word);
            tempMap.put(word, frequencyCounter == null ? 1 : frequencyCounter + 1);
        }

        for (String key: tempMap.keySet()) {
            if(tempMap.get(key) > 1) duplicatesList.add(key);
        }

        Collections.sort(duplicatesList);
        Collections.sort(duplicatesList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length()- o2.length();
            }
        });

        if(!duplicatesList.isEmpty() && duplicatesList.size() > limit){
            duplicatesList = duplicatesList.subList(0, (int) limit);
        }

        return  duplicatesList;
    }
}
