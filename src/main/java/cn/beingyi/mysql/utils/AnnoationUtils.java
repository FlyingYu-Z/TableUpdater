package cn.beingyi.mysql.utils;

import cn.beingyi.mysql.annotation.Table;

public class AnnoationUtils {

    public static String getTableName(Class<?> clazz){
        Table table=clazz.getAnnotation(Table.class);
        if(table!=null){
            return table.name();
        }
        return null;
    }


    public static String getTableSql(Class<?> clazz){
        Table table=clazz.getAnnotation(Table.class);
        if(table!=null){
            return table.sql();
        }
        return null;
    }

}
