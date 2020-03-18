package com.example.oasisdocument.utils;

import com.example.oasisdocument.model.docs.Paper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComputeUtil {
	public int getActiveness(List<Paper> paperList) {
		assert null != paperList;
		return 0;
	}

	public int getH_index(List<Paper> paperList) {
		assert null != paperList;
		return 0;
	}

	public int getHeat(List<Paper> paperList){
		assert null!=paperList;
		return 0;
	}

	public int getCitationCount(List<Paper> paperList) {
		assert null != paperList;
		int ans = 0;
		for (Paper p : paperList) ans += p.getCitationCount();
		return ans;
	}

	public int getPaperCount(List<Paper> paperList) {
		assert null != paperList;
		return paperList.size();
	}
}
