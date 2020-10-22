package com.kanq.demo.config.thread;

import com.kanq.demo.utils.Threads;
import com.kanq.demo.utils.SpringBeanUtil;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 * 
 * @author mi
 */
public class AsyncThreadManager {
    /**
     * 操作延迟10毫秒
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService executor = SpringBeanUtil.getBean("scheduledExecutorService");

    /**
     * 单例模式
     */
    private AsyncThreadManager(){}

    private static AsyncThreadManager me = new AsyncThreadManager();

    public static AsyncThreadManager me() {
        return me;
    }

    /**
     * 执行任务
     * 
     * @param task 任务
     */
    public void execute(TimerTask task) {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        Threads.shutdownAndAwaitTermination(executor);
    }
}
