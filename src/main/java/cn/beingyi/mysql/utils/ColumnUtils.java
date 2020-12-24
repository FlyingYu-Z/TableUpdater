package cn.beingyi.mysql.utils;

import cn.beingyi.mysql.annotation.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ColumnUtils {


    public static List<Column> getKeyColumn(Class<?> clazz){
        List<Column> columnList=new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);
            if(column!=null && column.isKey()){
                columnList.add(column);
            }
        }
        return columnList;
    }


    public static List<Column> getColumnList(Class<?> clazz){
        List<Column> columnList=new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);
            if(column!=null){
                columnList.add(column);
            }
        }
        return columnList;
    }



}
