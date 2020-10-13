package com.epam.cdp.m2.hw2.aggregator;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import javafx.util.Pair;

public class Java7ParallelAggregator implements Aggregator {

    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

    @Override
    public int sum(List<Integer> numbers) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_THREADS);
        int sum = forkJoinPool.invoke(new SumFork(numbers, 0, numbers.size()));
        return sum;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }



    class SumFork extends RecursiveTask<Integer> {
        private final int THRESHOLD = 250_000;
        private List<Integer> list;
        private int begin;
        private int end;

        public SumFork(List<Integer> list, int begin, int end) {
            this.list = list;
            this.begin = begin;
            this.end = end;

        }


        @Override
        protected Integer compute() {
            final int size = end - begin;
            if (size < THRESHOLD) {
                int sum = 0;
                for(int i = begin; i < end; i++) {
                    sum+= list.get(i);
                }
                return sum;
            } else {
                final int middle = ((end + begin) / 2);
                SumFork sum1 = new SumFork(list, begin, middle);
                sum1.fork();
                SumFork sum2 = new SumFork(list, middle, end);
                int sum =  sum2.compute() + sum1.join();
                return sum;
            }
        }
    }
}

