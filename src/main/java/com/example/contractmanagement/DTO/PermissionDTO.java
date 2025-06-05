package com.example.contractmanagement.DTO;

import lombok.Data;

@Data
public class PermissionDTO {
    private boolean draft;
    private boolean countersign;
    private boolean finalize;
    private boolean sign;
    private boolean query;
    private boolean approve;
}
