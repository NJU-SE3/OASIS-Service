package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.analysis.GraphEdge;
import com.example.oasisdocument.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {
	@Autowired
	private GraphService graphService;
	@Autowired
	private GeneralJsonVO generalJsonVO;

//	@GetMapping("/author")
//	public JSONArray authorMapViaId(@RequestParam(name = "id") String id) {
//		List<GraphEdge> edges = graphService.authorMapViaId(id);
//		JSONArray array = new JSONArray();
//		edges.forEach((GraphEdge edge) -> array.add(generalJsonVO.authorEdge2VO(edge)));
//		return array;
//	}

	@GetMapping("/field")
	public JSONArray fieldMapViaId(@RequestParam(name = "id") String id) {
		List<GraphEdge> edges = graphService.fieldMapViaId(id);
		JSONArray array = new JSONArray();
		edges.forEach((GraphEdge edge) -> {
			JSONObject obj = generalJsonVO.fieldEdge2VO(edge);
			//除去begin节点
			obj.remove("begin");
			array.add(obj);
		});
		return array;
	}

	@GetMapping("/affiliation")
	public JSONArray affMapViaId(@RequestParam(name = "id") String id) {
		List<GraphEdge> edges = graphService.affMapViaId(id);
		JSONArray array = new JSONArray();
		edges.forEach((GraphEdge edge) -> array.add(generalJsonVO.affiliationEdge2VO(edge)));
		return array;
	}
}
