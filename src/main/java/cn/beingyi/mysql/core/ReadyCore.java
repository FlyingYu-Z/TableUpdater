package cn.beingyi.mysql.core;

import cn.beingyi.mysql.utils.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadyCore {

    static Ready ready;
    public static List<String> tableNames = new ArrayList<>();

    public static void init(Ready mReady) throws Exception {
        ready = mReady;
        if (ready == null) {
            throw new Exception("ready is null");
        }
        try {
            Connection con = JDBCUtils.getConnection();
            DatabaseMetaData db = con.getMetaData();
            ResultSet resultSet = db.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(3));
            }
            resultSet.close();
            con.close();

        }catch (Exception e){
            //e.printStackTrace();
            throw new Exception("failed to connect to mysql server");
        }

    }


    public static void initTable(Class<?> ... cls) throws Exception{
        for(Class<?> clazz:cls){
            Thread thread=new Thread(){
                @Override
                public void run() {
                    super.run();
                    //String tableName= AnnoationUtils.getTableName(clazz);
                    try {
                        new TableCreator(clazz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.join();
            thread.start();
        }
    }


    public static Ready getReady() {
        return ready;
    }

}
