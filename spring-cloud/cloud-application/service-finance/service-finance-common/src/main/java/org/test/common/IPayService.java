package org.test.common;

import javax.servlet.http.HttpServletRequest;

/**
 * 这个接口的目的在于统一聚合支付
 * 提供统一的支付能力 和 退款能力
 * 共其他服务使用  使用策略模式实现
 *
 * @author: Liu Jinyun
 * @date: 2021/4/17/23:09
 */
public interface IPayService {

    /**
     * 统一发起支付的接口
     *
     * @param httpRequest Fixme: httpRequest 这个参数看是否可以去掉
     * @param payRequest  payRequest
     * @return 前端调起支付的参数
     */
    Object pay(HttpServletRequest httpRequest, IPayRequest payRequest);
}
