package org.test.common;

/**
 * 支付需要参数的统一接口
 *
 * id我们统一使用雪花算法来生成
 *
 * @author: Liu Jinyun
 * @date: 2021/4/17/23:23
 */
public interface IPayRequest {
    /**
     * 获取用户id
     *
     * @return id
     */
    Long getUserId();

    /**
     * 获取用户名称
     *
     * @return name
     */
    String getUserName();

    /**
     * 获取业务单号
     *
     * @return 业务相关的code
     */
    String getBizCode();


}
