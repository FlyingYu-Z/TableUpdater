package cn.beingyi;

import cn.beingyi.mysql.annotation.Column;
import cn.beingyi.mysql.annotation.Table;

import java.util.Date;

@Table(name = "user",sql = "SET sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));")
public class User {
    @Column(name = "uid",type = "int(11)",autoGen = false,isKey = true,defaultValue = "0")
    private Integer uid;


    @Column(name = "username",type = "varchar(20)",isKey = true)
    private Integer username;

    @Column(name = "password",type = "varchar(20)")
    private String acceptOrderSum;

    @Column(name = "SignTime",type = "datetime")
    private Date SignTime;


}
