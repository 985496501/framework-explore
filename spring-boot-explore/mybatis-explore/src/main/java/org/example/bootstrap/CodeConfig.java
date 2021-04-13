package org.example.bootstrap;

import org.apache.ibatis.datasource.pooled.PoolState;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.entity.Demo;
import org.example.mapper.DemoMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * P7硬性条件:
 *
 * Building SqlSessionFactory Without XML.
 * If you prefer to directly build the configuration from java,
 * rather than xml, or create your own configuration builder.
 *
 * Mybatis的构建仅仅在test pkg中引入了依赖 mysql-connector-driver, <scope>test<scope/>
 * 问题一: mybatis是如何发现装配 sql-driver 的?
 * BlogDataSourceFactory ?
 * 问题二: 先理解dataSource的概念? 以及接口抽象意义? java.sql.* 包下.
 * DataSource就是数据源的抽象, 里面维护了数据库连接, 获取连接. 通过数据源可以获取数据库的连接对象.
 * 同时在这个对象也具备父类 一个 Wrapper 接口的特性, T unwrap(T), boolean isWrapperFor(), 它提供了一种什么能力呢?
 * 它可以检索 委派实例, 当一个类是代理类的时候, 这个是一个标准设计, 用于获取标准的 the resource delegates.
 *
 *
 *
 * SPI: Service Provider Interface.
 * java中的支持:
 *
 * CopyOnWriteArrayList 用这个容器来保存了Driver的实现类, 这个容易属于并发容器, 同时这块都是 并发大神
 * Doug Lea 写的, 这块功能强大, 在一些应用比如...
 * 看这些 Tedious and Difficult 代码, 首先考虑情景是什么, 是什么需求使这样实现 ?
 * 枯燥无味 繁琐 而且 非常难
 *
 * @author: jinyun
 * @date: 2021/2/8
 */
public class CodeConfig {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/dev-local?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static final PooledDataSource POOLED_DATA_SOURCE;

    static {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource(DRIVER, URL, USERNAME, PASSWORD);
        POOLED_DATA_SOURCE = new PooledDataSource(unpooledDataSource);
    }


    public static void main(String[] args) throws SQLException {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        // 包装对象 里面就是一些属性 其他啥也没干 就是数据容器
        Environment environment = new Environment("default", transactionFactory, POOLED_DATA_SOURCE);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(DemoMapper.class);

        // 最终都是获取sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            DemoMapper mapper = sqlSession.getMapper(DemoMapper.class);
            Demo byId = mapper.getById(1);
            System.out.println(byId);
        }
    }






    public static void dataSourceTest() throws SQLException {
        PoolState poolState = POOLED_DATA_SOURCE.getPoolState();
        System.out.println(poolState.toString());

        // 这里幸好抽象出来了接口, 那底层数据库连接的代码 以及 一些策略啥的 把人搞死, 准则： 除非必要还是不要看他们写的代码实现, 除非是有一定算法实现, 可以参考算法设计
        // 其他的一些装配信息 一些业务逻辑 不要看 不要看 看了也没用 就当是一个工人帮你把活淦了  是因为要你 你也可以写出来  就是节省了开发时间而已 但是接口一定要看

        Connection connection = POOLED_DATA_SOURCE.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from demo where id = ?");
        preparedStatement.setObject(1, 1);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println(resultSet.getString("a"));
        }

        System.out.println(resultSet);
    }
}
