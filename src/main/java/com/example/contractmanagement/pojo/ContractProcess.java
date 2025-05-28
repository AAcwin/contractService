package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("contract_process")
public class ContractProcess {
    private String connum;
    private int type;
    @JsonIgnore
    private int state;
    private String userName;
    @JsonIgnore
    private String contend;
    private LocalDateTime time;
}
