package org.example.convention;

import org.junit.jupiter.api.Test;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.ConversionService;

/**
 * @author: jinyun
 * @date: 2021/4/20
 */
public class ApplicationConversionServiceTest {
    /**
     * 又是一个facade
     */
    @Test
    public void facadeTest() {
        // 外部直接调用这个对象即可： 全局单例对象
        ConversionService conversionService = ApplicationConversionService.getSharedInstance();

    }
}
