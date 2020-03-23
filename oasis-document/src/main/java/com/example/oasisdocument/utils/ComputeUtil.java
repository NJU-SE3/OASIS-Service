package com.example.oasisdocument.utils;

import com.example.oasisdocument.model.docs.Paper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ComputeUtil {
	/**
	 * 活跃度计算
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
}
