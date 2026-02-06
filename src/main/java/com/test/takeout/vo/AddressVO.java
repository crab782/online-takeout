package com.test.takeout.vo;
import lombok.Data;

@Data
public class AddressVO {
    private Long id;
    private String consignee;
    private String phone;
    private String sex;
    private String province;
    private String city;
    private String district;
    private String detail;
    private Integer isDefault;
}
