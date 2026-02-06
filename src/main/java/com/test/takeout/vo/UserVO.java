package com.test.takeout.vo;
import lombok.Data;
import java.util.Date;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String phone;
    private String nickname;
    private String avatar;
    private String gender;
    private Date birthday;
    private String status;
}
