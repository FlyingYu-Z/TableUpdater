package cn.beingyi.mysql.core;

import cn.beingyi.mysql.annotation.Column;
import cn.beingyi.mysql.utils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableCreator {
    ReadyCore readyCore;
    Class<?> clazz;
    String tableName;
    List<Column> columnList = new ArrayList<>();

    public TableCreator(ReadyCore readyCore, Class<?> clazz) throws Exception {
        this.readyCore = readyCore;
        this.clazz = clazz;
        init();
        new TableUpdater(readyCore, clazz);
    }

    private void init() throws Exception {
        tableName = TableUtils.getTableName(clazz);
        columnList = ColumnUtils.getColumnList(clazz);
        createTable();
    }


    private void createTable() throws Exception {
        List<Column> keyList = ColumnUtils.getKeyColumn(clazz);
        if (keyList.size() == 0) {
            throw new Exception("a table must have a key column");
        }
        Column keyColumn = keyList.get(0);
        StringBuilder sql = new StringBuilder();
        sql.append("create table if not exists `" + tableName + "` (\n");
        sql.append(getNewSqlByColumn(keyColumn));
        //如果是主键
        if (keyColumn.isKey()) {
            sql.append("  PRIMARY KEY (`" + keyColumn.name() + "`)").append("\n");
        }
        sql.append(")\n");
        sql.append("ENGINE=InnoDB DEFAULT CHARSET="+readyCore.getReady().getCharacterSet()+" ROW_FORMAT=DYNAMIC;");

        System.out.println(sql.toString());

        Connection con = readyCore.getConnection();
        Statement stmt = con.createStatement();
        if (stmt.executeLargeUpdate(sql.toString()) == 0) {
            //System.out.println("create table "+tableName+" successfully");
        } else {
            System.out.println("failed to create table " + tableName);
        }
        JDBCUtils.closeResource(stmt, con);
    }

    //获取创建表时的字段sql
    private String getNewSqlByColumn(Column column) {
        StringBuilder sql = new StringBuilder();
        sql.append(" `" + column.name() + "`").append(" ");
        sql.append(column.type()).append(" ");
        //如果不是主键，且可以为null
        if (column.canNull() && !column.isKey()) {
            sql.append(" NULL").append(" ");
        } else {
            sql.append("NOT NULL").append(" ");
        }
        //如果能自动增长
        if (column.autoGen()) {
            sql.append("AUTO_INCREMENT").append(" ");
        } else {
            //如果有默认值，且不会自动增长
            if (!column.defaultValue().isEmpty()) {
                sql.append("DEFAULT ").append(column.defaultValue()).append(" ");
            }
        }
        sql.append(",").append("\n");
        return sql.toString();
    }


}
