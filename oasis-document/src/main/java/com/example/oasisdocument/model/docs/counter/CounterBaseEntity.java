package com.example.oasisdocument.model.docs.counter;

import com.example.oasisdocument.model.docs.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

@Document(collection = "counterBases")
public class CounterBaseEntity extends BaseEntity {
	protected String checkId;
	protected int paperCount;
	protected int citationCount;
	protected int activeness;
	protected int H_index;
	protected int year;
	protected int heat;
	protected List<String> paperList;

	public int getPaperCount() {
		return paperCount;
	}

	public void setPaperCount(int paperCount) {
		this.paperCount = paperCount;
	}

	public int getCitationCount() {
		return citationCount;
	}

	public void setCitationCount(int citationCount) {
		this.citationCount = citationCount;
	}

	public int getActiveness() {
		return activeness;
	}

	public void setActiveness(int activeness) {
		this.activeness = activeness;
	}

	public int getH_index() {
		return H_index;
	}

	public void setH_index(int h_index) {
		H_index = h_index;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<String> getPaperList() {
		return paperList;
	}

	public void setPaperList(List<String> paperList) {
		this.paperList = paperList;
	}

	public int getHeat() {
		return heat;
	}

	public void setHeat(int heat) {
		this.heat = heat;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
}
