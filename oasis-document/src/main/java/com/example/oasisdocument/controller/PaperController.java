package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.docs.Paper;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.service.PaperService;
import com.example.oasisdocument.utils.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class PaperController {
    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);

    @Autowired
    private PaperService paperService;

    @Autowired
    private PageHelper pageHelper;

    @GetMapping("/")
    public String run() {
        return "hello world";
    }

    //初次数据查询
    @GetMapping("/paper/list")
    public JSONObject queryPaper(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "returnFacets") String returnFacets,
            @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        //set id
        String qid = UUID.randomUUID().toString().replaceAll("-", "");
        ;
        HttpSession session = request.getSession();
//        List<String> keys = Arrays.stream(query.split(" "))
//                .filter((String s) -> !s.trim().isEmpty()).collect(Collectors.toList());
//        assert keys.size() >= 1;
        List<Paper> list = paperService.queryPaper(query, returnFacets.toLowerCase());
        session.setAttribute(qid, list);
        if (list.isEmpty()) {
            JSONObject object = new JSONObject();
            object.put("papers", list);
            object.put("qid", qid);
            return object;
        }
        //分页
        List<Paper> pagedList = pageHelper.of(list, pageSize, pageNum);
        if (null == pagedList) throw new BadReqException();
        JSONObject ans = new JSONObject();
        ans.put("papers", pagedList);
        ans.put("qid", qid);
        return ans;
    }

    /**
     * 进一步筛选查询
     *
     * @param refinements : 新的限制. year , title , conferences , terms , keywords , authors , affiliations
     */
    @GetMapping("/paper/refine")
    public List<Paper> queryPaperRefine(@RequestParam(name = "qid") String qid,
                                        @RequestParam(name = "refinements") List<String> refinements,
                                        @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                        HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        try {
            //查询全集
            List<Paper> list = (List<Paper>) session.getAttribute(qid);
            //添加新限制
            list = paperService.queryPaperRefine(list, refinements);
            if (list.isEmpty()) {
                return list;
            }
            //需要用到分页信息
            List<Paper> ans = pageHelper.of(list, pageSize, pageNum);
            if (null == ans) throw new BadReqException();
            return ans;
        } catch (BadReqException e) {
            throw e;
        } catch (RuntimeException e) {
            //400
            e.printStackTrace();
            throw new Exception();
        }
    }

    @PostMapping(path = "/paper")
    public void insertPaper(@RequestBody JSONObject paper) {
        Paper entity = paper.toJavaObject(Paper.class);
        paperService.insert(entity);
    }

    @GetMapping(path = "/paper/summary")
    public JSONObject getPaperSummary(@RequestParam(name = "qid") String qid,
                                      HttpServletRequest request) {
        //fetch list
        List<Paper> list = (List<Paper>) request.getSession().getAttribute(qid);
        return paperService.papersSummary(list);
    }
}
