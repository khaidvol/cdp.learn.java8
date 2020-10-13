package com.epam.cdp.m2.hw2.aggregator.performance;


import com.epam.cdp.m2.hw2.aggregator.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaAggregatorPerformanceTest {

    private static final int SMALL_DATA_SET_AMOUNT = 1_000;
    private static final int LARGE_DATA_SET_AMOUNT = 1_000_000;
    private static final long LIMIT_SMALL_LIST = 10;
    private static final long LIMIT_LARGE_LIST = 1_000;

    private static Aggregator java7Aggregator;
    private static Aggregator java7ParallelAggregator;
    private static Aggregator java8Aggregator;
    private static Aggregator java8ParallelAggregator;

    private static List<Integer> numbersSmallList;
    private static List<Integer> numbersLargeList;
    private static List<String> wordsSmallList;
    private static List<String> wordsLargeList;


    @BeforeClass
    public static void setUp(){
        java7Aggregator = new Java7Aggregator();
        java7ParallelAggregator = new Java7ParallelAggregator();
        java8Aggregator = new Java8Aggregator();
        java8ParallelAggregator = new Java8ParallelAggregator();

        numbersSmallList = intDataSetInitialization(SMALL_DATA_SET_AMOUNT);
        numbersLargeList = intDataSetInitialization(LARGE_DATA_SET_AMOUNT);

        wordsSmallList = strDataSetInitialization(SMALL_DATA_SET_AMOUNT);
        wordsLargeList = strDataSetInitialization(LARGE_DATA_SET_AMOUNT);
    }

    @Test
    public void sumPerformanceSmallDataSetTest(){
        System.out.println("\nsum() execution with small (" + SMALL_DATA_SET_AMOUNT + " elements) data set");
        sumPerformance(java8Aggregator, numbersSmallList);
        sumPerformance(java8ParallelAggregator, numbersSmallList);
        sumPerformance(java7Aggregator, numbersSmallList);
        sumPerformance(java7ParallelAggregator, numbersSmallList);
    }

    @Test
    public void sumPerformanceLargeDataSetTest(){
        System.out.println("\nsum() execution with large (" + LARGE_DATA_SET_AMOUNT + " elements) data set");
        sumPerformance(java8Aggregator, numbersLargeList);
        sumPerformance(java8ParallelAggregator, numbersLargeList);
        sumPerformance(java7Aggregator, numbersLargeList);
        sumPerformance(java7ParallelAggregator, numbersLargeList);
    }

    @Test
    public void frequencyPerformanceSmallDataSetTest(){
        System.out.println("\ngetMostFrequentWords() execution with small (" + SMALL_DATA_SET_AMOUNT + " elements) data set");
        frequencyPerformance(java8Aggregator, wordsSmallList, LIMIT_SMALL_LIST);
        frequencyPerformance(java8ParallelAggregator, wordsSmallList, LIMIT_SMALL_LIST);
        frequencyPerformance(java7Aggregator, wordsSmallList, LIMIT_SMALL_LIST);
//        frequencyPerformance(java7ParallelAggregator, wordsSmallList, smallListLimit);

    }

    @Test
    public void frequencyPerformanceLargeDataSetTest(){
        System.out.println("\ngetMostFrequentWords() execution with large (" + LARGE_DATA_SET_AMOUNT + " elements) data set");
        frequencyPerformance(java8Aggregator, wordsLargeList, LIMIT_LARGE_LIST);
        frequencyPerformance(java8ParallelAggregator, wordsLargeList, LIMIT_LARGE_LIST);
        frequencyPerformance(java7Aggregator, wordsLargeList, LIMIT_LARGE_LIST);
//        frequencyPerformance(java7ParallelAggregator, wordsLargeList, largeListLimit);

    }

    @Test
    public void duplicatesPerformanceSmallDataSetTest(){
        System.out.println("\ngetDuplicates() execution with small (" + SMALL_DATA_SET_AMOUNT + " elements) data set");
        duplicatesPerformance(java8Aggregator, wordsSmallList, LIMIT_SMALL_LIST);
        duplicatesPerformance(java8ParallelAggregator, wordsSmallList, LIMIT_SMALL_LIST);
        duplicatesPerformance(java7Aggregator, wordsSmallList, LIMIT_SMALL_LIST);
//        duplicatesPerformance(java7ParallelAggregator, wordsSmallList, smallListLimit);

    }

    @Test
    public void duplicatesPerformanceLargeDataSetTest(){
        System.out.println("\ngetDuplicates() execution with large (" + LARGE_DATA_SET_AMOUNT + " elements) data set");
        duplicatesPerformance(java8Aggregator, wordsLargeList, LIMIT_LARGE_LIST);
        duplicatesPerformance(java8ParallelAggregator, wordsLargeList, LIMIT_LARGE_LIST);
        duplicatesPerformance(java7Aggregator, wordsLargeList, LIMIT_LARGE_LIST);
//        duplicatesPerformance(java7ParallelAggregator, wordsLargeList, largeListLimit);

    }




    private static void sumPerformance(Aggregator aggregator, List<Integer> numbers){
        long start = System.currentTimeMillis();
        aggregator.sum(numbers);
        long delta = System.currentTimeMillis() - start;
        System.out.println("Execution: " + delta + " ms " + aggregator.getClass().getSimpleName());
    }

    private static void frequencyPerformance(Aggregator aggregator, List<String> words, long limit){
        long start = System.currentTimeMillis();
        aggregator.getMostFrequentWords(words, limit);
        long delta = System.currentTimeMillis() - start;
        System.out.println("Execution: " + delta + " ms " + aggregator.getClass().getSimpleName());
    }

    private static void duplicatesPerformance(Aggregator aggregator, List<String> words, long limit){
        long start = System.currentTimeMillis();
        aggregator.getDuplicates(words, limit);
        long delta = System.currentTimeMillis() - start;
        System.out.println("Execution: " + delta + " ms " + aggregator.getClass().getSimpleName());
    }



    private static List<Integer> intDataSetInitialization(int amount) {
        List<Integer> dataSet = new ArrayList<>();
        Random rm = new Random();
        for(int i = 0; i < amount; i++) {
            dataSet.add(rm.nextInt(1000 + 500) - 500);
        }
        return dataSet;
    }

    private static List<String> strDataSetInitialization(int amount) {
        List<String> dataSet = new ArrayList<>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rm = new Random();
        for(int i = 0; i < amount; i++) {
            StringBuilder sb = new StringBuilder();
            int length = rm.nextInt(7);
            for(int n = 0; n < length; n++){
                int index = rm.nextInt(alphabet.length());
                char randomChar = alphabet.charAt(index);
                sb.append(randomChar);
            }
            String randomString = sb.toString();
            dataSet.add(randomString);
        }
        return dataSet;
    }
}
