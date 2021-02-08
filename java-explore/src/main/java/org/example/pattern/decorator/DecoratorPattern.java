package org.example.pattern.decorator;

import java.util.HashMap;
import java.util.Map;

/**
 * 装饰器模式
 * <p>
 * 扩展已经存在的类的功能。
 *
 * @author: jinyun
 * @date: 2021/2/1
 */
public class DecoratorPattern {
    public interface PerpetualCache {
        Object get(String key);

        void set(String key, Object val);
    }

    public static class DefaultConcreteCache implements PerpetualCache {
        private final Map<String, Object> cacheMap = new HashMap<>(16);

        @Override
        public Object get(String key) {
            return this.cacheMap.get(key);
        }

        @Override
        public void set(String key, Object val) {
            this.cacheMap.put(key, val);
        }
    }

    public static class Decorator implements PerpetualCache {
        private final PerpetualCache perpetualCache;

        public Decorator(PerpetualCache perpetualCache) {
            this.perpetualCache = perpetualCache;
        }

        @Override
        public Object get(String key) {
            return perpetualCache.get(key);
        }

        @Override
        public void set(String key, Object val) {
            perpetualCache.set(key, val);
        }
    }


    public static class ConcreteDecorator extends Decorator {
        public ConcreteDecorator(PerpetualCache perpetualCache) {
            super(perpetualCache);
        }
        // 扩展自己的逻辑
    }

}
