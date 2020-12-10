package cn.beingyi.mysql.utils;

public class LogUtils {

    public static void Log(String log){
        if(Build.isDubug()) {
            System.out.println(log);
        }
    }

}
