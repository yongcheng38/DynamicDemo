package com.kanq.demo.config.aspect;

import java.lang.annotation.*;

/**
 * @author yyc
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
  String description() default "";

  String system() default "";
}
