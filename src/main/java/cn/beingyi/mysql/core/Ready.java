package cn.beingyi.mysql.core;

import cn.beingyi.mysql.utils.Build;

public class Ready {

    static Ready ready;
    private String DbUrl;//数据库地址
    private String UserName;
    private String PassWord;

    public static Ready getInstance() {
        if(ready==null){
            ready=new Ready();
        }
        return ready;
    }

    public String getDbUrl() {
        return DbUrl;
    }

    public void setDbUrl(String dbUrl) {
        DbUrl = dbUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
