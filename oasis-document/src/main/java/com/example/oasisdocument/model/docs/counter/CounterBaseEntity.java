package com.example.oasisdocument.model.docs.counter;

import com.example.oasisdocument.model.docs.BaseEntity;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Index;
import java.util.List;

@Document(collection = "counterBases")
public class CounterBaseEntity extends BaseEntity {
	protected String checkId;
	protected int paperCount;
	@Indexed(direction = IndexDirection.DESCENDING)
	protected int citationCount;
	@Indexed(direction = IndexDirection.DESCENDING)
	protected double activeness;
	@Indexed(direction = IndexDirection.DESCENDING)
	protected int H_index;
	@Indexed(direction = IndexDirection.DESCENDING)
	protected double heat;
	protected int year;
	protected int authorCount;

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

	public double getActiveness() {
		return activeness;
	}

	public void setActiveness(double activeness) {
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

	public double getHeat() {
		return heat;
	}

	public void setHeat(double heat) {
		this.heat = heat;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public int getAuthorCount() {
		return authorCount;
	}

	public void setAuthorCount(int authorCount) {
		this.authorCount = authorCount;
	}
}
