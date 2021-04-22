package test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

public class Pojo {

    /**
     * 需要显示实现 serializable 不然实例化失败
     * <p>
     * 如果这么用对象纯属会出现很大问题 redis客户端展示的 16进制
     */
    @AllArgsConstructor
    @Data
    static class Student implements Serializable {
        private int num;
        private String userName;
    }
}