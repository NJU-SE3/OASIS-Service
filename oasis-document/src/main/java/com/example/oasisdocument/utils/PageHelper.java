package com.example.oasisdocument.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class PageHelper {
    private static class ActivenessComparator implements Comparator {
        private final static String activeKey = "activeness";

        @Override
        public int compare(Object o1, Object o2) {
            JSONObject obj1 = (JSONObject) o1;
            JSONObject obj2 = (JSONObject) o2;
            return (int) (obj2.getDouble(activeKey) - obj1.getDouble(activeKey));
        }
    }

    public <T> List<T> of(List<T> list, Integer pageSize, Integer pageNum) {
        if (pageNum == -1) return list;
        if (0 == pageNum) ++pageNum;
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        Integer count = list.size(); //记录总数
        Integer pageCount = 0; //页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; //开始索引
        int toIndex = 0; //结束索引

        if (pageNum > pageCount) {
            pageNum = pageCount;
        }
        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }

        List pageList = list.subList(fromIndex, toIndex);

        return pageList;
    }

    public JSONArray sortAndPage(JSONArray array, int pageSize, int pageNum) {
        array.sort(new ActivenessComparator());
        List<Object> arr = of(array, pageSize, pageNum);
        JSONArray ans = new JSONArray();
        ans.addAll(arr);
        return ans;
    }
}
