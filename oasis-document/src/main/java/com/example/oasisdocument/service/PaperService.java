package com.example.oasisdocument.service;

import com.example.oasisdocument.docs.Paper;

import java.util.List;

public interface PaperService {
    List<Paper> queryPaper(List<String> keys, String returnFacets);

    List<Paper> queryPaperRefine(List<Paper> papers, List<String> refinements);

    void insert(Paper entity);
}
