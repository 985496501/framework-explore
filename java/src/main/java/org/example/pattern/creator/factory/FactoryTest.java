package org.example.pattern.creator.factory;

import org.example.model.thread.util.Sleeper;
import org.junit.Test;

import java.util.concurrent.Executor;

/**
 * 我们创建多态对象：
 *     Typically, we generally create the specified functionality instance, which abstracts as an interface.
 *
 * @author: jinyun
 * @date: 2021/5/6
 */
public class FactoryTest {

    public interface ExecutorFactory {
        Executor newInstance();
    }

    /**
     * 不要写成
     */
    public static class HelloExecutorFactory implements ExecutorFactory {

        private HelloExecutorFactory() {}

        private volatile static ExecutorFactory executorFactory;

        @Override
        public Executor newInstance() {
            return new HelloExecutor();
        }

        public static ExecutorFactory getExecutorFactory() {
            if (executorFactory == null) { // avoid lock this method each time calling.
                synchronized (ExecutorFactory.class) {
                    if (executorFactory == null) {
                        executorFactory = new HelloExecutorFactory();
                    }
                }
            }

            return executorFactory;
        }
    }

    public static class HelloExecutor implements Executor {
        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }


    @Test
    public void factoryTest() {
        ExecutorFactory executorFactory = HelloExecutorFactory.getExecutorFactory();
        Executor executor = executorFactory.newInstance();
        executor.execute(()-> System.out.println("hello world"));

        Sleeper.sleep(1);
    }


}
