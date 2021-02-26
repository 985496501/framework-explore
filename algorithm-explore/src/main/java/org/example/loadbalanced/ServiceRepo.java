package org.example.loadbalanced;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: jinyun
 * @date: 2021/2/26
 */
public class ServiceRepo {
    // 我们定义的轮询服务, 可以子轮询，那么我们可以使用   有一知识点  就是如何快速定义环形数据结构，环形链表
    private final List<Service> services = new LinkedList<>();

}
