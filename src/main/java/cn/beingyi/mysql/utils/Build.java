package cn.beingyi.mysql.utils;

public class Build {

     static boolean isDubug=false;

    public static boolean isDubug() {
        return isDubug;
    }

    public static void setDubug(boolean dubug) {
        isDubug = dubug;
    }
}
