package org.example.pattern.structure.decorator;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>包装设计模式</h2>
 * <p>看了 Spring-cloud-alibaba-nacos-config 的 http 调用相关的封装之后真正的体会到。 <b>NacosConfigService</b></p>
 * <p>前面使用了也演练了但是没有体会到就忘记了  前面用到这种情况是 缓存系统 的设计。</p>
 * <p>
 * 扩展已经存在的类的功能。 一个顶级接口, 然后已经有一个指定接口的实现， 这个实现已经明明确确的完成了主体的任务，
 * 但是现在有了新的需求， 可能要前后加入 统计什么乱七八糟的东西， 可能还要依赖其他对象 完成组合性具体操作任务。
 * 于是 就设计一个新的类 具体实现原先的接口，然后依赖 原有 的具体对象 或者 其他对象 —— {构造方法注入}， 或者自行实现方法，
 * 然后这个类实现接口可以争强方法的实现
 * 还有就是 不需要争强的方式 直接调用依赖对象的方法即可。
 *
 * @author: jinyun
 * @date: 2021/2/1
 */
public class DecoratorPattern {

    /**
     * 顶级接口，一般就是最初的方法
     */
    public interface TopInterface {
        Object get(String key);
    }

    /**
     * 最初的模式实现已经完成了
     */
    public static class DefaultConcreteImpl implements TopInterface {
        private final Map<String, Object> cacheMap = new HashMap<>(16);

        @Override
        public Object get(String key) {
            return this.cacheMap.get(key);
        }
    }

    /**
     * 又有了新的需求 然后需要扩展原有接口的功能，但是主体的接口已经默认实现完成了
     * 算是功能的增强， 那么只要通过构造方法 把原先的默认实现注入即可。
     */
    public static class Decorator implements TopInterface {
        /**
         * Dependency default impl.
         */
        private final TopInterface perpetualCache;

        /**
         * constructor autowiring.
         *
         * @param perpetualCache 原先的默认实现
         */
        public Decorator(TopInterface perpetualCache) {
            this.perpetualCache = perpetualCache;
        }

        /**
         * 我是用了新的类进行实现调用，原有的具体代码没有改动，但是新增了类，增加了新的维护复杂度、
         * 还有如果  又有新的需求进来了, 这个该怎么做了？
         *
         * <p>是否还符合 开闭原则 ?</p>
         *
         * @param key
         * @return
         */
        @Override
        public Object get(String key) {
            System.out.println("开始之前的我需要进行复杂的校验功能.....");
            Object o = perpetualCache.get(key);
            System.out.println("我需要对获取的结果进行 负责的校验 已经其他功能，比如通知等功能");
            if (o == null) {
                System.out.println("没有获取指定key的对象数据，我们返回一个默认的对象");
                return new Object();
            }

            return o;
        }
    }


    public static void main(String[] args) {
        TopInterface i = new Decorator(new DefaultConcreteImpl());
        Object a = i.get("a");
        System.out.println(a);
    }

}
