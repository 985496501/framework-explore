package org.example.loadbalanced;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡算法
 * 在 loadBalanced 的默认使用中使用
 * 如果让你在  机器性能硬件都相同的情况下 处理业务的能力都相识 就可以使用 轮询算法
 *
 *
 * @author: jinyun
 * @date: 2021/2/26
 */
public class RoundRobinTest {
    // 首先我们需要定义的数据的存储结构

    private AtomicInteger i = new AtomicInteger(-1);

    public int get() {
        return Math.abs(i.incrementAndGet()) % 4;
    }

    /**
     * loadBalancer 就是根据服务的列表, 然后一直随机取, 想这个底层直接使用数组实现即可。
     */
    @Test
    public void testTest() {
        for (int i1 = 0; i1 < 10; i1++) {
            System.out.println(get());
        }
    }






    /**
     * k: service name
     *
     * v: service name 相同的抽象的服务列表
     */
    private final Map<String, List<Service>> serviceMap = new HashMap<>();

    public void addServiceInstance(Service service) {
        if (serviceMap.get(service.getName()) == null) {
//            List<Service> objects = new Concur<>();
        }
    }




}
