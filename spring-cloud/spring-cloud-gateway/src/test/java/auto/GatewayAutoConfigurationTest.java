package auto;

import org.junit.Test;
import org.springframework.cloud.gateway.support.StringToZonedDateTimeConverter;
import org.springframework.util.StringUtils;

import java.time.*;
import java.util.Arrays;

/**
 * 测试Gateway的自动装配
 *
 * @author: jinyun
 * @date: 2021/4/14
 */
public class GatewayAutoConfigurationTest {
    public static final String LOCAL_ZONE_ID = "Asia/Shanghai";

    @Test
    public void zonedDateTimeTest() {
        // jdk1.8 ZonedDateTime 时区时间
        // 获取系统默认的时区 从操作系统中获取
        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println("fetch system default zoneId= " + zoneId.getId());

        // 直接获取这个对象, 速度超快了
        ZoneId zoneID = ZoneId.of(LOCAL_ZONE_ID);

        // 看下instant 这个类 实时     jdk1.8:  Clock, Instant
        long localNow = Instant.now(Clock.system(zoneID)).toEpochMilli();
        System.out.println(getZonedDateTime(localNow) + "\n\n");

        // 继续查看instant 这个系统主要用于开发   国际化， 可以用于 实时转换时间
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(localNow).atOffset(ZoneOffset.ofHours(8)).toZonedDateTime();
        System.out.println(formatZonedDateTime(zonedDateTime));
    }

    /**
     * 1970-01-01T00:00:00Z milliseconds
     * 这个 时区时间的 converter 怎么调用, 怎么统一处理时间
     */
    @Test
    public void StringToZonedDateTimeConverterTest() {
        // 返回一个毫秒级别的时间戳 这个默认是获取UTC的时间
        long l = Instant.now().toEpochMilli();

        String date = this.getZonedDateTime(l);
        System.out.println(date);
    }

    /**
     * gateway的属性配置接口
     * ssl: secure socket layer.  安全套接字, 应用层(http)和传输层(tcp)进行数据加密
     */
    @Test
    public void gatewayPropertiesTest() {

    }




    @Test
    public void stringUtilTest() {
        String[] strings = StringUtils.tokenizeToStringArray("/red/{segment},/blue/{segment}", ",");
        System.out.println(Arrays.toString(strings));
    }







































    private String getZonedDateTime(long instant) {
        StringToZonedDateTimeConverter converter = new StringToZonedDateTimeConverter();
        ZonedDateTime zonedDateTime = converter.convert(instant + "");
        return formatZonedDateTime(zonedDateTime);
    }

    private String formatZonedDateTime(ZonedDateTime zonedDateTime) {
        return String.format("%d年%d月%d日 %d:%d:%d",
                zonedDateTime.getYear(), zonedDateTime.getMonth().getValue(), zonedDateTime.getDayOfMonth(),
                zonedDateTime.getHour(), zonedDateTime.getMinute(), zonedDateTime.getSecond());
    }

}
