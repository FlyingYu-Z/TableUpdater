# TableUpdater

A framework of creating or updating mysql table

**usage:**

````aidl

<dependency>
   <groupId>com.github.FlyingYu-Z</groupId>
   <artifactId>TableUpdater</artifactId>
   <version>1.0.1</version>
</dependency>

````

````aidl

import cn.beingyi.mysql.annotation.Column;
import cn.beingyi.mysql.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Table(name = "user")
public class User {
    @Column(name = "uid",type = "int(11)",autoGen = false,isKey = true,defaultValue = "0")
    private Integer uid;


    @Column(name = "username",type = "varchar(20)")
    private Integer username;

    @Column(name = "password",type = "varchar(20)")
    private String acceptOrderSum;

    @Column(name = "SignTime",type = "datetime")
    private Date SignTime;


}

````

```aidl

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

```

It will create table automatically.