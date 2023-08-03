package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        log.info("start");
        String result1 = target.callA();
        log.info("result = {}", result1);

        log.info("start");
        String result2 = target.callB();
        log.info("result = {}", result2);
    }

    @Test
    void reflection1() throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        // 경로로 클래스의 메타정보를 가져와 리플렉션 객체를 생성한다.
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        // 실제 객체를 생성한다.
        Hello target = new Hello();

        // 리픒렉션 객체에서 매서드 정보를 가져온다.
        Method methodCallA = classHello.getMethod("callA");
        // 매서드가 호출될 객체를 파라미터로 넣는다.
        // 호출의 결과를 Object로 반환한다.
        Object result1 = methodCallA.invoke(target);
        log.info("result = {}", result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result = {}", result2);
    }

    @Test
    void reflection2() throws Exception {

        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();

        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    // 동적으로 리플렉션 매서드와 실제 객체를 받아 사용한다.
    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        Object result = method.invoke(target);
        log.info("result = {}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }


}
