package com.sxy.graduwork.service;

import java.util.Map;

public class IndexService {
	public String getContent(Map<String, String> paramsMap) {
		String url = paramsMap.get("url");
		return url;
	}
}
