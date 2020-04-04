package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
	private GeneralJsonVO generalJsonVO;

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
	 * 获取单个作者具体信息
	 */
	@GetMapping("/author/detail")
	public JSONObject fetchAuthorDetail(@RequestParam(name = "id") String id) {
		Author en = authorService.fetchEnById(id);
		CounterBaseEntity baseEntity = counterService.getSummaryInfo(id);
		return generalJsonVO.author2VO(en, baseEntity);
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
	 * 获取作者列表
	 * 可以按照领域、机构进行额外的筛选
	 */
	@GetMapping("/author/list")
	public JSONArray fetchAuthorList(@RequestParam(name = "refinement", defaultValue = "") String refinement,
									 @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
									 @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
		//需要考察是否需要筛选
		List<Author> authorList = refinement.isEmpty() ? authorService.fetchAuthorList(pageNum, pageSize) :
				authorService.fetchAuthorList(refinement);
		JSONArray array = new JSONArray();
		for (Author author : authorList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(author.getId());
			array.add(generalJsonVO.author2VO(author, baseEntity));
		}
		return array;
	}

	/**
	 * 机构列表获取
	 * 分页 , 按照最多的引用数划分
	 */
	@GetMapping("/affiliation/list")
	public JSONArray fetchAffiliationList(
			@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
		List<Affiliation> affiliationList = affiliationService.fetchAffiliationList(pageNum, pageSize);

		JSONArray array = new JSONArray();
		for (Affiliation affiliation : affiliationList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(affiliation.getId());
			array.add(generalJsonVO.affiliation2VO(affiliation, baseEntity));
		}
		return array;
	}

	/**
	 * 领域列表获取
	 * 可以按照会议 id 获取
	 */
	@GetMapping("/field/list")
	public JSONArray fetchFieldList(@RequestParam(name = "refinement", defaultValue = "") String refinement,
									@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
									@RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
		List<Field> fieldList = refinement.isEmpty() ? fieldService.fetchFieldList(pageNum, pageSize) :
				fieldService.fetchFieldList(refinement);
		JSONArray array = new JSONArray();
		for (Field field : fieldList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(field.getId());
			array.add(generalJsonVO.field2VO(field, baseEntity));
		}
		return array;
	}

	/**
	 * 会议列表获取
	 */
	@GetMapping("/conference/list")
	public JSONArray fetchConferenceList(@RequestParam(name = "refinement", defaultValue = "") String refinement,
										 @RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
										 @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
		List<Conference> conferenceList =
				refinement.isEmpty() ? conferenceService.fetchConferenceList(pageNum, pageSize) :
						conferenceService.fetchConferenceList(refinement);
		//Construct conference to result
		JSONArray array = new JSONArray();
		for (Conference conference : conferenceList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(conference.getId());
			array.add(generalJsonVO.conference2VO(conference, baseEntity));
		}
		return array;
	}
}
