package cn.beingyi.mysql.core;

import cn.beingyi.mysql.utils.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReadyCore {

    private Ready ready;
    private List<String> tableNames = new ArrayList<>();

    public static ReadyCore init(Ready mReady) throws Exception {
        return new ReadyCore(mReady);
    }

    public ReadyCore(Ready ready) throws Exception {
        this.ready = ready;
        if (ready == null) {
            throw new Exception("ready is null");
        }
        try {
            Connection con = getConnection();
            DatabaseMetaData db = con.getMetaData();
            ResultSet resultSet = db.getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(3));
            }
            resultSet.close();
            con.close();
        } catch (Exception e) {
            throw new Exception("failed to connect to mysql server");
        }
    }

    public void initTable(Class<?>... cls) throws Exception {
        for (Class<?> clazz : cls) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        String tableName = AnnoationUtils.getTableName(clazz);
                        if (tableName == null || tableName.isEmpty()) {
                            throw new Exception("table name must not be empty");
                        }

                        //不存在则创建，再升级
                        if (!tableNames.contains(tableName)) {
                            new TableCreator(ReadyCore.this, clazz);
                        } else {
                            //如果表存在，则升级
                            new TableUpdater(ReadyCore.this, clazz);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.join();
            thread.start();
        }
    }

    public Connection getConnection() throws Exception {
        return JDBCUtils.getConnection(this);
    }

    public Ready getReady() {
        return ready;
    }

    public List<String> getTableNames() {
        return tableNames;
    }
}
