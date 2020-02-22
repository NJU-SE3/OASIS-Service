package com.example.oasispaper.mapper;

import com.example.oasispaper.model.Paper;
import com.example.oasispaper.model.VO.PaperQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaperMapper {
    List<Paper> findById(int id);

    List<PaperQueryVO> queryAll(@Param("key") String key);
}
