package com.example.oasisdocument.model.docs.extendDoc;

import com.example.oasisdocument.model.docs.BaseEntity;
import com.example.oasisdocument.model.docs.Paper;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "conferences")
public class Conference extends BaseEntity {
	private String conferenceName;                //会议名称
	private int year;                            //年份
	private String ranker;        //届
	@DBRef(lazy = true)
	private List<Paper> paperList;
	public Conference() {
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getRanker() {
		return ranker;
	}

	public void setRanker(String ranker) {
		this.ranker = ranker;
	}

	@Override
	public String toString() {
		return "Name = " + conferenceName +
				",year = " + year + ",ranker = " + ranker;
	}

	public List<Paper> getPaperList() {
		return paperList;
	}

	public void setPaperList(List<Paper> paperList) {
		this.paperList = paperList;
	}
}
