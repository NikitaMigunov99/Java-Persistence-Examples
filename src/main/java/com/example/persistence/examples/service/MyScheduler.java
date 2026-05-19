package com.example.persistence.examples.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@Service
public class MyScheduler {


    private final Logger logger = LoggerFactory.getLogger(MyScheduler.class);


    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
//    @Async
    public void run() throws InterruptedException {
        LocalTime time = LocalTime.now();
        logger.info("Hello from scheduler 1 {} thread {}", time, Thread.currentThread().getName());
        Thread.sleep(1000);
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
//    @Async
    public void runNew() throws InterruptedException {
        LocalTime time = LocalTime.now();
        logger.info("Hello from scheduler 2 {} thread {}", time, Thread.currentThread().getName());
        Thread.sleep(1000);
    }
}
