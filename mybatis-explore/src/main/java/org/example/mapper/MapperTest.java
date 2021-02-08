package org.example.mapper;

import org.apache.ibatis.binding.MapperRegistry;

/**
 * 我们定义了一些列的mapper接口 里面有对应了sql的方法
 * 为了加速执行速度, 需要把所有的mapper缓存起来
 * 定义了 Registry 注册表(Mapper的注册中心)
 *
 *
 * @author: jinyun
 * @date: 2021/2/8
 */
public class MapperTest {
    public static void main(String[] args) {
        MapperRegistry mapperRegistry = new MapperRegistry(null);
        // 添加一个字节码接口对象
        mapperRegistry.addMapper(DemoMapper.class);
    }
}
