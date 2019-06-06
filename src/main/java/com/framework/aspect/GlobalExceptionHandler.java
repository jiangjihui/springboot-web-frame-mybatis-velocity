package com.framework.aspect;

import com.common.web.form.SimpleResponseForm;
import com.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局接口异常处理
 * https://blog.csdn.net/qiuqiu_qiuqiu123/article/details/78489619
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exceptionHandler(Exception e) {
        if (e instanceof BusinessException) {
            return SimpleResponseForm.error(400, "业务异常："+e.getMessage());
        }
        logger.error("API Exception",e);
        return SimpleResponseForm.error(500,"服务器异常："+e.getMessage());
    }

}
