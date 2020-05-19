package net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {

    public static String getIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
