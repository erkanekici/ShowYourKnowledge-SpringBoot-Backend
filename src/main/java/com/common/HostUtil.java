package com.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

//TODO Test Et
public class HostUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(HostUtil.class);

    public static String getLocalHostName() { //Machine Name
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOGGER.error("HostUtil getLocalHostName UnknownHostException error", e);
            return "";
        }
    }

    public static String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress(); // Machine IP Address
        } catch (UnknownHostException e) {
            LOGGER.error("HostUtil getLocalHostAddress UnknownHostException error", e);
            return "";
        }
    }

    public static String getRemoteHostName() {
        return InetAddress.getLoopbackAddress().getHostName(); // localHost

    }

    public static String getRemoteHostAddress() {
        return InetAddress.getLoopbackAddress().getHostAddress(); // 127.0.0.1

    }
}
