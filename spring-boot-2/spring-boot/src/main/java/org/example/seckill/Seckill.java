package org.example.seckill;

/**
 * 描述秒杀行为 其中有 用户id 商品skuid
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public interface Seckill {
    /**
     * 用户id
     *
     * @return id
     */
    Long userId();

    /**
     * sku id
     *
     * @return 商品库存id
     */
    Long skuId();
}
