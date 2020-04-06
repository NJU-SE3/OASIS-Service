package com.example.oasisdocument.model.docs.analysis;

import com.example.oasisdocument.model.docs.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("graphEdges")
public class GraphEdge extends BaseEntity {
	private String begin;
	private String end;
	private double weight;

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}
