package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSON;
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
import com.example.oasisdocument.service.IntermeService;
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.oasisdocument.service.IntermeService.affCounterCollection;
import static com.example.oasisdocument.service.IntermeService.authorCounterCollection;

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
	private IntermeService intermeService;
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
	public JSONObject fetchAuthorList(int pageNum, int pageSize, String rankKey) {
//一级缓存命中
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		JSONArray data = new JSONArray();
		long itemCnt = mongoTemplate.count(new Query(), Author.class);
		Sort sort = Sort.by(Sort.Direction.DESC, rankKey);
		if (intermeService.intermeExist(authorCounterCollection)) {
			List<CounterBaseEntity> bufferList = mongoTemplate.find(new Query()
							.with(sort)
							.with(pageable),
					CounterBaseEntity.class, authorCounterCollection);
			for (CounterBaseEntity entity : bufferList) {
				data.add(authorBuffer2VO(entity));
			}
		} else {
			//缓存未命中 , 随机返回
			//异步缓存
			intermeService.genAuthorCounter();
			//随机返回pageable条目
			List<Author> entities = mongoTemplate.find(new Query()
					.with(pageable).with(sort), Author.class);
			for (Author en : entities) {
				CounterBaseEntity baseEntity = counterService.getSummaryInfo(en.getId());
				data.add(generalJsonVO.author2VO(en, baseEntity));
			}
		}
		JSONObject ans = new JSONObject();
		ans.put("data", data);
		ans.put("itemCnt", itemCnt);
		return ans;

	}

	private JSONObject authorBuffer2VO(CounterBaseEntity entity) {
		String uid = entity.getCheckId();
		Author en = mongoTemplate.findById(uid, Author.class);
		if (null == en) return new JSONObject();

		return generalJsonVO.author2VO(en, entity);
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
			Set<String> authorIds = new HashSet<>();

			for (String pid : entity.getPaperList()) {
				Paper paper = mongoTemplate.findById(pid, Paper.class);
				for (String name : Paper.getAllAuthors(paper)) {
					Author author = mongoTemplate.findOne(Query.query(new Criteria(auName).is(name)),
							Author.class);
					if (author != null && !authorIds.contains(author.getId())) {
						authorIds.add(author.getId());
						authorList.add(author);
					}
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
	@Cacheable(cacheNames = "authorFieldSummary", unless = "#result==null")
	public JSONArray fetchAuthorSummaryUponField() {
		final int pageSize = 10;
		JSONArray ans = new JSONArray();

		Map<String, Long> hash = new HashMap<>();
		for (Author author : authorRepository.findAll()) {
			for (String fieldName : Author.getAllFields(author)) {
				hash.put(fieldName, hash.getOrDefault(fieldName, 0L) + 1);
			}
		}

		for (String key : hash.keySet()) {
			JSONObject obj = new JSONObject();
			obj.put("fieldName", key);
			obj.put("count", hash.get(key));
			ans.add(obj);
		}
		ans.sort((o1, o2) -> {
			JSONObject ob1 = JSONObject.parseObject(JSON.toJSONString(o1)),
					ob2 = JSONObject.parseObject(JSON.toJSONString(o2));
			return (int) (ob2.getDouble("count") - ob1.getDouble("count"));
		});
		List<Object> li = pageHelper.of(ans, pageSize, 0);
		return JSONArray.parseArray(JSON.toJSONString(li));
	}

	@Override
	@Cacheable(cacheNames = "authorFieldSummary", unless = "#result==null")
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
