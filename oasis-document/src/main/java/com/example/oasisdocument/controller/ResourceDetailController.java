package com.example.oasisdocument.controller;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.service.AffiliationService;
import com.example.oasisdocument.service.AuthorService;
import com.example.oasisdocument.service.ConferenceService;
import com.example.oasisdocument.service.FieldService;
import com.example.oasisdocument.utils.JsonUtil;
import org.aspectj.apache.bcel.classfile.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

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
	private JsonUtil jsonUtil;

	@GetMapping("/field/detail")
	public String fetchFieldDetail(@RequestParam(name = "id") String id) {
		Field en = fieldService.fetchEnById(id);
		return jsonUtil.objToJsonStr(en);
	}

	@GetMapping("/author/detail")
	public String fetchAuthorDetail(@RequestParam(name = "id") String id) {
		Author en = authorService.fetchEnById(id);
		return jsonUtil.objToJsonStr(en);
	}

	@GetMapping("/affiliation/detail")
	public String fetchAffiliationDetail(@RequestParam(name = "id") String id) {
		Affiliation en = affiliationService.fetchEnById(id);
		return jsonUtil.objToJsonStr(en);
	}

	@GetMapping("/conference/detail")
	public String fetchConferenceDetail(@RequestParam(name = "id") String id) {
		Conference en = conferenceService.fetchEnById(id);
		return jsonUtil.objToJsonStr(en);
	}
}
