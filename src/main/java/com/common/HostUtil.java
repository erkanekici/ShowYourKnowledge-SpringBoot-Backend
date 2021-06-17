package com.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

//TODO Test Et
public class HostUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(HostUtil.class);

    public static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOGGER.error("HostUtil getLocalHostName UnknownHostException error", e);
            return "";
        }
    }

    public static String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("HostUtil getLocalHostAddress UnknownHostException error", e);
            return "";
        }
    }

    public static String getRemoteHostName() {
        return InetAddress.getLoopbackAddress().getHostName();

    }

    public static String getRemoteHostAddress() {
        return InetAddress.getLoopbackAddress().getHostAddress();

    }
}
