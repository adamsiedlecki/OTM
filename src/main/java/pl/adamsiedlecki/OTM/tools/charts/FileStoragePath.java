package pl.adamsiedlecki.OTM.tools.charts;

import java.io.File;

public class FileStoragePath {

    private final static String s = File.separator;

    public static String get() {
        if (SystemDetect.isUnix()) {
            return s + "storage" + s; // /storage/
        } else {
            return "storage" + s;
        }
    }
}
