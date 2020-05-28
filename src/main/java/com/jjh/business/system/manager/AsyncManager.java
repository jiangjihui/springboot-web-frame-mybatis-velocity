package com.jjh.business.system.manager;

import cn.hutool.extra.spring.SpringUtil;
import com.jjh.common.util.Threads;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理
 *
 * @author ruoyi
 * @date 2020/04/23
 */
public class AsyncManager {

    /**
     * 操作延迟时间 10ms
     */
    private final int OPERATE_DELAY_TIME = 10;

    private ScheduledExecutorService executor = SpringUtil.getBean("scheduledExecutorService");


    /**
     * 单例模式
     */
    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me()
    {
        return me;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task)
    {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(sra, true);
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown()
    {
        Threads.shutdownAndAwaitTermination(executor);
    }

}
