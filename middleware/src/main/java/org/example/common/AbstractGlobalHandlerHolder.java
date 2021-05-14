package org.example.common;

import java.util.List;

/**
 * 这个是全局的处理持有器, 抽象这个类的作用用于 javabean 自动注入
 * 子类实现的所有global Handler, 全局的会在容器内部完成类型注入,
 * 凡是符合类型的全部都会注入;
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public abstract class AbstractGlobalHandlerHolder {
    private List<IGlobalHandler> globalHandlerList;


    /**
     * 抽象方法 由子类实现 如果在spring环境下可以由IOC自动注入所有的 JavaBean
     * @Autowired 进行标注即可
     *
     * @param globalHandlerList including
     */
    public abstract void setGlobalHandlerList(List<IGlobalHandler> globalHandlerList);

    public List<IGlobalHandler> getGlobalHandlerList() {
        return this.globalHandlerList;
    }
}
