package com.example.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singleton {

    private static volatile Singleton singleton = null;

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
        Singleton singleton = Singleton.getInstance();

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }



}