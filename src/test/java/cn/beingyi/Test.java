package cn.beingyi;

import cn.beingyi.mysql.core.Ready;
import cn.beingyi.mysql.core.ReadyCore;

public class Test {

    public static void main(String[] args) {
        Ready ready = new Ready();
        ready.setDbUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&&useSSL=true&serverTimezone=Asia/Shanghai");
        ready.setUserName("root");
        ready.setPassWord("123456");
        ready.setCharacterSet("utf8mb4");
        ready.setCollation("utf8mb4_general_ci");
        try {
            ReadyCore.init(ready)
            .initTable(
                    User.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
