package cn.beingyi.mysql.utils;

import cn.beingyi.mysql.annotation.Table;

public class TableUtils {


    public static boolean isTable(Class<?> cls) {
        boolean exists = cls.isAnnotationPresent(Table.class);
        return exists;
    }

    public static String getTableName(Class<?> cls)throws Exception{
        if(!isTable(cls)){
            throw new Exception(cls.getName()+" is not a table class");
        }
        Table table=cls.getAnnotation(Table.class);
        return table.name();
    }

}
