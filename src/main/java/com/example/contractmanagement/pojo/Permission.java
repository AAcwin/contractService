package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("permission")
public class Permission {
    @JsonIgnore
    private String rolename;
    private boolean draft;
    private boolean countersign;
    private boolean finalize;
    private boolean sign;
    private boolean query;
    private boolean approve;
}
