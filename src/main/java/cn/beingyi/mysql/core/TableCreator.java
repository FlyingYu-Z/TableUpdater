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
    Class<?> clazz;
    List<String> columnNames=new ArrayList<>();
    String tableName;
    List<Column> columnList=new ArrayList<>();

    public TableCreator(Class<?> clazz) throws Exception {
        this.clazz = clazz;
        init();

    }

    private void init() throws Exception {
        tableName = TableUtils.getTableName(clazz);
        columnList= ColumnUtils.getColumnList(clazz);

        if (ReadyCore.tableNames.contains(tableName)) {
            updateTable();
            updateColumn();
        } else {
            createTable();
            updateTable();
        }


    }


    private void updateColumn() throws Exception {
        //columnNames=getColumnNames(tableName);
        for(Column column:columnList){
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE `" + tableName + "` \n");
            sql.append(getModifySqlByColumn(column));
            //System.out.println(sql.toString());

            Connection con = JDBCUtils.getConnection();
            Statement stmt = con.createStatement();
            if (stmt.executeLargeUpdate(sql.toString()) == 0) {
                //System.out.println("update column "+column.name()+" successfully");
            }else{
                System.out.println("failed to add column "+column.name());
            }
            JDBCUtils.closeResource(stmt, con);
        }
    }


    private void updateTable() throws Exception {
        columnNames=getColumnNames(tableName);
        for(Column column:columnList){
            if(columnNames.contains(column.name())){
                continue;
            }
            addColumn(column);
        }
    }



    private void addColumn(Column column)throws Exception{

        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE `" + tableName + "` \n");
        sql.append(getAddSqlByColumn(column));
        //System.out.println(sql.toString());

        Connection con = JDBCUtils.getConnection();
        Statement stmt = con.createStatement();
        if (stmt.executeLargeUpdate(sql.toString()) == 0) {
            //System.out.println("add column "+column.name()+" successfully");
        }else{
            System.out.println("failed to add column "+column.name());
        }
        JDBCUtils.closeResource(stmt, con);

    }

    //获取添加字段的sql
    private String getAddSqlByColumn(Column column){
        StringBuilder sql = new StringBuilder();
        sql.append("ADD COLUMN").append(" ");
        sql.append("`"+column.name()+"`").append(" ");
        sql.append(column.type()).append(" ");
        //如果不是主键，且可以为null
        if(column.canNull() && !column.isKey()){
            sql.append(" NULL").append(" ");
        }else {
            sql.append("NOT NULL").append(" ");
        }
        //如果能自动增长
        if(column.autoGen()){
            sql.append("AUTO_INCREMENT").append(" ");
        }
        //如果有默认值，且不会自动增长
        if(!column.defaultValue().isEmpty() && !column.autoGen()){
            sql.append("DEFAULT ").append(column.defaultValue()).append(" ");
        }
        sql.append(";").append("\n");
        return sql.toString();
    }



    private void createTable() throws Exception {
        Column column= ColumnUtils.getKeyColumn(clazz);
        if(column==null){
            throw new Exception("a table must have a key column");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("create table if not exists `" + tableName + "` (\n");
        sql.append(getNewSqlByColumn(column));
        //如果是主键
        if(column.isKey()){
            sql.append("  PRIMARY KEY (`"+column.name()+"`)").append("\n");
        }
        sql.append(");");

        System.out.println(sql.toString());

        Connection con = JDBCUtils.getConnection();
        Statement stmt = con.createStatement();
        if (stmt.executeLargeUpdate(sql.toString()) == 0) {
            //System.out.println("create table "+tableName+" successfully");
        }else{
            System.out.println("failed to create table "+tableName);
        }
        JDBCUtils.closeResource(stmt, con);
    }

    //获取创建表时的字段sql
    private String getNewSqlByColumn(Column column){
        StringBuilder sql = new StringBuilder();
        sql.append(" `"+column.name()+"`").append(" ");
        sql.append(column.type()).append(" ");
        //如果不是主键，且可以为null
        if(column.canNull() && !column.isKey()){
            sql.append(" NULL").append(" ");
        }else {
            sql.append("NOT NULL").append(" ");
        }
        //如果能自动增长
        if(column.autoGen()){
            sql.append("AUTO_INCREMENT").append(" ");
        }
        //如果有默认值，且不会自动增长
        if(!column.defaultValue().isEmpty() && !column.autoGen()){
            sql.append("DEFAULT ").append(column.defaultValue()).append(" ");
        }
        sql.append(",").append("\n");
        return sql.toString();
    }


    private String getModifySqlByColumn(Column column){
        StringBuilder sql = new StringBuilder();
        sql.append("MODIFY COLUMN").append(" ");
        sql.append("`"+column.name()+"`").append(" ");
        sql.append(column.type()).append(" ");
        //如果不是主键，且可以为null
        if(column.canNull() && !column.isKey()){
            sql.append(" NULL").append(" ");
        }else {
            sql.append("NOT NULL").append(" ");
        }
        //如果能自动增长
        if(column.autoGen()){
            sql.append("AUTO_INCREMENT").append(" ");
        }
        //如果有默认值，且不会自动增长
        if(!column.defaultValue().isEmpty() && !column.autoGen()){
            sql.append("DEFAULT ").append(column.defaultValue()).append(" ");
        }

        int index=columnList.indexOf(column);
        if(index!=0 && index!=columnList.size()-1){
            String afterName=columnList.get(index-1).name();
            sql.append("AFTER ").append("`"+afterName+"`").append(" ");
        }

        sql.append(";").append("\n");
        return sql.toString();
    }


    /**
     * 获取表中所有字段名称
     *
     * @param tableName 表名
     * @return
     */
    public static List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        try {
            Connection con = JDBCUtils.getConnection();
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
        }catch (Exception e){
        }
        return columnNames;
    }



}
