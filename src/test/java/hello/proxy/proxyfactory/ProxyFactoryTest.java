package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceInterface;
import hello.proxy.common.service.ServiceInterfaceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스 있으면 JDK 동적 프록시 사용 테스트")
    void interfaceProxy() {
        // 인터페이스 구현 객체 생성한다.
        ServiceInterface serviceInterface = new ServiceInterfaceImpl();
        // 구현 객체를 파라미터로 넣은 프록시 팩토리 객체 생성한다.
        ProxyFactory proxyFactory = new ProxyFactory(serviceInterface);
        // 어드바이스 추가한다.
        proxyFactory.addAdvice(new TimeAdvice());
        // 프록시 객체 생성한다.
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}", serviceInterface.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.save();
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구현 클래스만 있으면 CGLIB 사용 테스트")
    void concreteProxy() {
        // 구현 클래스 객체 생성한다.
        ConcreteService concreteService = new ConcreteService();
        // 구현 객체를 파라미터로 넣은 프록시 팩토리 객체 생성한다.
        ProxyFactory proxyFactory = new ProxyFactory(concreteService);
        // 어드바이스 추가한다.
        proxyFactory.addAdvice(new TimeAdvice());
        // 프록시 객체 생성한다.
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();

        log.info("targetClass = {}", concreteService.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.call();
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 사용, 클래스 기반 프록시 테스트")
    void proxyTargetClass() {
        // 인터페이스 구현 객체 생성한다.
        ServiceInterface serviceInterface = new ServiceInterfaceImpl();
        // 구현 객체를 파라미터로 넣은 프록시 팩토리 객체 생성한다.
        ProxyFactory proxyFactory = new ProxyFactory(serviceInterface);
        // 인터페이스가 있어도 타켓 클래스만 바라봐서 CGLIB 기반으로 사용한다.
        proxyFactory.setProxyTargetClass(true);
        // 어드바이스 추가한다
        proxyFactory.addAdvice(new TimeAdvice());
        // 프록시 객체 생성한다.
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass = {}", serviceInterface.getClass());
        log.info("proxyClass = {}", proxy.getClass());

        proxy.save();
        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
