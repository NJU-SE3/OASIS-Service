package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    public JSONObject queryPaper(HttpServletRequest request,
                                 @RequestParam(name = "query") String query,
                                 @RequestParam(name = "returnFacets") String returnFacets,
                                 @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                 @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        HttpSession session = request.getSession();
        List<String> keys = Arrays.asList(query.split(" ")).stream()
                .filter((String s) -> !s.isBlank()).collect(Collectors.toList());
        assert keys.size() >= 1;
        List<Paper> list = paperService.queryPaper(keys, returnFacets.toLowerCase());
        if (list.isEmpty()) return JSONArray.parseObject(JSON.toJSONString(list));
        //分页
        List<Paper> pagedList = pageHelper.of(list, pageSize, pageNum);
        if (null == pagedList) throw new BadReqException();
        //根据ip进行session存储
        session.setAttribute(request.getRemoteAddr(), list);
        session.setAttribute(request.getRemoteAddr() + "reqinfo",
                request.getParameterMap());
        //装配
        JSONObject summary = paperService.papersSummary(list);
        JSONObject ans = new JSONObject();
        ans.put("summary", summary);
        ans.put("papers", pagedList);
        return ans;
    }

    /**
     * 进一步筛选查询
     *
     * @param refinements : 新的限制. year , title , conferences , terms , keywords , authors , affiliations
     */
    @GetMapping("/paper/refine")
    public JSONArray queryPaperRefine(HttpServletRequest request,
                                      @RequestParam(name = "refinements") List<String> refinements) throws Exception {
        HttpSession session = request.getSession();
        try {
            //查询全集
            List<Paper> list = (List<Paper>) session.getAttribute(request.getRemoteAddr());
            Map<String, String[]> params =
                    (Map<String, String[]>) session.getAttribute(request.getRemoteAddr() + "reqinfo");
            //添加新限制
            list = paperService.queryPaperRefine(list, refinements);
            if (list.isEmpty()) {
                return new JSONArray();
            }
            int pageNum = Integer.parseInt(params.getOrDefault("pageNum", new String[]{"0"})[0]),
                    pageSize = Integer.parseInt(params.getOrDefault("pageSize", new String[]{"10"})[0]);
            List<Paper> ans = pageHelper.of(list, pageSize, pageNum);
            if (null == ans) throw new BadReqException();
            return JSONArray.parseArray(JSON.toJSONString(ans));
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

}
