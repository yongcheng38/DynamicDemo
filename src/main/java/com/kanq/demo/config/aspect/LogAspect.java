package com.kanq.demo.config.aspect;

import com.kanq.demo.config.thread.AsyncThreadManager;
import com.kanq.demo.entity.MailBean;
import com.kanq.demo.utils.MailUtils;
import com.kanq.demo.utils.SpringBeanUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

/**
 * @Description: 异常日志记录及发送邮件
 * @author yyc
 * @date 2020/7/2410:25
 */
@Aspect
@Component

public class LogAspect {

  @Resource
  private TemplateEngine templateEngine;
  @Resource
  private MailUtils mailUtils;
  /** logger */
  private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

  @Pointcut("@annotation(com.kanq.demo.config.aspect.SystemLog)")
  public void SystemAspect() {}

  /**
   * 异常通知 用于拦截记录异常日志
   *
   * @param joinPoint
   * @param e
   */
  @AfterThrowing(pointcut = "SystemAspect()", throwing = "e")
  public void doAfterThrowing(JoinPoint joinPoint, RuntimeException e) {
    try {
      // 获得注解
      SystemLog annotationLog = getAnnotationLog(joinPoint);
      //类名
      String targetName = joinPoint.getTarget().getClass().getName();
      //方法名
      String methodName = joinPoint.getSignature().getName();
      // 参数名
      String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
      // 参数值：
      final Object[] argValues = joinPoint.getArgs();

      StringBuilder sb = new StringBuilder();
      for (int i = 0,size =argNames.length; i < size; i++) {
        String value = argValues[i] == null ? "null" : argValues[i].toString();
        sb.append(argNames[i]).append("=").append(value).append(",");
      }
      String paramStr = sb.length() > 0 ? sb.toString().substring(0, sb.length() - 1) : "";

      // 服务异常邮件通知
      Context context = new Context();
      context.setVariable("logDescription", annotationLog.description());
      context.setVariable("errorDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      context.setVariable("targetName", targetName);
      context.setVariable("methodName", methodName);
      context.setVariable("params", paramStr);
      context.setVariable("errorInfo", e.getMessage());
      context.setVariable("stackTrace", ArrayUtils.toString(e.getStackTrace()));

      String mail = templateEngine.process("mail.html", context);
      MailBean mailBean = new MailBean();
      mailBean.setRecipient("879907137@qq.com");
      mailBean.setSubject("服务执行异常");
      mailBean.setContent(mail);
      //发送邮件
      mailUtils.sendHTMLMail(mailBean);

      // 异步保存日志到数据库
      AsyncThreadManager.me().execute(new TimerTask() {
        @Override
        public void run() {
          // 调用插入数据库日志方法
          SpringBeanUtil.getBean("");
        }
      });
    } catch (Exception ex) {
      // 记录本地异常日志
      LOGGER.error("==异常通知异常==" + ex.getMessage());
    }
  }

  /** 是否存在注解，如果存在就获取 */
  private SystemLog getAnnotationLog(JoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();

    if (method != null) {
      return method.getAnnotation(SystemLog.class);
    }
    return null;
  }
}
