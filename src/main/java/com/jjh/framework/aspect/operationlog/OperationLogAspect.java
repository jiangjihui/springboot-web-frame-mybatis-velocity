package com.jjh.framework.aspect.operationlog;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jjh.business.system.manager.AsyncManager;
import com.jjh.business.system.manager.factory.AsyncFactory;
import com.jjh.business.system.record.model.SysOperationLog;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.util.TimeUtils;
import com.jjh.common.util.request.GetRequestJsonUtils;
import com.jjh.common.util.request.RequestUtils;
import com.jjh.framework.jwt.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 操作日志记录处理
 * <p>
 * refer ruoyi
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.jjh.framework.aspect.operationlog.Log)")
    public void operationLogPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "operationLogPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "operationLogPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    /**
     * 记录日志
     * @param joinPoint
     * @param e
     * @param jsonResult
     */
    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // 获取当前的用户
            String userId = JwtUtil.getUserId();
            String deptId = JwtUtil.getDeptId();

            // *========数据库日志=========*//
            SysOperationLog sysOperationLog = new SysOperationLog();
            sysOperationLog.setOperTime(TimeUtils.nowDate());
            sysOperationLog.setStatus(BaseConstants.RUN_STATUS_SUCCESS);
            // 请求的地址
            sysOperationLog.setOperIp(RequestUtils.currentRequestIP());
            // 返回参数
            sysOperationLog.setJsonResult(JSONObject.toJSONString(jsonResult));

            sysOperationLog.setOperUrl(RequestUtils.currentRequestUrl());
            if (userId != null) {
                sysOperationLog.setOperUserId(userId);
                if (StrUtil.isNotBlank(deptId)) {
                    sysOperationLog.setDeptId(userId);
                }
            }

            if (e != null) {
                sysOperationLog.setStatus(BaseConstants.RUN_STATUS_FAILED);
                sysOperationLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            sysOperationLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            sysOperationLog.setRequestMethod(RequestUtils.currentRequestMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog, sysOperationLog);
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.asyncSaveOperationLog(sysOperationLog));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(Log log, SysOperationLog operLog) throws Exception {
        // 设置action动作
        operLog.setOperationType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(operLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog
     */
    private void setRequestValue(SysOperationLog operLog) {
        String params = null;
        try {
            params = GetRequestJsonUtils.getRequestJsonString(RequestUtils.getRequest());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            params = e.getMessage();
        }
        operLog.setOperParam(StringUtils.substring(params, 0, 2000));
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
