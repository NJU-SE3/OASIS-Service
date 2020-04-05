package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.model.VO.PaperInsertVO;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.service.AuthorService;
import com.example.oasisdocument.service.PaperService;
import com.example.oasisdocument.utils.CookieUtil;
import com.example.oasisdocument.utils.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/query")
public class PaperController {
    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);
    @Autowired
    private PaperService paperService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private PageHelper pageHelper;
    @Autowired
    private GeneralJsonVO generalJsonVO;

    @GetMapping("/")
    public String run() {
        return "hello world";
    }

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
        //set id
        String qid = UUID.randomUUID().toString().replaceAll("-", "");
        JSONArray list = paperService.queryPaper(query.toLowerCase(),
                returnFacets.toLowerCase());
        if (list.isEmpty()) {
            JSONObject ans = new JSONObject();
            ans.put("papers", list);
            ans.put("itemCnt", list.size());
            return ans;
        }
        //分页
        List<Object> pagedList = pageHelper.of(list, pageSize, pageNum);
        if (null == pagedList) throw new BadReqException();
        JSONArray arr = new JSONArray();
        //convert from paged list
        pagedList.forEach((Object obj) -> {
            Paper paper = JSON.parseObject(String.valueOf(obj), Paper.class);
            arr.add(generalJsonVO.paper2BriefVO(paper));
        });
        request.getSession().setAttribute(qid, list);
        cookieUtil.set(response, "qid", qid);
        JSONObject ans = new JSONObject();
        ans.put("papers", arr);
        ans.put("itemCnt", list.size());
        return ans;
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
            HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        try {
            //查询全集
            JSONArray list = (JSONArray) session.getAttribute(qid);
            //添加新限制
            list = paperService.queryPaperRefine(list, refinements);
            if (list.isEmpty()) {
                JSONObject ans = new JSONObject();
                ans.put("papers", list);
                ans.put("itemCnt", list.size());
                return ans;
            }
            //需要用到分页信息
            List<Object> pagedList = pageHelper.of(list, pageSize, pageNum);
            if (null == pagedList) throw new BadReqException();

            JSONObject t = new JSONObject();
            JSONArray arr = new JSONArray();
            //convert from paged list
            pagedList.forEach((Object obj) -> {
                Paper paper = JSON.parseObject(String.valueOf(obj), Paper.class);
                arr.add(generalJsonVO.paper2BriefVO(paper));
            });
            t.put("papers", arr);
            t.put("itemCnt", list.size());
            return t;
        } catch (BadReqException e) {
            throw e;
        } catch (RuntimeException e) {
            //400
            e.printStackTrace();
            throw new Exception();
        }
    }

    /**
     * 获取指定的查询id下的论文summary
     *
     * @param qid : query id
     */
    @GetMapping(path = "/paper/summary")
    public JSONObject getPaperSummary(@CookieValue(name = "qid") String qid,
                                      HttpServletRequest request) {
        //fetch list
        JSONArray list = (JSONArray) request.getSession().getAttribute(qid);
        return paperService.papersSummary(list);
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

    /**
     * 作者添加接口
     *
     * @param author : author实体
     */
    @PostMapping(path = "/author")
    public void insertAuthor(@RequestBody JSONObject author) {
        try {
            Author entity = author.toJavaObject(Author.class);
            authorService.insert(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
