package com.okcl.aiagent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网页搜索工具
 */
@Component
public class WebSearchTool {
    @Value("${searchApi.url}")
    private String url;
    @Value("${searchApi.apiKey}")
    private String apiKey;

    @Tool(description = "Search for information from Baidu Search Engine")
    public String search(@ToolParam(description = "Search query keyword") String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("q", query);
        params.put("api_key", apiKey);
        params.put("engine", "baidu");
        try {
            String res = HttpUtil.get(url, params);
            JSONObject jsonObject = JSONUtil.parseObj(res);

            // 添加空值检查
            JSONArray jsonArray = jsonObject.getJSONArray("organic_results");
            if (jsonArray == null || jsonArray.isEmpty()) {
                return "No search results found";
            }
            // 安全地获取子列表
            int endIndex = Math.min(5, jsonArray.size());
            List<Object> objects = jsonArray.subList(0, endIndex);

            return objects.stream().map(obj -> {
                // 添加类型检查
                if (obj instanceof JSONObject tmp) {
                    return tmp.toString();
                }
                return "{}"; // 返回默认值
            }).collect(Collectors.joining(","));
        } catch (Exception e) {
            return "Error searching Baidu: " + e.getMessage();
        }
    }
}
