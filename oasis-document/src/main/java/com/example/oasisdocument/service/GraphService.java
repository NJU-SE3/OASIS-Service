package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.analysis.GraphEdge;

import java.util.List;
import java.util.Set;

public interface GraphService {
	List<Set<GraphEdge>> authorMapViaId(String id);

	List<GraphEdge> fieldMapViaId(String id);

	List<GraphEdge> affMapViaId(String id);
}
