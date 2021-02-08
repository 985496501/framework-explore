package org.example.reflect;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Modifier;

/**
 * java定义了类的修饰符
 *
 * @author: jinyun
 * @date: 2021/2/8
 */
public class ModifierTest {

    private static final int ALLOWED_MODES = MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
            | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC;

    public static void main(String[] args) {

        // private=2 protected=4 package[static]=8  public=1
        // 使用二进制标识符进行 或 运算 ==> 就可以获取所有的状态 这个值 ！！！！
        int seventeen = 0x00000011;
        System.out.println(seventeen);

        // 妈的 使用16进制存储标识 0X00000001 表示32位bit
        System.out.println(Modifier.PUBLIC);
        System.out.println(Modifier.FINAL);
        System.out.println(ALLOWED_MODES);
    }
}
