package org.test.auto;

import org.junit.Test;
import org.springframework.cloud.gateway.support.StringToZonedDateTimeConverter;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * 测试Gateway的自动装配
 *
 * @author: jinyun
 * @date: 2021/4/14
 */
public class GatewayAutoConfigurationTest {

    @Test
    public void getMillisecondsTest() {
        // 返回一个毫秒级别的时间戳
        long l = Instant.now().toEpochMilli();
        System.out.println(l);
    }

    /**
     * 1970-01-01T00:00:00Z milliseconds
     */
    @Test
    public void StringToZonedDateTimeConverterTest() {
        StringToZonedDateTimeConverter converter = new StringToZonedDateTimeConverter();
        ZonedDateTime zonedDateTime = converter.convert("1618396444676");
        String data = String.format("%d年%d月%d日 %d：%d:%d",
                zonedDateTime.getYear(), zonedDateTime.getMonth().getValue(), zonedDateTime.getDayOfMonth(),
                zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond());
        System.out.println(data);
    }
}
