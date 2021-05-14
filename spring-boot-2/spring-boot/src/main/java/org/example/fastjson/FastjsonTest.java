package org.example.fastjson;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.junit.jupiter.api.Test;

/**
 * @author: jinyun
 * @date: 2021/5/14
 */
public class FastjsonTest {

    @Test
    public void xxTest() {
        // alibaba implements the MessageConverter.
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
    }


}
