package cn.beingyi.mysql.utils;

import java.text.DecimalFormat;

public class FloatUtils {

    public static String formatTwo(float num){
        DecimalFormat fnum = new DecimalFormat( "##0.00 ");
        String result=fnum.format(num);
        return result;
    }

}
