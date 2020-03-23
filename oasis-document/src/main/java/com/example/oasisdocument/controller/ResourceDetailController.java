package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONObject;
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
	private JsonUtil jsonUtil;

	@GetMapping("/field/detail")
	public JSONObject fetchFieldDetail(@RequestParam(name = "id") String id) throws Exception {
		Field en = fieldService.fetchEnById(id);
		CounterBaseEntity baseEntity = initializationService.getSummaryInfo(id);
		JSONObject res = new JSONObject();

		//组装
		res.put("id", en.getId());
		res.put("fieldName", en.getFieldName());
		res.put("paperCount", baseEntity.getPaperCount());
		res.put("citationCount", baseEntity.getCitationCount());
		res.put("activeness", baseEntity.getActiveness());
		return res;
	}

	@GetMapping("/author/detail")
	public JSONObject fetchAuthorDetail(@RequestParam(name = "id") String id) {
		Author en = authorService.fetchEnById(id);
		CounterBaseEntity baseEntity = initializationService.getSummaryInfo(id);
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("authorName", en.getAuthorName());
		res.put("affiliationName", en.getAffiliationName());
		res.put("bioParagraphs", en.getBioParagraphs());
		res.put("field", en.getField());
		res.put("photoUrl", en.getPhotoUrl());
		res.put("paperCount", baseEntity.getPaperCount());
		res.put("citationCount", baseEntity.getCitationCount());
		res.put("activeness", baseEntity.getActiveness());
		res.put("H_index", baseEntity.getH_index());
		return res;
	}

	@GetMapping("/affiliation/detail")
	public JSONObject fetchAffiliationDetail(@RequestParam(name = "id") String id) {
		Affiliation en = affiliationService.fetchEnById(id);
		CounterBaseEntity baseEntity = initializationService.getSummaryInfo(id);
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("affiliationName", en.getAffiliationName());
		res.put("authorCount", affiliationService.fetchAuthorsByAffiliationName(en.getAffiliationName()).size());
		res.put("citationCount", baseEntity.getCitationCount());
		res.put("paperCount", baseEntity.getPaperCount());
		res.put("activeness", baseEntity.getActiveness());
		res.put("H_index", baseEntity.getH_index());
		return res;
	}

	@GetMapping("/conference/detail")
	public JSONObject fetchConferenceDetail(@RequestParam(name = "id") String id) {
		Conference en = conferenceService.fetchEnById(id);
		CounterBaseEntity baseEntity = initializationService.getSummaryInfo(id);
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("conferenceName", en.getConferenceName());
		res.put("citationCount", baseEntity.getCitationCount());
		res.put("paperCount", baseEntity.getPaperCount());
		res.put("activeness", baseEntity.getActiveness());
		res.put("H_index", baseEntity.getH_index());
		return res;

	}

}
