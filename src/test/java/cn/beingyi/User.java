package cn.beingyi;

import cn.beingyi.mysql.annotation.Column;
import cn.beingyi.mysql.annotation.Table;

import java.util.Date;

@Table(name = "user")
public class User {
    @Column(name = "uid",type = "int(11)",isId = true,autoGen = false,isKey = true,defaultValue = "0")
    private Integer uid;


    @Column(name = "username",type = "varchar(20)")
    private Integer username;

    @Column(name = "password",type = "varchar(20)")
    private String acceptOrderSum;

    @Column(name = "SignTime",type = "datetime")
    private Date SignTime;


}
