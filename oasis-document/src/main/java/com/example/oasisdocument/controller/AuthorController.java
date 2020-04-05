package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.service.AuthorService;
import com.example.oasisdocument.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class AuthorController {
	@Autowired
	private AuthorService authorService;
	@Autowired
	private ReportService reportService;

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

	/**
	 * 获取单个作者具体信息
	 */
	@GetMapping("/author/detail")
	public JSONObject fetchAuthorDetail(@RequestParam(name = "id") String id) {
		return authorService.fetchEnById(id);
	}

	/**
	 * 获取作者列表
	 * 可以按照领域、机构进行额外的筛选
	 */
	@GetMapping("/author/list")
	public Object fetchAuthorList(@RequestParam(name = "refinement", defaultValue = "") String refinement,
								  @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
								  @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
								  HttpServletRequest request,
								  HttpServletResponse response) {
		//需要考察是否需要筛选
		return refinement.isEmpty() ? reportService.getRankViaType("author", pageNum, pageSize) :
				authorService.fetchAuthorList(refinement, pageNum, pageSize);
	}

	@GetMapping("/author/fieldSummary")
	public JSONArray fetchAuthorSummaryUponField() {
		return authorService.fetchAuthorSummaryUponField();
	}

	@GetMapping("/author/list/refine")
	public JSONArray fetchAuthorWithRefine(@RequestParam(name = "fieldName") List<String> fieldName,
										   @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
										   @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
		return authorService.fetchAuthorWithRefine(fieldName, pageNum, pageSize);
	}
}
