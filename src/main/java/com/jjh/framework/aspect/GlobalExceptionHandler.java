package com.jjh.framework.aspect;

import com.jjh.common.exception.BusinessException;
import com.jjh.common.web.form.SimpleResponseForm;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 全局接口异常处理
 * https://blog.csdn.net/qiuqiu_qiuqiu123/article/details/78489619
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     *  权限异常处理（比如无访问权限）
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public SimpleResponseForm<Object> unauthorizedException(UnauthorizedException exception){
        return SimpleResponseForm.error(400, "权限异常，您当前没有访问权限。"+exception.getMessage());
    }

    /**
     *  数据保存异常（比如唯一约束）
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    @ResponseBody
//    public SimpleResponseForm<Object> dataIntegrityViolationException(DataIntegrityViolationException exception){
//        return SimpleResponseForm.error(400, "业务异常，SQL执行错误："+exception.getMessage());
//    }

    /**
     *  校验错误拦截处理（比如@NotBlank等）
     *  https://blog.csdn.net/jytxioabai/article/details/83823486
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public SimpleResponseForm<Object> methodArgumentNotValidException(MethodArgumentNotValidException exception){
        return validBindingResult(exception.getBindingResult());
    }

    /**
     *  校验错误拦截处理（比如@NotBlank等）
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public SimpleResponseForm<Object> bindException(BindException exception){
        return validBindingResult(exception.getBindingResult());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exceptionHandler(Exception e) {
        if (e instanceof BusinessException) {
            return SimpleResponseForm.error(400, "业务异常："+e.getMessage());
        }
        logger.error("API Exception",e);
        return SimpleResponseForm.error(500,"服务器异常："+e.getMessage());
    }


    /**
     * 获取校验结果中的错误信息
     * @param result    校验结果
     * @return
     */
    public static SimpleResponseForm<Object> validBindingResult(BindingResult result) {
        StringBuilder msg = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p ->{
                FieldError fieldError = (FieldError) p;
                msg.append(fieldError.getDefaultMessage()).append(";");
//                logger.error("数据校验失败 : object{"+fieldError.getObjectName()+"},field{"+fieldError.getField()+ "},errorMessage{"+fieldError.getDefaultMessage()+"}");
            });
        }
        return SimpleResponseForm.error(400, "请填写正确信息：" + msg.toString());
    }
}
