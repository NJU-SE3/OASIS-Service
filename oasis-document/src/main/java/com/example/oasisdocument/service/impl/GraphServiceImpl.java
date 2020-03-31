package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.graph.nodes.AuthorNeo;
import com.example.oasisdocument.model.graph.nodes.PaperNeo;
import com.example.oasisdocument.repository.graph.AuthorNeoRepo;
import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class GraphServiceImpl implements GraphService {
	@Autowired
	private AuthorNeoRepo authorNeoRepo;

	@Override
	public JSONObject centeralAuthor(String authorId) {
		AuthorNeo centerEntity = authorNeoRepo.findByID(authorId);
		if (null == centerEntity) throw new EntityNotFoundException();
		// 寻找一度关系作者
		Set<AuthorNeo> neighbors = new HashSet<>();


		for (PaperNeo paper : centerEntity.getPaperNeoSet()) {
			neighbors.addAll(paper.getAuthorNeoSet());
		}


		JSONArray edges = new JSONArray(), nodes = new JSONArray();
		// construct the nodes set
		nodes.add(centerEntity);
		nodes.addAll(neighbors);
		// construct the edges set
		for (AuthorNeo authorNeo : neighbors) {
			JSONObject obj = new JSONObject();
			obj.put("begin", centerEntity.getID());
			obj.put("end", authorNeo.getID());
			edges.add(obj);
		}
		JSONObject ans = new JSONObject();
		ans.put("nodes", nodes);
		ans.put("edges", edges);

		return ans;
	}
}
