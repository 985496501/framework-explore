package org.example.feature.date;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author: jinyun
 * @date: 2021/3/9
 */
public class DateTest {

    @Test
    public void localDateTest() {
//        DateUtil.
//        LocalDateTimeUtil.
        System.out.println(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }
}
