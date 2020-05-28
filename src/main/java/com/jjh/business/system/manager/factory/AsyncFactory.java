package com.jjh.business.system.manager.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.jjh.business.system.record.model.SysOperationLog;
import com.jjh.business.system.record.service.SysOperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author jjh
 * @date 2020/04/23
 */
public class AsyncFactory
{
    private static final Logger logger = LoggerFactory.getLogger(AsyncFactory.class);

    /**
     * 异步保存操作日志
     *
     * @param sysOperationLog 异步保存操作日志
     * @return
     */
    public static TimerTask asyncSaveOperationLog(final SysOperationLog sysOperationLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtil.getBean(SysOperationLogService.class).add(sysOperationLog);
            }
        };
    }

}
