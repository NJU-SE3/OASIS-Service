package com.example.oasisdocument.utils;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ComputeUtil {
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 活跃度计算 , 总数计算
	 * paper数的和（以3年为周期，每向前3年paper数除以Math.ceiling（今年-当年年份/3））
	 */
	public double getActiveness(List<Paper> paperList) {
		assert null != paperList;
		if (paperList.isEmpty()) return 0.0;
		paperList.sort(Comparator.comparingInt(Paper::getYear));
		int end = paperList.get(paperList.size() - 1).getYear();
		//store the map from gap to paperNum
		Map<Integer, List<Integer>> hash = new HashMap<>();
		for (int idx = paperList.size() - 2; idx >= 0; --idx) {
			Paper p = paperList.get(idx);
			int gap = (int) Math.ceil((double) (end - p.getYear()) / 3);
			List<Integer> years = hash.getOrDefault(gap, new LinkedList<>());
			years.add(p.getYear());
			hash.put(gap, years);
		}
		double heat = 0.0;
		for (int gap : hash.keySet()) {
			double incr = 0.0;
			for (int year : hash.get(gap)) {
				incr += (int) Math.ceil((double) (end - year) / 3);
			}
			heat += incr;
		}
		return heat;
	}

	/**
	 * 活跃度计算 , 以某一年为结束时间
	 */
	public double getActiveness(int endYear, String id) {
		CounterBaseEntity en = mongoTemplate.findOne(Query.query(new Criteria("checkId").is(id)
				.and("year").is(-1)), CounterBaseEntity.class);
		if (null == en)
			throw new EntityNotFoundException();
		List<Paper> papers = new LinkedList<>();
		en.getPaperList().forEach((String pid) -> {
			Paper paper = mongoTemplate.findById(pid, Paper.class);
			if (null != paper && endYear >= paper.getYear()) papers.add(paper);
		});
		return getActiveness(papers);
	}


	/**
	 * H_index计算
	 */
	public int getH_index(List<Paper> paperList) {
		assert null != paperList;
		List<Integer> citations = paperList.stream()
				.map(Paper::getCitationCount).sorted(Integer::compareTo).collect(Collectors.toList());
		int level = 0;
		for (int i = 0; i < citations.size(); i++)
			level = Math.max(level, Math.min(citations.size() - i, citations.get(i)));
		return level;
	}

	/**
	 * 热度计算
	 * 总被引用数/发表论文总数
	 */
	public double getHeat(List<Paper> paperList) {
		assert null != paperList;
		if (paperList.isEmpty()) return 0.0;
		return getCitationCount(paperList) / (double) getPaperCount(paperList);
	}

	/**
	 * citation计数
	 */
	public int getCitationCount(List<Paper> paperList) {
		assert null != paperList;
		int ans = 0;
		for (Paper p : paperList) ans += p.getCitationCount();
		return ans;
	}

	/**
	 * paper计数
	 */
	public int getPaperCount(List<Paper> paperList) {
		assert null != paperList;
		return paperList.size();
	}

	/**
	 * 作者数目添加
	 */
	public int getAuthorCnt(List<Paper> paperList) {
		assert null != paperList;
		int ans = 0;
		for (Paper paper : paperList) {
			ans += Paper.getAllAuthors(paper).size();
		}
		return ans;
	}
}
