package com.renchaigao.spider.dao.mapper;

import com.renchaigao.spider.dao.codes;

import java.util.ArrayList;
import java.util.List;

public interface codesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(codes record);

    int insertSelective(codes record);

    codes selectByPrimaryKey(Integer id);

    List<codes> selectAllCodes();

    int updateByPrimaryKeySelective(codes record);

    int updateByPrimaryKey(codes record);
}