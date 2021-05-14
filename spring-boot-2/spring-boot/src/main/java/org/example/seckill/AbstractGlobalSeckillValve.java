package org.example.seckill;

/**
 * 全局的秒杀阀门, 所有链路都必须使用这个阀门, 将公共的阀门抽取是为了减少 管道公共阀门 对象的创建
 *
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public abstract class AbstractGlobalSeckillValve implements SeckillValve {
    @Override
    public void seckill(Seckill seckill) {
       seckillRequired(seckill);
    }

    /**
     *
     *
     * @param seckill
     */
    public abstract void seckillRequired(Seckill seckill);
}
