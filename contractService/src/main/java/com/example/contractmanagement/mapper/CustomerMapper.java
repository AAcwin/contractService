package com.example.contractmanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.contractmanagement.pojo.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}