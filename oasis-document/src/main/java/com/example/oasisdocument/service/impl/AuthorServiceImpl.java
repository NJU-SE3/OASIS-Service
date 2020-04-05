package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.service.AuthorService;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
	private static final String refineSplitter = ":";

	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private CounterService counterService;
	@Autowired
	private GeneralJsonVO generalJsonVO;
	@Autowired
	private PageHelper pageHelper;

	//author 导入
	//涉及到 affiliation 和 field 的导入
	@Override
	@Async
	public void insert(Author entity) {
		if (null == mongoTemplate.findById(entity.getId(), Author.class)) {
			String affName = entity.getAffiliationName();
			//导入机构
			Affiliation affEn = mongoTemplate.findOne(Query.query(new Criteria("affiliationName").is(affName)),
					Affiliation.class);
			if (null == affEn) {
				if (!affName.isEmpty()) {
					affEn = new Affiliation();
					affEn.setAffiliationName(affName);
					affEn = mongoTemplate.save(affEn);
				}
			}
			authorRepository.save(entity);
		}
	}

	@Override
	@Cacheable(cacheNames = "fetchEnById", unless = "#result==null")
	public JSONObject fetchEnById(String id) {
		Author en = mongoTemplate.findById(id, Author.class);
		if (null == en) throw new EntityNotFoundException();
		CounterBaseEntity baseEntity = counterService.getSummaryInfo(id);
		return generalJsonVO.author2VO(en, baseEntity);
	}

	@Override
	@Cacheable(cacheNames = "fetchAuthorList", unless = "#result==null")
	public JSONArray fetchAuthorList(int pageNum, int pageSize) {
		List<Author> authorList = mongoTemplate.findAll(Author.class);
		JSONArray array = new JSONArray();
		for (Author author : authorList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(author.getId());
			array.add(generalJsonVO.author2VO(author, baseEntity));
		}
		return pageHelper.sortAndPage(array, pageSize, pageNum);
	}

	//查找某一个限定条件下的作者列表 , 可以是机构 , 领域限制
	@Override
	@Cacheable(cacheNames = "fetchAuthorList", unless = "#result==null")
	public JSONArray fetchAuthorList(String refinement, int pageNum, int pageSize) {
		final String affiCol = "affiliationName", fieldCol = "field", auName = "authorName";
		String[] strings = refinement.split(refineSplitter);
		if (strings.length != 2) throw new BadReqException();
		String id = strings[1];
		List<Author> authorList;
		if (strings[0].equals("affiliation")) {
			Affiliation aff = mongoTemplate.findById(id, Affiliation.class);
			if (null == aff) throw new EntityNotFoundException();
			authorList = mongoTemplate.find(Query.query(new Criteria(affiCol).is(aff.getAffiliationName())),
					Author.class);
		} else if (strings[0].equals("field")) {
			CounterBaseEntity entity = counterService.getSummaryInfo(id);
			if (null == entity) throw new EntityNotFoundException();
			authorList = new LinkedList<>();
			for (String pid : entity.getPaperList()) {
				Paper paper = mongoTemplate.findById(pid, Paper.class);
				for (String name : Paper.getAllAuthors(paper)) {
					Author author = mongoTemplate.findOne(Query.query(new Criteria(auName).is(name)),
							Author.class);
					if (author != null)
						authorList.add(author);
				}
			}
		} else throw new BadReqException();

		JSONArray array = new JSONArray();

		for (Author author : authorList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(author.getId());
			array.add(generalJsonVO.author2VO(author, baseEntity));
		}
		return pageHelper.sortAndPage(array, pageSize, pageNum);
	}

	@Override
	public JSONArray fetchAuthorSummaryUponField() {
		JSONArray ans = new JSONArray();
		Set<String> fieldWholeSet = new HashSet<>();
		for (Author author : authorRepository.findAll()) {
			fieldWholeSet.addAll(Author.getAllFields(author));
		}
		Map<String, Long> hash = new HashMap<>();
		for (String fieldName : fieldWholeSet) {
			long count = mongoTemplate.count(Query.query(new Criteria("field").regex(fieldName)),
					Author.class);
			hash.put(fieldName, count);
		}
		for (String key : hash.keySet()) {
			JSONObject obj = new JSONObject();
			obj.put("fieldName", key);
			obj.put("count", hash.get(key));
			ans.add(obj);
		}
		return ans;
	}

	@Override
	public JSONArray fetchAuthorWithRefine(List<String> fieldNames, int pageNum, int pageSize) {
		List<Author> authors = authorRepository.findAll();
		Set<String> fieldSet = new HashSet<>(fieldNames);
		authors = authors.stream()
				.filter((Author author) -> {
					for (String name : Author.getAllFields(author)) {
						if (!fieldSet.contains(name)) return false;
					}
					return true;
				}).collect(Collectors.toList());
		JSONArray ans = new JSONArray();
		if (authors.isEmpty()) return ans;
		for (Author author : authors) {
			CounterBaseEntity en = counterService.getSummaryInfo(author.getId());
			ans.add(generalJsonVO.author2VO(author, en));
		}
		return pageHelper.sortAndPage(ans, pageSize, pageNum);
	}


}
