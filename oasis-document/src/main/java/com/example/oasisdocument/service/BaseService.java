package com.example.oasisdocument.service;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * dao 和 service 的中间过渡层 , 提供简化的级联查询接口
 */
@Service
public class BaseService {
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 根据作者id获取所有论文
	 *
	 * @param authorId : 作者id
	 */
	public List<Paper> getPapersByAuthorId(String authorId) {
		Author entity = mongoTemplate.findById(authorId, Author.class);
		if (null == entity) throw new EntityNotFoundException();
		return getPapersByAuthorName(entity.getAuthorName());
	}

	/**
	 * 根据作者名获取论文
	 *
	 * @param authorName : 作者名称
	 */
	public List<Paper> getPapersByAuthorName(String authorName) {
		final String authorCol = "authors";
		return mongoTemplate.find(
				Query.query(Criteria.where(authorCol).regex(authorName)),
				Paper.class);
	}

	/**
	 * 根据会议id查询论文列表
	 */
	public List<Paper> getPapersByConferenceId(String cid) {
		Conference entity = mongoTemplate.findById(cid, Conference.class);
		if (null == entity) throw new EntityNotFoundException();
		return getPapersByConferenceName(entity.getConferenceName());
	}

	/**
	 * 根据会议名称查询论文列表
	 */
	public List<Paper> getPapersByConferenceName(String conName) {
		final String conCol = "conference";
		return mongoTemplate.find(
				Query.query(Criteria.where(conCol).is(conName)),
				Paper.class);
	}

	/**
	 * 根据机构获取作者列表
	 *
	 * @param affId: 机构id
	 * @return : 满足条件作者列表
	 */
	public List<Author> getAuthorsByAffId(String affId) {
		Affiliation entity = mongoTemplate.findById(affId, Affiliation.class);
		if (null == entity) throw new EntityNotFoundException();
		return getAuthorsByAffName(entity.getAffiliationName());
	}

	/**
	 * 根据机构名获取作者列表
	 *
	 * @param affName : 机构名称
	 */
	public List<Author> getAuthorsByAffName(String affName) {
		final String affCol = "affiliationName";
		return mongoTemplate.find(
				Query.query(Criteria.where(affCol).is(affName)),
				Author.class
		);
	}


}