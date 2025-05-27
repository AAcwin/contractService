package com.example.contractmanagement.service;

import java.util.List;

public interface RoleService {
    List<String> findByFunctionId(int funId);
    boolean containsFunction(String functionIds, int funId);
}
