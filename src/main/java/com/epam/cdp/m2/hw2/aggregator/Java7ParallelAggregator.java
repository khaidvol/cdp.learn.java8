package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Java7ParallelAggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int sum = forkJoinPool.invoke(new SumRecursiveTask(numbers, 0, numbers.size()));
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


    class SumRecursiveTask extends RecursiveTask<Integer> {

        private final int THRESHOLD = 125_000;
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
                for(int i = begin; i < end; i++) {
                    sum+= list.get(i);
                }
                return sum;
            } else {
                int middle = ((end + begin) / 2);
                SumRecursiveTask t1 = new SumRecursiveTask(list, begin, middle);
                SumRecursiveTask t2 = new SumRecursiveTask(list, middle, end);
                t1.fork();
                t2.fork();
                int res = 0;
                res += t1.join();
                res += t2.join();
                return res;
            }
        }
    }
}

