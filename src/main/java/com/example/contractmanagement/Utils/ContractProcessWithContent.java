package com.example.contractmanagement.Utils;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ContractProcessWithContent{
    private String userName;
    private String contend;
    private LocalDateTime time;
}
