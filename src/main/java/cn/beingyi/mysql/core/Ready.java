package cn.beingyi.mysql.core;

import cn.beingyi.mysql.utils.Build;

public class Ready {

    private String DbUrl;//数据库地址
    private String UserName;
    private String PassWord;
    private String CharacterSet = "utf8";
    private String Collation = "utf8_general_ci";

    public Ready() {
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

    public String getCharacterSet() {
        return CharacterSet;
    }

    public void setCharacterSet(String characterSet) {
        CharacterSet = characterSet;
    }

    public String getCollation() {
        return Collation;
    }

    public void setCollation(String collation) {
        Collation = collation;
    }
}
