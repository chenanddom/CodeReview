package com.itdom.mapper;

import com.itdom.entity.Management;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

@Mapper
public interface ManagementMapper {

    public Management getById(@Param("id") Integer id);

    public List<Management> getById2(@Param("id") Integer id);

    public List<Management> getById3(@Param("id") Integer id);

    public List<Management> getById4(Map<String,Object> item);

    public List<Management> list();

    public Management selectNoneClearCache(@Param("id") Integer id);

    public Integer updateById(@Param("id")Integer id);

    public Management findById1(@Param("id")Integer id);

    public Management findById2(@Param("id")Integer id);




}
