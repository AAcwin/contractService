package com.example.contractmanagement.pojo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("contract")
public class Contract {
    private String num;
    private String name;
    private String customer;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String content;
    private String userName;
}
