package pl.adamsiedlecki.OTM.tools.charts;

import java.io.File;

public class MyFilesystem {

    private final static String s = File.separator;

    public static String getStoragePath() {
        if (SystemDetect.isUnix()) {
            return s + "storage" + s; // /storage/
        } else {
            return "storage" + s;
        }
    }

    public static String getSeparator() {
        return s;
    }

    public static boolean fileExistsAndIsNoOlderThanXSeconds(File file, long x) {
        return file.exists() && (System.currentTimeMillis() - file.lastModified()) < (x * 1000);
    }
}
