package com.example.oasisdocument.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JsonUtil {


	/**
	 * 功能描述：把JSON数据转换成指定的java对象
	 *
	 * @param jsonData JSON数据
	 * @param clazz    指定的java对象
	 * @return 指定的java对象
	 */
	public  <T> T jsonToObj(String jsonData, Class<T> clazz) {
		return JSON.parseObject(jsonData, clazz);
	}

	/**
	 * 功能描述：把java对象转换成JSON数据
	 *
	 * @param object java对象
	 * @return JSON数据
	 */
	public  String objToJsonStr(Object object) {
		return JSON.toJSONString(object);
	}

	/**
	 * 功能描述：把JSON数据转换成指定的java对象列表
	 *
	 * @param jsonData JSON数据
	 * @param clazz    指定的java对象
	 * @return List<T>
	 */
	public  <T> List<T> jsonToList(String jsonData, Class<T> clazz) {
		return JSON.parseArray(jsonData, clazz);
	}

	/**
	 * 功能描述：把JSON数据转换成较为复杂的List<Map<String, Object>>
	 *
	 * @param jsonData JSON数据
	 * @return List<Map < String, Object>>
	 */
	public  List<Map<String, Object>> getJsonToListMap(String jsonData) {
		return JSON.parseObject(jsonData, new TypeReference<List<Map<String, Object>>>() {
		});
	}

}
