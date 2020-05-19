package net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility osztály netes funkciókhoz
 */
public class NetUtils {

    public static final int PORT = 5000;

    public static String getIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
