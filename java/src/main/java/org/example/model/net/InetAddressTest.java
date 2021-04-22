package org.example.model.net;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * @author: jinyun
 * @date: 2021/4/22
 */
public class InetAddressTest {

    @Test
    public void getByNamesTest() throws UnknownHostException {
        InetAddress[] localhosts = InetAddress.getAllByName("localhost");
        System.out.println(Arrays.toString(localhosts));

        InetAddress cdn001 = InetAddress.getByName("cdh001");
        System.out.println(cdn001.toString());
    }
}
