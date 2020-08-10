package com.example.oasisdocument.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.options.AttentionDTO;
import com.example.oasisdocument.service.AttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attention")
public class AttentionController {
    @Autowired
    private AttentionService attentionService;


    @GetMapping("/batchQuery")
    public JSONArray batchQueryAttention(@RequestParam(name = "authorId") String authorId,
                                         @RequestParam(name = "minYear") int minYear,
                                         @RequestParam(name = "maxYear") int maxYear) {
        List<AttentionDTO> attentionDTOS = attentionService.batchQueryMaxAttention(authorId, minYear, maxYear);
        JSONArray ans = new JSONArray();
        for (AttentionDTO attentionDTO : attentionDTOS) {
            JSONObject obj = new JSONObject();
            obj.put("year", attentionDTO.getYear());
            obj.put("score", attentionDTO.getScore());
            obj.put("fieldName", attentionDTO.getFieldName());
            ans.add(obj);
        }
        return ans;
    }

    @GetMapping("/trend")
    public JSONArray attentionTrend(@RequestParam(name = "authorId") String authorId,
                                    @RequestParam(name = "fieldName") String fieldName) {
        List<AttentionDTO> attentionDTOS = attentionService.queryAttentionTrend(authorId, fieldName);
        JSONArray ans = new JSONArray();
        for (AttentionDTO attentionDTO : attentionDTOS) {
            JSONObject obj = new JSONObject();
            obj.put("year", attentionDTO.getYear());
            obj.put("score", attentionDTO.getScore());
            obj.put("fieldName", fieldName);
            ans.add(obj);
        }
        return ans;
    }
}
