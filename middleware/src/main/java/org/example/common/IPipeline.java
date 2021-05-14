package org.example.common;

import java.util.List;

/**
 * 定义一条处理管道 维护多个 Handler, 支持merge 全局的handler
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public interface IPipeline {

    /**
     * 这个pipeline 的唯一标识, 用于路由 充当 routingKey 的作用
     *
     * @return name alias for routingKey
     */
    String pipelineName();

    /**
     * 获取所有的全局handler
     *
     * @return handlers
     */
    //List<IGlobalHandler> globalHandlers();

    /**
     * 获取这个pipeline的独有handler
     *
     * @return handlers
     */
    //List<IHandler> localHandlers();

    /**
     * 是否包含特定的handler
     *
     * @param handlerName 处理器名称
     * @return true or false
     */
    //boolean containsHandler(String handlerName);

    /**
     * 获取所有的handlers
     *
     * @return handlers
     */
    List<IHandler> handlers();

    /**
     * 将全局handler和本地handler进行merge
     * 要求在实现类整个bean周期的最后阶段完成merge操作
     *
     * @return handlers
     */
    List<IHandler> mergeHandlers();
}
