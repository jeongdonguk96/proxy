package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceInterface;
import hello.proxy.common.service.ServiceInterfaceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class MultiAdvisorTest {

    @Test
    @DisplayName("여러 프록시")
    void multiAdvisorTest1() {
        // 타겟 클래스 생성
        ServiceInterface target = new ServiceInterfaceImpl();

        // 프록시 팩토리1 생성
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        // 포인트컷과 어드바이스로 어드바이저1 생성
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        // 어드바이저1 추가
        proxyFactory1.addAdvisor(advisor1);
        // 프록시1 생성
        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        // 프록시 팩토리2 생성
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        // 포인트컷과 어드바이스로 어드바이저2 생성
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        // 어드바이저2 추가
        proxyFactory1.addAdvisor(advisor2);
        // 프록시2 생성
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

        proxy1.save();
        proxy1.find();

        proxy2.save();
        proxy2.find();
    }

    @Test
    @DisplayName("하나의 프록시")
    void multiAdvisorTest2() {
        // 타겟 클래스 생성
        ServiceInterface target = new ServiceInterfaceImpl();
        // 프록시 팩토리 생성
        ProxyFactory proxyFactory = new ProxyFactory(target);
        // 포인트컷과 어드바이스로 어드바이저 생성
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        // 어드바이저 추가
        proxyFactory.addAdvisor(advisor2);
        proxyFactory.addAdvisor(advisor1);
        // 프록시 생성
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
    }

    @Slf4j
    static class Advice1 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }

    @Slf4j
    static class Advice2 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }

}
