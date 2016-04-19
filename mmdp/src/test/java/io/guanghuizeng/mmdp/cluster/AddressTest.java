package io.guanghuizeng.mmdp.cluster;

import io.guanghuizeng.fs.Address;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class AddressTest {
    @Test
    public void test0() {

        String host = "111";
        int port = 9000;

        Address address1 = new Address(host, port);
        Address address2 = new Address(host, port);

        assertEquals(address1, address2);
    }

    @Test
    public void test1() {
        String host = "111";
        int port = 9000;

        Address address1 = new Address(host, port);
        Address address2 = new Address(host, port);

        HashMap<Address, Integer> map = new HashMap<>();

        map.put(address1, 1);

        assertTrue(map.containsKey(address2));
    }
}
