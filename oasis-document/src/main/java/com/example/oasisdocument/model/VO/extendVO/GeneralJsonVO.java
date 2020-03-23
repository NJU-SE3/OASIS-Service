package com.example.oasisdocument.model.VO.extendVO;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.AffiliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneralJsonVO {
	@Autowired
	private AffiliationService affiliationService;

	public JSONObject author2VO(Author en, CounterBaseEntity baseEntity) {
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("authorName", en.getAuthorName());
		res.put("affiliationName", en.getAffiliationName());
		res.put("bioParagraphs", en.getBioParagraphs());
		updateCounter(res, baseEntity);
		return res;
	}

	public JSONObject affiliation2VO(Affiliation en, CounterBaseEntity baseEntity) {
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("affiliationName", en.getAffiliationName());
		updateCounter(res, baseEntity);
		return res;
	}

	public JSONObject field2VO(Field en, CounterBaseEntity baseEntity) {
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("fieldName", en.getFieldName());
		updateCounter(res, baseEntity);
		return res;
	}

	public JSONObject conference2VO(Conference en, CounterBaseEntity baseEntity) {
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("conferenceName", en.getConferenceName());
		updateCounter(res, baseEntity);
		return res;
	}

	private void updateCounter(JSONObject res, CounterBaseEntity baseEntity) {
		res.put("heat", baseEntity.getHeat());
		res.put("paperCount", baseEntity.getPaperCount());
		res.put("citationCount", baseEntity.getCitationCount());
		res.put("activeness", baseEntity.getActiveness());
		res.put("H_index", baseEntity.getH_index());
	}
}
