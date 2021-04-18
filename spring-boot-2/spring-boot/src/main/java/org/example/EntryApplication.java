package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringBoot Entry.
 *
 * versus 相对 有冲突
 *
 * 1. 如果统一对象如何显式的声明两个名字： 在通过 AnnotatedBeanNameGenerator 中就会校验失败.
 *
 *
 *
 * 注解是java的一种标记系统, 可以在运行时动态的根据 这些标记 执行相应的逻辑
 * 简单 易扩展  非常舒服, 并且 可以在spring中可以看出来 注解的作用被发挥的淋漓尽致
 *
 * springboot的易用性 与 spring 强大的注解系统 密切相关;
 *
 * @author: jinyun
 * @date: 2021/2/8
 */
@Description("整个应用核心启动类")
@RestController
@SpringBootApplication
public class EntryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntryApplication.class, args);
    }
}
