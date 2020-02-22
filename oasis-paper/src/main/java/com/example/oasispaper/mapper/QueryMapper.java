package com.example.oasispaper.mapper;

import com.example.oasispaper.model.VO.PaperQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QueryMapper {
    //多关键字全表模糊查找
    List<PaperQueryVO> queryAll(@Param("key") String key);
}
