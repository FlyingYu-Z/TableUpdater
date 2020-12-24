package cn.beingyi.mysql.core;

import cn.beingyi.mysql.annotation.Column;
import cn.beingyi.mysql.utils.AnnoationUtils;
import cn.beingyi.mysql.utils.ColumnUtils;
import cn.beingyi.mysql.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableUpdater {
    ReadyCore readyCore;
    Class<?> clazz;
    String tableName;
    List<String> fieldList;
    List<Column> columnList;

    public TableUpdater(ReadyCore readyCore,Class<?> clazz) throws Exception {
        this.readyCore=readyCore;
        this.clazz = clazz;
        this.tableName = AnnoationUtils.getTableName(clazz);
        this.fieldList = getColumnNames(tableName);
        this.columnList = ColumnUtils.getColumnList(clazz);
        updateTable();
    }

    private void updateTable() throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("ALTER TABLE `" + tableName + "` \n");
        for (Column column : columnList) {
            sqlBuilder.append(getSqlByColumn(column)).append(",\n");
        }
        sqlBuilder.append("DROP PRIMARY KEY").append(",\n");
        List<Column> keyList = ColumnUtils.getKeyColumn(clazz);
        if (keyList.size() == 0) {
            throw new Exception("a table must have a key column");
        }
        sqlBuilder.append("ADD PRIMARY KEY (");
        for(Column column:keyList){
            sqlBuilder.append("`"+column.name()+"`,");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
        sqlBuilder.append(");");

        String sql=sqlBuilder.toString();
        System.out.println(sql);
        Connection con = readyCore.getConnection();
        Statement stmt = con.createStatement();
        if (stmt.executeLargeUpdate(sql) == 0) {
            System.out.println("update table "+tableName+" successfully");
        } else {
            System.err.println("failed to update table:"+tableName);
        }
        JDBCUtils.closeResource(stmt, con);
    }

    private String getSqlByColumn(Column column) {
        boolean isAdd=!fieldList.contains(column.name());
        StringBuilder sql = new StringBuilder();
        if(isAdd){
            sql.append("ADD COLUMN").append(" ");
        }else {
            sql.append("MODIFY COLUMN").append(" ");
        }
        sql.append("`" + column.name() + "`").append(" ");
        sql.append(column.type()).append(" ");

        if(!isAdd && column.type().contains("char")){
            sql.append("CHARACTER SET "+readyCore.getReady().getCharacterSet()).append(" ");
            sql.append("COLLATE "+readyCore.getReady().getCollation()).append(" ");
        }

        //如果不是主键，且可以为null
        if (column.canNull() && !column.isKey()) {
            sql.append(" NULL").append(" ");
        } else {
            sql.append("NOT NULL").append(" ");
        }
        //如果能自动增长
        if (column.autoGen()) {
            sql.append("AUTO_INCREMENT").append(" ");
        }
        //如果有默认值，且不会自动增长
        if (!column.defaultValue().isEmpty() && !column.autoGen()) {
            sql.append("DEFAULT ").append(column.defaultValue()).append(" ");
        }

        int index = columnList.indexOf(column);
        if(index==0){
            sql.append("FIRST");
        }else if(index != columnList.size() - 1) {
            String afterName = columnList.get(index - 1).name();
            sql.append("AFTER ").append("`" + afterName + "`").append(" ");
        }
        return sql.toString();
    }


    /**
     * 获取表中所有字段名称
     */
    public List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        try {
            Connection con = readyCore.getConnection();
            PreparedStatement stmt = null;
            String tableSql = "select * from `" + tableName + "`";
            stmt = con.prepareStatement(tableSql);
            ResultSetMetaData rsmd = stmt.getMetaData();
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
        }
        return columnNames;
    }

}
