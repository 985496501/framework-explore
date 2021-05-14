package org.example.common;

import java.util.Map;

/**
 * 封装统一的业务参数 它将贯穿整个pipeline
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public interface Business {
    /**
     * 获取业务code, 每个业务对应一个code, 根据需求 和 前端协商
     *
     * @return code
     */
    String bizCode();

    /**
     * 获取业务名称, 仅仅是为了可读性
     *
     * @return 可读性的code
     */
    String bizName();

    /**
     * 获取业务其他参数
     * Fixme: 这里使用一个统一的Object?
     *
     * @return 其他参数
     */
    Map<String, Object> obtainBizVals();
}
