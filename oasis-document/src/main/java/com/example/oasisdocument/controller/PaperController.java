package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.PaperInsertVO;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.service.AuthorService;
import com.example.oasisdocument.service.PaperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/query")
public class PaperController {
    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);
    @Autowired
    private PaperService paperService;
    /**
     * 初次数据查询
     *
     * @param query        : 查询关键字
     * @param returnFacets : 返回具体类型
     * @param pageNum      : 页号
     * @param pageSize     : 页大小
     * @return json
     */
    @GetMapping("/paper/list")
    public JSONObject queryPaper(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "returnFacets") String returnFacets,
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request,
            HttpServletResponse response) {
        return paperService.queryPaper(query.toLowerCase(),
                returnFacets.toLowerCase(), pageSize, pageNum,
                request, response);
    }

    /**
     * 进一步筛选查询
     *
     * @param refinements : 新的限制. year , title , conferences , terms , keywords , authors , affiliations
     * @param pageNum     : 页号
     * @param pageSize    : 页大小
     */
    @GetMapping("/paper/refine")
    public JSONObject queryPaperRefine(
            @CookieValue(name = "qid") String qid,
            @RequestParam(name = "refinements") List<String> refinements,
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        return paperService.queryPaperRefine(qid, refinements, pageNum, pageSize, request);
    }

    /**
     * 获取指定的查询id下的论文summary
     *
     * @param qid : query id
     */
    @GetMapping(path = "/paper/summary")
    public JSONObject getPaperSummary(@CookieValue(name = "qid") String qid,
                                      HttpServletRequest request) {
        return paperService.papersSummary(qid, request);
    }

    /**
     * 添加新paper接口
     *
     * @param paperVO : paper实体
     */
    @PostMapping(path = "/paper")
    public void insertPaper(@RequestBody JSONObject paperVO) {
        try {
            PaperInsertVO entity = paperVO.toJavaObject(PaperInsertVO.class);
            paperService.insertPaperVOEntity(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
