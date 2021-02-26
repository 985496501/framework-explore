package org.example.loadbalanced;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
