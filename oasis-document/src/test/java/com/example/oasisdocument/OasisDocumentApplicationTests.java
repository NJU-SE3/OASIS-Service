package com.example.oasisdocument;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@SpringBootTest
public class OasisDocumentApplicationTests {

    private class MyProxy implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) {

            return null;
        }
    }
    @Test
    public void contextLoads() {
    }

}
