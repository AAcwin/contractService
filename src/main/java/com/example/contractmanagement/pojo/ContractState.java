package com.example.contractmanagement.pojo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("contract_state")
public class ContractState {
    private String conname;
    private int type;
    private LocalDateTime datetime;
}
