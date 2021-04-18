package org.test.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.test.common.IType;

/**
 * 支付类型枚举
 *
 * @author: Liu Jinyun
 * @date: 2021/4/17/23:37
 */
@Getter
@RequiredArgsConstructor
public enum PayTypeEnum implements IType {
    /**
     * 这里仅仅列举一个简单的例子 支付订单
     */
    WECHAT(1, "微信支付"),
    ALiPAY(2, "支付宝")
    ;

    private final int type;
    private final String desc;

}
