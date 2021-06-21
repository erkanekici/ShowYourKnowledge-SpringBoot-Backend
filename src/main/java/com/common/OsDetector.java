package com.common;

public class OsDetector {

    private static boolean isWindows = false;
    private static boolean isLinux = false;
    private static boolean isMac = false;

    private static OsDetector _this = null;
    public static OsDetector getInstance() {
        if (_this == null)
            _this = new OsDetector();
        return _this;
    }

    static {
        String os = System.getProperty("os.name").toLowerCase();
        isWindows = os.contains("win");
        isLinux = os.contains("nux") || os.contains("nix");
        isMac = os.contains("mac");
    }
    public boolean isWindows() {
        return isWindows;
    }
    public boolean isLinux() {
        return isLinux;
    }
    public boolean isMac() {
        return isMac;
    }
}