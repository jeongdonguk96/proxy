package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        log.info("TimeProxy 실행");
        long start = System.currentTimeMillis();

        Object result = methodProxy.invoke(target, args);

        long end = System.currentTimeMillis();
        long resultTIme = start - end;
        log.info("TimeProxy 종료 resultTime = {}", resultTIme);
        return result;
    }
}
