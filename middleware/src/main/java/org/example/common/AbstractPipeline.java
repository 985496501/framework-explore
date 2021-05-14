package org.example.common;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.LinkedList;
import java.util.List;


/**
 * pipeline的基础实现, 维护了pipeline的公共属性
 * 如果是spring环境下, 实现类可以默认实现 BeanNameAware 接口, 使用spring的唯一 beanName,
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public abstract class AbstractPipeline implements IPipeline, BeanNameAware, SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        mergeHandlers();
    }

    /**
     * 使用这个key来唯一标识一条特定业务的pipeline
     * dispatcher 通过这个key 匹配特定的pipeline
     */
    private String routingKey;

    @Override
    public void setBeanName(String name) {
        this.routingKey = name;
    }

    /**
     * 全局的handlers, 每一个pipeline都有, 可为空
     * 如果在spring 环境下可以直接注入
     */
    private AbstractGlobalHandlerHolder globalHandlerList;

    /**
     * 这个是每个实例对象特有的handlers
     */
    private final List<ILocalHandler> localHandlers;

    private boolean merged = false;

    /**
     * 所有的handlers
     */
    private List<IHandler> allHandlers;

    public AbstractPipeline(List<ILocalHandler> localHandlers) {
        this.localHandlers = localHandlers;
    }

    @Override
    public String pipelineName() {
        return this.routingKey;
    }

//    @Override
//    public List<IGlobalHandler> globalHandlers() {
//        return this.globalHandlerList;
//    }
//
//    @Override
//    public List<IHandler> localHandlers() {
//        return this.localHandlers;
//    }

    @Override
    public List<IHandler> handlers() {
        if (merged) {
            return this.allHandlers;
        } else {
            return mergeHandlers();
        }
    }

    @Override
    public List<IHandler> mergeHandlers() {
        if (allHandlers == null || allHandlers.size() == 0) {
            allHandlers = new LinkedList<>();
        }

        allHandlers.addAll(this.globalHandlerList.getGlobalHandlerList());
        allHandlers.addAll(this.localHandlers);
        merged = true;
        return allHandlers;
    }

    /**
     * 子类实现, 可以借助IOC完成自动注入
     *
     * @param holder
     */
    public abstract void setAbstractGlobalHandlerHolder(AbstractGlobalHandlerHolder holder);
}
