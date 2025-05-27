package com.example.contractmanagement.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("contract_process")
public class ContractProcess {
    private String connum;
    private int type;
    private int state;
    private String userName;
    private String contend;
    private LocalDateTime time;
}
