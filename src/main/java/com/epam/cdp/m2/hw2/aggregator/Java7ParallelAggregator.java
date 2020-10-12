package com.epam.cdp.m2.hw2.aggregator;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import javafx.util.Pair;

public class Java7ParallelAggregator implements Aggregator {

    static int numOfThreads = Runtime.getRuntime().availableProcessors();

    @Override
    public int sum(List<Integer> numbers) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(numOfThreads);
        SumFork sumFork = new SumFork(numbers, 0, numbers.size());
        int sum = forkJoinPool.invoke(sumFork);
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
        static final int THRESHOLD = 4;
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
                final int middle = begin + ((end - begin) / 2);
                SumFork sum1 = new SumFork(list, begin, middle);
                sum1.fork();
                SumFork sum2 = new SumFork(list, middle, end);
                int sum = sum1.join() + sum2.compute();
                return sum;
            }
        }
    }
}

