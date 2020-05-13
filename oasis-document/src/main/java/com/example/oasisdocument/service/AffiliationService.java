package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;

import java.util.List;

public interface AffiliationService {
    Affiliation fetchEnById(String id);

    JSONArray fetchAffiliationList(int pageNum, int pageSize, String rankKey);

    JSONArray fetchAffiliationList(String refinement, int pageNum, int pageSize);
}
