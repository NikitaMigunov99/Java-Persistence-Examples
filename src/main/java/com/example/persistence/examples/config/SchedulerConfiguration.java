package com.example.persistence.examples.config;

import jakarta.persistence.EntityManager;
import jakarta.servlet.Filter;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Configuration
//@EnableAsync
public class SchedulerConfiguration implements AsyncConfigurer {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolTaskExecutor.class);

    ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 42);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private int data = 0;

    public int read() {
        getClass().getDeclaredFields();
        lock.readLock().lock();
        try {
            return data;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(2);
        executor.setThreadNamePrefix("MyAsyncThread-");
//        executor.setTaskDecorator(runnable -> () -> {
//            logger.info("Task started");
//            try {
//                runnable.run();
//            } finally {
//                logger.info("Task finished");
//            }
//        });
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}