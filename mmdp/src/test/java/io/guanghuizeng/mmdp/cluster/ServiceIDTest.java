package io.guanghuizeng.mmdp.cluster;

import io.guanghuizeng.fs.ServiceID;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class ServiceIDTest {
    @Test
    public void test0() {

        String host = "111";
        int port = 9000;

        ServiceID serviceID1 = new ServiceID(host, port);
        ServiceID serviceID2 = new ServiceID(host, port);

        assertEquals(serviceID1, serviceID2);
    }

    @Test
    public void test1() {
        String host = "111";
        int port = 9000;

        ServiceID serviceID1 = new ServiceID(host, port);
        ServiceID serviceID2 = new ServiceID(host, port);

        HashMap<ServiceID, Integer> map = new HashMap<>();

        map.put(serviceID1, 1);

        assertTrue(map.containsKey(serviceID2));
    }
}
