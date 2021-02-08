package org.example;

/**
 * mybatis framework explore entry point.
 *
 * Mybatis的三层架构：
 * API接口层： 提供了接口供开发者直接调用使用 -->
 * 数据处理层：负责sql查找 解析 执行 结果映射 完成一次数据库操作 -->
 * 数据支撑层：最基础的功能支撑, 连接管理 事务管理 配置加载和缓存 最基础的组件
 *
 *
 * 一个非常重要的对象 MappedStatement
 *
 *
 * @author: jinyun
 * @date: 2021/2/1
 */
public class MybatisMain {
    /**
     * 1. 数据库访问层 明白sqlSession 统一生产sqlSession的Factory 构建Factory的builder.
     * org.apache.ibatis.session.SqlSession
     * 2. java.sql.Connection defines transaction isolation levels: 5
     * TRANSACTION_NONE=0 没有事务
     * TRANSACTION_READ_UNCOMMITTED=1 读未提交, 一个事务A还没有提交就事务B就读到了, 如果这个A回滚了, 数据出现错误, dirty read. 脏读是读到了假数据。
     * TRANSACTION_READ_COMMITTED=2 读已提交, 一个事务A读到事务B提交的数据, 如果事务A在同一事务在B的提交前后分别读同样的条件id=1, 会出现不同的结果, none repeatable read.
     * TRANSACTION_REPEATABLE_READ=4 可重复读, id=1的数据在整个事务A中, 即使事务B改了也还是事务A读到的数据, 这就有了事务隔离的感觉了吧
     * TRANSACTION_SERIALIZATION=8 序列化操作, 当事务的级别设置成了可重复读, 但是这个还是避免不了where条件查询多了或者少了一条的情况， phantom read. 幻读，多少了一条记录出现了幻觉
     *
     * 3. deep learning MySQL application:
     * mysql 对这几种事务的隔离级别是如何实现的, 版本控制的实现方式。
     *
     * 4. 要充分理解 mybatis 如何使用动态代理技术 创建了sql的实际执行类
     *
     * 5. 要深入理解 mybatis 的三级缓存
     *
     * refer to https://www.cnblogs.com/wuzhenzhao/p/11103043.html
     * mybatis的缓存设计 值得学习, 后面抄袭一下.
     * 缓存的一般实现： 普通缓存 淘汰算法缓存 装饰器缓存
     * 装饰器模式：
     *
     *
     */
    public static void main(String[] args) {
//        SqlSession
//        SqlSessionFactoryBuilder

//        MappedStatement
    }

}
