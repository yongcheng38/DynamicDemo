package com.kanq.demo.config.aspect;

import com.kanq.demo.config.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 多数据源处理
 * 
 * @author mi
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    @Pointcut("@annotation(com.kanq.demo.config.aspect.DataSourceAnnotation)"
            + "|| @within(com.kanq.demo.config.aspect.DataSourceAnnotation)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSourceAnnotation dataSource = getDataSource(point);

        if (!StringUtils.isEmpty(dataSource)) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().getDataSourceType());
        }

        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSourceAnnotation getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<? extends Object> targetClass = point.getTarget().getClass();
        DataSourceAnnotation targetDataSource = targetClass.getAnnotation(DataSourceAnnotation.class);
        if (!StringUtils.isEmpty(targetDataSource)) {
            return targetDataSource;
        }
        else {
            Method method = signature.getMethod();
            DataSourceAnnotation dataSource = method.getAnnotation(DataSourceAnnotation.class);
            return dataSource;
        }
    }
}
