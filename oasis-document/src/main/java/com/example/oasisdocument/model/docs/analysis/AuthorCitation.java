package com.example.oasisdocument.model.docs.analysis;

import com.example.oasisdocument.model.docs.BaseEntity;
import com.example.oasisdocument.model.docs.Paper;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//作者 - citation
@Document("authorCitation")
public class AuthorCitation extends BaseEntity {
	private String authorName;
	private int citationCount;
	private List<Paper> papers;

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public int getCitationCount() {
		return citationCount;
	}

	public void setCitationCount(int citationCount) {
		this.citationCount = citationCount;
	}

	public List<Paper> getPapers() {
		return papers;
	}

	public void setPapers(List<Paper> papers) {
		this.papers = papers;
	}
}
