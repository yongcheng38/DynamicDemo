package com.kanq.demo.config.aspect;

import java.lang.annotation.*;

/**
 * 自定义多数据源切换注解
 * 
 * @author mi
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSourceAnnotation
{
    /**
     * 切换数据源名称
     */
    DataSourceEnum value() default  DataSourceEnum.MASTER;
}
