package org.example.pattern.builder;

import lombok.ToString;
import org.junit.Test;

/**
 * 创建对象模式： 构建者模式 这个是创建复杂对象
 * 1. 对象的attributes 很多
 * 2. 每个属性 或多或少 都要进行配置
 * <p>
 * 比如很典型的应用 Security 中, 通过一个 config 来配置构建的对象
 * 可以学习它的接口设计。
 * <p>
 * <p>
 * Plain ordinary java object builder
 *
 * @author: Liu Jinyun
 * @date: 2021/2/17/0:22
 */
@ToString
public class Pojo {
    @Test
    public void buildObj() {

        Pojo pojo = Pojo.builder()
                .name("builder")
                .length(15)
                .build();
        System.out.println(pojo);
    }


    // 一般使用构建者模式 属性都是大于或者等于4个 这里仅仅是模拟

    private String name;
    private Integer length;

    /**
     * 外部类可以直接访问 它的静态内部类的成员属性
     * 这个是编译器会生成静态方法
     *
     * 这就必须显式 声明 一个外部类的构造方法 00
     *
     * @param pojoBuilder builder
     */
    private Pojo(PojoBuilder pojoBuilder) {
        this.name = pojoBuilder.name;
        this.length = pojoBuilder.length;
    }

    public Pojo() {}

    // 一般是通过一个静态内部类构造对象
    /**
     * 肯定是创建静态内部类对象 需要我们暴露一个静态的方法
     *
     * @return builder
     */
    public static PojoBuilder builder() {
        return new PojoBuilder();
    }

    /**
     * 既然通过一个静态内部类配置外层的类
     * 就要 复制 所有的 属性
     *
     * 这个就是构建者
     * 我们拿到我们需要的对象的构建者
     * 然后把我们需要的属性给构建者
     * 然后调用构建者的 构建方法 就可以直接生产出我们需要的代码
     */
    static class PojoBuilder {
        private String name;
        private Integer length;


        // 每次构建完都要返回构建者
        public PojoBuilder name(String name) {
            // 对这个属性是一顿操作啊 这里是一个简单的引用数据类型
            // 如果这也是一个对象 同样有很多属性需要设置 就可以使用构建者 将配置分开
            this.name = name;
            // 内部类对象
            return this;
        }

        public PojoBuilder length(Integer length){
            this.length = length;
            return this;
        }

        /**
         * 这个静态内部类 需要提供一个实例方法 返回一个我们最终要的对象
         *
         * @return pojo
         */
        public Pojo build() {
            return new Pojo(this);
        }
    }
}
