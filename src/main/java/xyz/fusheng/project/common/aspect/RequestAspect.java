package xyz.fusheng.project.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @FileName: RequestAspect
 * @Author: code-fusheng
 * @Date: 2022/3/31 23:00
 * @Version: 1.0
 * @Description: 请求切面
 * 切面输出基本信息以及记录日志
 * 0、@pointcut       配置切入点
 * 1、@Around         环绕通知 - 在目标方法执行前后实施增强，可以用于日志，事务管理等功能
 * 2、@Before         前置通知 - 在目标方法执行前实施增强，可以应用于权限管理功能
 * 3、@AfterReturning 后置通知 - 在目标方法执行后实施增强，可以应用于关闭流、上传文件、删除临时文件等功能
 * 4、@AfterThrowing  异常抛出通知 - 在方法抛出异常后实施增强，可以应用于处理异常记录日志等功能
 * 5、@DeclareParents 引介通知 - 在目标类中添加一些新的方法和属性，可以应用于修改老版本程序
 * 6、@After          最终通知
 */

@Aspect
@Component
public class RequestAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestAspect.class);


    /**
     * @Pointcut 切点 指定那些文件需要 AOP
     * execution 切入点表达式 - 两个..代表所有子目录，最后括号里的两个..代表所有参数
     */
    @Pointcut("execution( * xyz.fusheng.*.controller..*(..))")
    public void basePointCut() {
    }

    /**
     * 前置通知
     * 1. 方法执行之前调用在目标方法执行前后实施增强
     * 2. 可以应用于权限管理等功能
     * 3. 此处用于记录请求的参数和请求内容
     */
    @Before("basePointCut()")
    public void doBefore(JoinPoint joinPoint) throws Exception {
        // logger.info("[AOP切面日志-前置通知]");
    }

    /**
     * 环绕通知
     * 在目标方法执行前后实施增强，可以应用于日志，事务等功能 PS: 在前置通知之前
     * pjp 是 JoinPoint 的子接口，表示可以执行目标方法
     * 1. 必须是Object类型的返回值
     * 2. 必须要接收一个参数
     * 3. 必须使用 throw Throwable
     */
    @Around("basePointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // logger.info("[AOP切面日志-环绕通知]");
        // pjp.proceed() 执行目标方法 可以理解为对业务方法的模拟
        Object ob = pjp.proceed();
        return ob;
    }

    /**
     * 后置通知 没有发生异常 （在发生异常的情况下不执行此通知）
     * 会直接获取对应切面方法的返回值，可以对这个返回值进行进一步处理（不能改变）
     * 此处用于返回日志请求结果 LogResult
     */
    @AfterReturning(returning = "ret", pointcut = "basePointCut()")
    public void doAfterReturning(Object ret) {
        // logger.info("[AOP切面日志-后置通知]");
    }

    /**
     * 异常通知 处理程序中未处理的异常
     * 发生异常
     */
    @AfterThrowing(pointcut = "basePointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        // logger.info("[AOP切面日志-异常通知]");
    }

}
