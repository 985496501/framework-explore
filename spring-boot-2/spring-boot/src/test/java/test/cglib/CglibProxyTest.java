package test.cglib;

import org.junit.Test;
import org.springframework.cglib.proxy.Proxy;

/**
 * @author: jinyun
 * @date: 2021/4/26
 */
public class CglibProxyTest {

    @Test
    public void proxyTest() {
//        Proxy.newProxyInstance()
        Class proxyClass = Proxy.getProxyClass(ClassLoader.getSystemClassLoader(), new Class[]{ServiceImpl.class});
        assert proxyClass != null;
    }


    static class ServiceImpl {
        public String getStr() {
            return "hello world";
        }
    }
}
