package org.test.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.test.common.IType;

/**
 * 业务类型枚举
 *
 * @author: Liu Jinyun
 * @date: 2021/4/17/23:31
 */
@Getter
@RequiredArgsConstructor
public enum BizTypeEnum implements IType {
    /**
     * 这里仅仅列举一个简单的例子 支付订单
     */
    ORDER(1, "普通订单"),

    ;

    private final int type;
    private final String desc;
}
