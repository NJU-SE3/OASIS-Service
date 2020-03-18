package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InitializationServiceImpl implements InitializationService {
	private static final Logger logger = LoggerFactory.getLogger(InitializationService.class);
	private static final char BLANK = ' ';
	private static final char FIELD_SPLITTER = ',';
	@Autowired
	private MongoTemplate mongoTemplate;

	//机构初始化
	@Override
	@Async
	public void initAffiliationBase() {
		final String checkColumn = "affiliationName";
		Set<String> affiliationNameFullSet = mongoTemplate.findAll(Author.class)
				.stream()
				.map(Author::getAffiliationName)
				.filter((String name) -> !name.isEmpty())
				.collect(Collectors.toSet());
		for (String affName : affiliationNameFullSet) {
			//不添加重复项
			if (mongoTemplate.exists(
					Query.query(Criteria.where(checkColumn).is(affName)), Affiliation.class)) {
				continue;
			}
			Affiliation entity = new Affiliation();
			entity.setAffiliationName(affName);
			//存储
			entity = mongoTemplate.save(entity);
		}
	}

	//会议初始化
	//领域信息添加
	@Async
	@Override
	public void initConferenceBasic() {
		final String checkColumn = "conferenceName";
		Set<String> conferenceRawSet = mongoTemplate.findAll(Paper.class)
				.stream()
				.map(Paper::getConference)
				.collect(Collectors.toSet());
		Set<String> conSet = new HashSet<>();
		final String rankerPattern = "[0-9]+[a-z]+$";
		for (String conferenceName : conferenceRawSet) {
			Conference conference = new Conference();
			if (mongoTemplate.exists(Query.query(Criteria.where(checkColumn).is(conferenceName)),
					Conference.class)) {
				continue;
			}
			//2015 IEEE/ACM 37th IEEE International Conference on Software Engineering
//			String[] words = conferenceName.split(String.valueOf(BLANK));
//			StringBuilder sb = new StringBuilder();
//			int year = -1;
//			String ranker = "";
//			for (String word : words) {
//				if (isNumeric(word)) {
//					try {
//						year = Integer.parseInt(word.substring(0, 4));
//					} catch (Exception ignored) {
//					}
//				} else if (word.matches(rankerPattern)) {
//					ranker = word;
//				} else sb.append(BLANK).append(word);
//			}
//			conference.setConferenceName(sb.toString().trim());
			conference.setConferenceName(conferenceName);
//			conference.setRanker(ranker);
//			conference.setYear(year);
			mongoTemplate.save(conference);
		}

	}

	@Async
	@Override
	public void initFieldBasic() {
		final String checkColumn = "fieldName";
		List<Author> authorList = mongoTemplate.findAll(Author.class);
		Set<String> filedSet = new HashSet<>();
		for (Author author : authorList) {
			String[] fields = author.getField().split(String.valueOf(FIELD_SPLITTER));
			filedSet.addAll(Arrays.asList(fields));
		}
		filedSet.stream()
				.filter((String name) -> !name.isEmpty())
				.forEach((String name) -> {
					if (!mongoTemplate.exists(Query.query(Criteria.where(checkColumn).is(name)), Field.class)) {
						Field field = new Field();
						field.setFieldName(name);
						mongoTemplate.save(field);
					}
				});
	}

	//基本计数信息
	@Override
	public void initCounterPOJO() {
//		final String authorCol = "authors";
//		final String conferenceCol = "conference";
//		final String termCol = "terms";
//		final String affiliationCol = "affiliations";
//		//作者
//		for (Author entity : mongoTemplate.findAll(Author.class)) {
//			List<Paper> papers = mongoTemplate.find(
//					Query.query(Criteria.where(authorCol).is(entity.getAuthorName())),
//					Paper.class);
//			BigInteger id = entity.getId();
//			computeRound(id, papers);
//		}
//
//		//机构
//		for (Affiliation entity : mongoTemplate.findAll(Affiliation.class)) {
//			List<Paper> papers = mongoTemplate.find(
//					Query.query(Criteria.where(affiliationCol).is(entity.getAffiliationName())),
//					Paper.class);
//			BigInteger id = entity.getId();
//			computeRound(id, papers);
//		}
//		//会议
//		for (Conference entity : mongoTemplate.findAll(Conference.class)) {
//			List<Paper> papers = mongoTemplate.find(
//					Query.query(Criteria.where(conferenceCol).is(entity.getConferenceName())),
//					Paper.class);
//			BigInteger id = entity.getId();
//			computeRound(id, papers);
//		}
//		//领域
//		for (Field entity : mongoTemplate.findAll(Field.class)) {
//			List<Paper> papers = mongoTemplate.find(
//					Query.query(Criteria.where(termCol).is(entity.getFieldName())),
//					Paper.class);
//			BigInteger id = entity.getId();
//			computeRound(id, papers);
//		}
	}

//	private void computeRound(BigInteger id, List<Paper> papers) {
//		papers.sort(Comparator.comparingInt(Paper::getYear));
//		CounterBaseEntity totalPOJO = new CounterBaseEntity();
//		totalPOJO.setCheckId(id);
//		totalPOJO.setYear(-1);        // year < 0 denotes the total
//		totalPOJO.setActiveness(computeUtil.getActiveness(papers));
//		totalPOJO.setPaperList(papers.stream().map(Paper::getId).collect(Collectors.toList()));
//		totalPOJO.setPaperCount(computeUtil.getPaperCount(papers));
//		totalPOJO.setCitationCount(computeUtil.getCitationCount(papers));
//		totalPOJO.setH_index(computeUtil.getH_index(papers));
//		totalPOJO.setHeat(computeUtil.getHeat(papers));
//		//save
//		mongoTemplate.save(totalPOJO);
//
//		//根属pojo , 主要计算h-index
//		Set<Integer> yearList = papers.stream()
//				.map(Paper::getYear)
//				.collect(Collectors.toSet());
//		//for every year
//		for (Integer y : yearList) {
//			CounterBaseEntity pojo = new CounterBaseEntity();
//			List<Paper> curList = papers.stream()
//					.filter((Paper p) -> p.getYear() == y)
//					.collect(Collectors.toList());
//			pojo.setCheckId(id);
//			pojo.setYear(y);
//			pojo.setPaperList(curList.stream().map(Paper::getId).collect(Collectors.toList()));
//			pojo.setPaperCount(computeUtil.getPaperCount(curList));
//			pojo.setCitationCount(computeUtil.getCitationCount(curList));
//			pojo.setH_index(computeUtil.getH_index(curList));
//			pojo.setHeat(computeUtil.getHeat(curList));
//			//save
//			mongoTemplate.save(pojo);
//		}
//	}

	private static boolean isNumeric(String str) {
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
		} catch (Exception e) {
			return false;//异常 说明包含非数字。
		}
		return true;
	}
}
