package ru.juriasan.threading;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class LoadBalancer {

    private ExecutorService pool;
    public static final int MAXIMUM_POOL_SIZE = 10;
    public static final int MINIMUM_POOL_SIZE = 1;

    private volatile static LoadBalancer instance;

    public static LoadBalancer getInstance(int n) {
        if (instance == null) {
            synchronized (LoadBalancer.class) {
                if (instance == null)
                    instance = new LoadBalancer(n);
            }
        }
        return instance;
    }

    private LoadBalancer(int n) {
        this.pool = Executors.newFixedThreadPool(n > MAXIMUM_POOL_SIZE ? MAXIMUM_POOL_SIZE :
                n < MINIMUM_POOL_SIZE ? MINIMUM_POOL_SIZE : n);
    }

    public void submit(Runnable task) {
        this.pool.submit(task);
    }
}
