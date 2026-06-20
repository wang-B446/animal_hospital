package com.animal.config;

import com.animal.entity.OperationLog;
import com.animal.service.OperationLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogService operationLogService;

    // 拦截所有 Controller 的增删改操作
    @Pointcut("(execution(* com.animal.controller..*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)) || " +
              "(execution(* com.animal.controller..*(..)) && @annotation(org.springframework.web.bind.annotation.PutMapping)) || " +
              "(execution(* com.animal.controller..*(..)) && @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        String operation = "";
        try {
            result = point.proceed();
            operation = "成功";
        } catch (Throwable e) {
            operation = "失败:" + e.getMessage();
            throw e;
        } finally {
            try {
                OperationLog log = new OperationLog();
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
                log.setModule(point.getTarget().getClass().getSimpleName().replace("Controller", ""));
                log.setMethod(point.getSignature().getName());
                log.setOperation(operation);
                if (point.getArgs() != null && point.getArgs().length > 0) {
                    ObjectMapper mapper = new ObjectMapper();
                    log.setParams(mapper.writeValueAsString(point.getArgs()[0]));
                }
                log.setIp(request != null ? request.getRemoteAddr() : "-");
                log.setUsername(request != null ? request.getHeader("username") : "system");
                log.setCreateTime(LocalDateTime.now());
                operationLogService.save(log);
            } catch (Exception ignored) {}
        }
        return result;
    }
}
