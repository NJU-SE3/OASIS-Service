package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.*;
import com.example.oasisdocument.utils.JsonUtil;
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceDetailController {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AffiliationService affiliationService;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private CounterService counterService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private GeneralJsonVO generalJsonVO;
    @Autowired
    private PageHelper pageHelper;
    @Autowired
    private JsonUtil jsonUtil;

    /**
     * 获取单个领域具体信息
     */
    @GetMapping("/field/detail")
    public JSONObject fetchFieldDetail(@RequestParam(name = "id") String id) {
        Field en = fieldService.fetchEnById(id);
        CounterBaseEntity baseEntity = counterService.getSummaryInfo(id);
        return generalJsonVO.field2VO(en, baseEntity);
    }

    /**
     * 获取单个机构具体信息
     */
    @GetMapping("/affiliation/detail")
    public JSONObject fetchAffiliationDetail(@RequestParam(name = "id") String id) {
        Affiliation en = affiliationService.fetchEnById(id);
        CounterBaseEntity baseEntity = counterService.getSummaryInfo(id);
        return generalJsonVO.affiliation2VO(en, baseEntity);
    }

    /**
     * 获取单个会议具体信息
     */
    @GetMapping("/conference/detail")
    public JSONObject fetchConferenceDetail(@RequestParam(name = "id") String id) {
        Conference en = conferenceService.fetchEnById(id);
        CounterBaseEntity baseEntity = counterService.getSummaryInfo(id);
        return generalJsonVO.conference2VO(en, baseEntity);
    }

    /**
     * 获取论文列表
     *
     * @param id: 限制实体的 id
     */
    @GetMapping("/paper/list")
    public JSONArray fetchPaperList(@RequestParam(name = "id") String id) {
        return JSONArray.parseArray(JSON.toJSONString(paperService.fetchPaperList(id)));
    }
//======================================================================================================================================================

    /**
     * 机构列表获取
     * 分页 , 按照最多的引用数划分
     */
    @GetMapping("/affiliation/list")
    public Object fetchAffiliationList(
            @RequestParam(name = "refinement", defaultValue = "") final String refinement,
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(name = "rankKey", defaultValue = "activeness") String rankKey) {
        return refinement.isEmpty() ?
                affiliationService.fetchAffiliationList(pageNum, pageSize, rankKey) :        //不进行过滤的列表获取
                affiliationService.fetchAffiliationList(refinement, pageNum, pageSize);      //进行refinement列表获取
    }

    /**
     * 领域列表获取
     * 可以按照会议 id / 机构 id 获取
     */
    @GetMapping("/field/list")
    public Object fetchFieldList(@RequestParam(name = "refinement", defaultValue = "") final String refinement,
                                 @RequestParam(name = "pageNum", defaultValue = "0") final int pageNum,
                                 @RequestParam(name = "pageSize", defaultValue = "20") final int pageSize) {
        return refinement.isEmpty() ? reportService.getRankViaType("field", pageNum, pageSize) :
                fieldService.fetchFieldList(refinement, pageNum, pageSize);
    }

    /**
     * 会议列表获取
     */
    @GetMapping("/conference/list")
    public Object fetchConferenceList(@RequestParam(name = "refinement", defaultValue = "") String refinement,
                                      @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                      @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        return refinement.isEmpty() ? reportService.getRankViaType("conference", pageNum, pageSize) :
                conferenceService.fetchConferenceList(refinement, pageNum, pageSize);
    }
}
