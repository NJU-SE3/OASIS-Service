package com.example.oasisdocument.model.VO.extendVO;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.analysis.GraphEdge;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.model.graph.nodes.AuthorNeo;
import com.example.oasisdocument.service.AffiliationService;
import com.example.oasisdocument.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class GeneralJsonVO {
	@Autowired
	private AffiliationService affiliationService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private CounterService counterService;

	public JSONObject authorNeo2VO(AuthorNeo entity) {
		JSONObject res = new JSONObject();
		res.put("id", entity.getXid());
		res.put("authorName", entity.getAuthorName());
		return res;
	}

	public JSONObject author2VO(Author en, CounterBaseEntity baseEntity) {
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("authorName", en.getAuthorName());
		res.put("trends", en.getPaperTrends());
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

	public JSONObject authorEdge2VO(GraphEdge en) {
		Author begin = mongoTemplate.findById(en.getBegin(), Author.class),
				end = mongoTemplate.findById(en.getEnd(), Author.class);
		JSONObject ans = new JSONObject();
		ans.put("begin", author2VO(begin, counterService.getSummaryInfo(en.getBegin())));
		ans.put("end", author2VO(end, counterService.getSummaryInfo(en.getEnd())));
		ans.put("weight", en.getWeight());
		return ans;
	}

	public JSONObject fieldEdge2VO(GraphEdge edge) {
		Field begin = mongoTemplate.findById(edge.getBegin(), Field.class),
				end = mongoTemplate.findById(edge.getEnd(), Field.class);
		JSONObject ans = new JSONObject();
		ans.put("begin", field2VO(begin, counterService.getSummaryInfo(edge.getBegin())));
		ans.put("end", field2VO(end, counterService.getSummaryInfo(edge.getEnd())));
		ans.put("weight", edge.getWeight());
		return ans;
	}

	public JSONObject affiliationEdge2VO(GraphEdge edge) {
		Affiliation begin = mongoTemplate.findById(edge.getBegin(), Affiliation.class),
				end = mongoTemplate.findById(edge.getEnd(), Affiliation.class);
		JSONObject ans = new JSONObject();
		ans.put("begin", affiliation2VO(begin, counterService.getSummaryInfo(edge.getBegin())));
		ans.put("end", affiliation2VO(end, counterService.getSummaryInfo(edge.getEnd())));
		ans.put("weight", edge.getWeight());
		return ans;
	}

	private void updateCounter(JSONObject res, CounterBaseEntity baseEntity) {
		res.put("heat", baseEntity.getHeat());
		res.put("paperCount", baseEntity.getPaperCount());
		res.put("citationCount", baseEntity.getCitationCount());
		res.put("activeness", baseEntity.getActiveness());
		res.put("H_index", baseEntity.getH_index());
	}

}
