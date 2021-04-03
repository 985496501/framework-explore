package org.example.thread.future;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.example.model.thread.executor.service.ThreadPoolExecutorTest;
import org.example.model.thread.util.Sleeper;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Liu Jinyun
 * @date: 2021/3/21/20:59
 */
public class CompletableFutureTest {
    public static final ThreadPoolExecutor threadPoolExecutor =
            ThreadPoolExecutorTest.threadPoolExecutor;


    @Test
    public void cfTest() throws ExecutionException, InterruptedException {
        CompletableFuture<UserDetail> cf = new CompletableFuture<>();
        new Thread(()-> {
            UserDetail user = new User(1, "jinyun", "abc123");
            Sleeper.sleep(2);
            cf.complete(user);
        }).start();

        Sleeper.sleep(1);
        UserDetail userDetail = cf.get();
        System.out.println(userDetail);
    }

    /**
     * 取最快线程的结果
     *
     * @throws InterruptedException i
     */
    @Test
    public void asyncUsingFastestTest() throws InterruptedException {
        CompletableFuture.supplyAsync(this::getUserDetail,
                threadPoolExecutor)
        .acceptEitherAsync(CompletableFuture.supplyAsync(this::getUserDetail2,
                threadPoolExecutor), System.out::println, threadPoolExecutor);
        Thread.currentThread().join();
    }

    @Test
    public void asyncTest() throws InterruptedException {
        CompletableFuture.supplyAsync(this::getUserDetail, threadPoolExecutor)
                .applyToEitherAsync(
                        CompletableFuture.supplyAsync(this::getUserDetail2, threadPoolExecutor), Object::toString, threadPoolExecutor)
                .whenCompleteAsync((r, e) -> {
                    if (e != null) {
                        System.out.println(e);
                    }

                    System.out.println(r);
                    }, threadPoolExecutor);
        Thread.currentThread().join();
    }



    private UserDetail getUserDetail() {
        UserDetail user = new User(2, "jinyun", "abc123");
        Sleeper.sleep(2);
        return user;
    }

    private UserDetail getUserDetail2() {
        UserDetail user = new User(3, "jinyun", "abc123");
        Sleeper.sleep(3);
        return user;
    }

    interface UserDetail {
        Integer getId();
        String getUserName();
        String getPassword();
    }

    @Getter
    @ToString
    @AllArgsConstructor
    static class User implements UserDetail {
        Integer id;
        String userName;
        String password;
    }

    // 定义接口进行业务模型的抽象

    interface ICalculation {

    }

    interface IEvaluation {

    }
}
