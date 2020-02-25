package com.example.oasispaper.mapper;

import com.example.oasispaper.model.VO.AuthorVO;
import com.example.oasispaper.model.VO.PaperVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QueryMapper {
    List<PaperVO> queryAll(@Param("key") String key);

    List<AuthorVO> queryByPaperId(@Param("paper_id") long paperId);
}
