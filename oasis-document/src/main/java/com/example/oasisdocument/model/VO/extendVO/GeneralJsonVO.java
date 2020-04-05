package com.example.oasisdocument.model.VO.extendVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.model.graph.nodes.AffiliationNeo;
import com.example.oasisdocument.model.graph.nodes.AuthorNeo;
import com.example.oasisdocument.model.graph.nodes.FieldNeo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GeneralJsonVO {
	@Autowired
	private MongoTemplate mongoTemplate;

	public JSONObject authorNeo2VO(AuthorNeo entity) {
		JSONObject res = new JSONObject();
		res.put("id", entity.getXid());
		res.put("authorName", entity.getAuthorName());
		return res;
	}

	public JSONObject fieldNeo2VO(FieldNeo entity) {
		JSONObject res = new JSONObject();
		res.put("id", entity.getXid());
		res.put("fieldName", entity.getFieldName());
		return res;
	}

	public JSONObject affNeo2VO(AffiliationNeo neo) {
		JSONObject res = new JSONObject();
		res.put("id", neo.getXid());
		res.put("affiliationName", neo.getAffiliationName());
		return res;
	}

	public JSONObject author2VO(Author en, CounterBaseEntity baseEntity) {
		JSONObject res = new JSONObject();
		res.put("id", en.getId());
		res.put("authorName", en.getAuthorName());
		res.put("trends", en.getPaperTrends());
		res.put("affiliationName", en.getAffiliationName());
		res.put("bioParagraphs", en.getBioParagraphs());
		res.put("field", en.getField());
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
		res.put("year", en.getYear());
		updateCounter(res, baseEntity);
		return res;
	}

	private void updateCounter(JSONObject res, CounterBaseEntity baseEntity) {
		res.put("heat", baseEntity.getHeat());
		res.put("paperCount", baseEntity.getPaperCount());
		res.put("citationCount", baseEntity.getCitationCount());
		res.put("activeness", baseEntity.getActiveness());
		res.put("H_index", baseEntity.getH_index());
		res.put("authorCount", baseEntity.getAuthorCount());
	}

	public JSONObject paper2BriefVO(Paper paper) {
		JSONObject ans = new JSONObject();
		ans.put("id", paper.getId());
		ans.put("title", paper.getTitle());
		ans.put("conference", paper.getConference());
		ans.put("terms", Paper.getAllTerms(paper));
		ans.put("keywords", paper.getKeywords());
		ans.put("pdfLink", paper.getPdfLink());
		ans.put("citationCount", paper.getCitationCount());
		ans.put("referenceCount", paper.getReferenceCount());
		ans.put("year", paper.getYear());
		ans.put("authors", paper.getAuthors());
		ans.put("affiliations", paper.getAffiliations());
		// put conference
		String conName = paper.getConference();
		Conference con = mongoTemplate.findOne(Query.query(new Criteria("conferenceName").is(conName)),
				Conference.class);
		JSONObject confObj = new JSONObject();
		confObj.put("conferenceName", conName);
		confObj.put("id", con != null ? con.getId() : "NAN");
		ans.put("conferenceLink", confObj);
		// put author
		Set<String> authorNames = Paper.getAllAuthors(paper);
		JSONArray authorArr = new JSONArray();
		for (String name : authorNames) {
			Author author = mongoTemplate.findOne(Query.query(new Criteria("authorName").is(name)),
					Author.class);
			JSONObject authorObj = new JSONObject();
			authorObj.put("authorName", name);
			authorObj.put("id", author != null ? author.getId() : "NAN");
			authorArr.add(authorObj);
		}
		ans.put("authorLink", authorArr);
		//put affiliation
		Set<String> affiliationNames = Paper.getAllAffiliations(paper);
		JSONArray affArr = new JSONArray();
		for (String name : affiliationNames) {
			Affiliation affiliation = mongoTemplate.findOne(
					Query.query(new Criteria("affiliationName").is(name)), Affiliation.class);
			JSONObject affObj = new JSONObject();
			affObj.put("affiliationName", name);
			affObj.put("id", affiliation != null ? affiliation.getId() : "NAN");
			affArr.add(affObj);
		}
		ans.put("affiliationLink", affArr);
		//put field
		Set<String> fieldNames = Paper.getAllTerms(paper);
		JSONArray fieldArr = new JSONArray();
		for (String name : fieldNames) {
			Field field = mongoTemplate.findOne(
					Query.query(new Criteria("fieldName").is(name)), Field.class);
			JSONObject obj = new JSONObject();
			obj.put("id", field != null ? field.getId() : "NAN");
			obj.put("fieldName", name);
			fieldArr.add(obj);
		}
		ans.put("fieldLink", fieldArr);
		return ans;
	}

}
