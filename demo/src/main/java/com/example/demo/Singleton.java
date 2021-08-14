package com.example.demo;

import com.sun.xml.internal.ws.util.CompletedFuture;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Singleton {

    private static /*volatile*/ Singleton singleton = null;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if(null == singleton) {
            synchronized (Singleton.class) {
                if(null == singleton) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }


    public static void main(String[] args) {

        Executor executor = initExecutor();
        /*for (int i = 0; i < 50; i++) {
            int finalI = i;
            executor.execute(() -> {

                try {
                    Singleton singleton = Singleton.getInstance();
                    System.out.println(finalI + "i========" + singleton);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }*/
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        List<CompletableFuture<Integer>> s = list.stream().map(v->getRemark(v)).collect(Collectors.toList());
        s.stream().forEach(v->{
            v.thenApply((result)->{
                System.out.println("thenApply res" +result);
                return result;
            });
        });
        //使用allof 获得返回值
        CompletableFuture cf = CompletableFuture.allOf(s.toArray(new CompletableFuture[s.size()]));

        try {
            System.out.println("date1" + new Date());

            CompletableFuture<List<Integer>> ssss = cf.thenApply(v-> s.stream().map(mm ->mm.join()).collect(Collectors.toList()));
            System.out.println("date2" + new Date());

            List<Integer> lista = ssss.get();
            System.out.println("date3" + new Date());

            lista.stream().forEach(v->{
                System.out.println(v);
            });
        } catch (Exception e) {

        }

    }

    public static CompletableFuture<Integer> getRemark(Integer v) {
        Executor executor = initExecutor();

        CompletableFuture<Integer> completedFuture = CompletableFuture.supplyAsync(()-> {
            System.out.println("开始date" + new Date());
            try {
                Thread.sleep(30000);

            } catch(Exception e ) {

            }
                    System.out.println("threadName" + Thread.currentThread().getName());
            System.out.println("date" + new Date());
                    System.out.println("v is " + v);
                    System.out.println("supplyAsync 是否为守护线程 " + Thread.currentThread().isDaemon());
            return v;
        } , executor);

        return completedFuture;
    }


    public static Executor initExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程池大小
        executor.setCorePoolSize(10);
        // 最大线程数
        executor.setMaxPoolSize(50);
        // 队列容量
        executor.setQueueCapacity(20);
        // 活跃时间
        executor.setKeepAliveSeconds(30);
        // 线程名字前缀
        executor.setThreadNamePrefix("excel.executor-");

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }



}