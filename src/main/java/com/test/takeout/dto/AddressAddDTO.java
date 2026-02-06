package com.test.takeout.dto;
import lombok.Data;

@Data
public class AddressAddDTO {
    private String consignee;
    private String phone;
    private String sex;
    private String province;
    private String city;
    private String district;
    private String detail;
    private Integer isDefault;
}
