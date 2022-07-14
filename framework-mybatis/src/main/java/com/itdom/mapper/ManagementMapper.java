package com.itdom.mapper;

import com.itdom.entity.Management;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ManagementMapper {

    public List<Management> getById(@Param("id") Integer id);

    public List<Management> getById2(@Param("id") Integer id);

    public List<Management> getById3(@Param("id") Integer id);

    public List<Management> getById4(Map<String,Object> item);
}
