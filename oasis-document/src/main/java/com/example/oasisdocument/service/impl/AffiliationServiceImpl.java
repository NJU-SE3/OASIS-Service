package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.service.AffiliationService;
import com.example.oasisdocument.service.BaseService;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.service.IntermeService;
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.oasisdocument.service.IntermeService.affCounterCollection;

@Service
public class AffiliationServiceImpl implements AffiliationService {
    private static final String refineSplitter = ":";

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private GeneralJsonVO generalJsonVO;
    @Autowired
    private PageHelper pageHelper;
    @Autowired
    private CounterService counterService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private IntermeService intermeService;

    @Override
    @Cacheable(cacheNames = "fetchEnById", unless = "#result==null")
    public Affiliation fetchEnById(String id) {
        Affiliation en = mongoTemplate.findById(id, Affiliation.class);
        if (null == en) throw new EntityNotFoundException();
        return en;
    }

    @Override
    public JSONObject fetchAffiliationList(int pageNum, int pageSize, String rankKey) {
        //一级缓存命中
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        JSONArray data = new JSONArray();
        long itemCnt = mongoTemplate.count(new Query(), Affiliation.class);
        Sort sort = Sort.by(Sort.Direction.DESC, rankKey);
        if (intermeService.intermeExist(affCounterCollection)) {
            List<CounterBaseEntity> bufferList = mongoTemplate.find(new Query()
                            .with(sort)
                            .with(pageable),
                    CounterBaseEntity.class, affCounterCollection);
            for (CounterBaseEntity entity : bufferList) {
                data.add(affBuffer2VO(entity));
            }
        } else {
            //缓存未命中 , 随机返回
            //异步缓存
            intermeService.genAffCounter();
            //随机返回pageable条目
            List<Affiliation> entities = mongoTemplate.find(new Query()
                    .with(pageable).with(sort), Affiliation.class);
            for (Affiliation en : entities) {
                CounterBaseEntity baseEntity = counterService.getSummaryInfo(en.getId());
                data.add(generalJsonVO.affiliation2VO(en, baseEntity));
            }
        }
        JSONObject ans = new JSONObject();
        ans.put("data", data);
        ans.put("itemCnt", itemCnt);
        return ans;
    }

    @Override
    @Cacheable(cacheNames = "fetchAffiliationList", unless = "#result==null")
    public JSONArray fetchAffiliationList(String refinement, int pageNum, int pageSize) {
        String[] strings = refinement.split(refineSplitter);
        if (strings.length != 2) throw new BadReqException();
        Set<Affiliation> affiliationList;
        String id = strings[1];
        if (strings[0].equals("field")) {
            Set<String> names = new HashSet<>();
            CounterBaseEntity entity = counterService.getSummaryInfo(id);
            if (null == entity) throw new EntityNotFoundException();
            affiliationList = new HashSet<>();
            for (String pid : entity.getPaperList()) {
                Paper paper = mongoTemplate.findById(pid, Paper.class);
                for (String name : Paper.getAllAffiliations(paper)) {
                    Affiliation affiliation = mongoTemplate
                            .findOne(Query.query(new Criteria("affiliationName").is(name)),
                                    Affiliation.class);
                    if (affiliation != null && !names.contains(name)) {
                        affiliationList.add(affiliation);
                        names.add(name);
                    }
                }
            }
        } else throw new BadReqException();

        JSONArray array = new JSONArray();
        for (Affiliation affiliation : affiliationList) {
            CounterBaseEntity baseEntity;
            try {
                baseEntity = counterService.getSummaryInfo(affiliation.getId());
            } catch (EntityNotFoundException e) {
                baseEntity = new CounterBaseEntity();
            }
            array.add(generalJsonVO.affiliation2VO(affiliation, baseEntity));
        }
        return pageHelper.sortAndPage(array, pageSize, pageNum);
    }

    private JSONObject affBuffer2VO(CounterBaseEntity entity) {
        String uid = entity.getCheckId();
        Affiliation aff = mongoTemplate.findById(uid, Affiliation.class);
        if (null == aff) return new JSONObject();

        return generalJsonVO.affiliation2VO(aff, entity);
    }

}
