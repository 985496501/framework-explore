package org.example.seckill;

import org.springframework.core.Ordered;

/**
 * 每一个valve实例对象充当一道 检验门槛, 实现
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public interface SeckillValve extends Ordered {
    /**
     * 判断是否具有抢购权限
     *
     * @param seckill
     */
    void seckill(Seckill seckill);

    /**
     * 用于排序 返回值越小 优先级越大
     *
     * @return Integer.MIN -- Integer.MAX
     */
    @Override
    int getOrder();
}
