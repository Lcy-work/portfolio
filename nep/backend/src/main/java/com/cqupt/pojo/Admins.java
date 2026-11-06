package com.cqupt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("admins")
public class Admins {
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer adminId;
    private String adminCode;
    private String password;
    private String adminName;
    private String phone;
    private String remarks;
}