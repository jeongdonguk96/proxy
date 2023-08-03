package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        // 실제 객체를 생성한다.
        AInterface target = new AImpl();
        // 실제 객체를 파라미터로 넘기고 핸들러 생성한다.
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 클래스 로더, 인터페이스, 핸들러 메타정보를 토대로 프록시 객체를 생성한다.
        AInterface proxy =
                (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);

        // 프록시 객체가 로직을 수행한다.
        proxy.call();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        AInterface proxy =
                (AInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();

        log.info("targetClass = {}", target.getClass());
        log.info("proxyClass = {}", proxy.getClass());
    }

}
