package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.PaperBriefVO;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.*;
import com.example.oasisdocument.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
	private InitializationService initializationService;
	@Autowired
	private PaperService paperService;
	@Autowired
	private GeneralJsonVO generalJsonVO;

	@Autowired
	private JsonUtil jsonUtil;

	@GetMapping("/field/detail")
	public JSONObject fetchFieldDetail(@RequestParam(name = "id") String id) {
		Field en = fieldService.fetchEnById(id);
		CounterBaseEntity baseEntity = initializationService.getSummaryInfo(id);
		return generalJsonVO.field2VO(en, baseEntity);
	}

	@GetMapping("/author/detail")
	public JSONObject fetchAuthorDetail(@RequestParam(name = "id") String id) {
		Author en = authorService.fetchEnById(id);
		CounterBaseEntity baseEntity = initializationService.getSummaryInfo(id);
		return generalJsonVO.author2VO(en, baseEntity);
	}

	@GetMapping("/affiliation/detail")
	public JSONObject fetchAffiliationDetail(@RequestParam(name = "id") String id) {
		Affiliation en = affiliationService.fetchEnById(id);
		CounterBaseEntity baseEntity = initializationService.getSummaryInfo(id);
		return generalJsonVO.affiliation2VO(en, baseEntity);
	}

	@GetMapping("/conference/detail")
	public JSONObject fetchConferenceDetail(@RequestParam(name = "id") String id) {
		Conference en = conferenceService.fetchEnById(id);
		CounterBaseEntity baseEntity = initializationService.getSummaryInfo(id);
		return generalJsonVO.conference2VO(en, baseEntity);
	}

	@GetMapping("/paper/list")
	public JSONArray fetchPaperList(@RequestParam(name = "id") String id) {
		List<PaperBriefVO> paperList = paperService.fetchPaperList(id).stream()
				.map(PaperBriefVO::PO2VO)
				.collect(Collectors.toList());
		return JSONArray.parseArray(JSON.toJSONString(paperList));
	}

	@GetMapping("/author/list")
	public JSONArray fetchAuthorList(@RequestParam(name = "id", defaultValue = "") String id) {
		List<Author> authorList = id.isEmpty() ? authorService.fetchAuthorList() :
				authorService.fetchAuthorList(id);
		JSONArray array = new JSONArray();
		for (Author author : authorList) {
			CounterBaseEntity baseEntity = initializationService.getSummaryInfo(author.getId());
			array.add(generalJsonVO.author2VO(author, baseEntity));
		}
		return array;
	}
}
