package org.example.pattern.pattern;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: jinyun
 * @date: 2021/4/14
 */
public class PatternTest {
    /**
     * a regular expression.
     *
     * <ul>
     *     <li> <b>*</b> 可以替换任意字符</li>
     * </ul>
     *
     *
     *
     */
    @Test
    public void patternTest() {
        // 建立利用预编译功能 加速正则匹配
        Pattern p = Pattern.compile("a*b");
        Matcher matcher = p.matcher("aaaab");
        boolean matches = matcher.matches();
        System.out.println(matches);


        System.out.println(Pattern.matches("a*b", "aaabc"));
    }
}
