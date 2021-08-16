package com.example.demo;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;


public class TreadPool {

    private static volatile TreadPool singleton = null;

    private TreadPool() {

    }

    public static TreadPool getInstance() {
        if(null == singleton) {
            synchronized (TreadPool.class) {
                if(null == singleton) {
                    singleton = new TreadPool();
                }
            }
        }
        return singleton;
    }


    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        List<List<Integer>> aList = Lists.partition(list, 2);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
        List<Future<Integer>> futures =  aList.stream().map(v->{
            Future<Integer> future = executor.submit(new GetInteger(v));
            return future;
        }).collect(Collectors.toList());

        Integer index =0;
        futures.stream().forEach(v->{
                try {
                    Integer s = v.get();
                    System.out.println(s + "=  ssss");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        index++;
    }

    private static class GetInteger implements Callable<Integer> {
        private List<Integer> list;

        private GetInteger(List<Integer> list) {
            this.list = list;
        }

        @Override
        public Integer call() {
            if(CollectionUtils.isEmpty(list)) {
                System.out.println("空的list");
                return null;
            }
            System.out.println("线程开始");

            list.stream().forEach(v->{
                System.out.println(v);

            });
            System.out.println("线程结束");
            return null;

        }
    }




}