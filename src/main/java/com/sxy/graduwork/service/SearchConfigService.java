package com.sxy.graduwork.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.sxy.graduwork.dao.SearchConfigDao;
import com.sxy.graduwork.po.SearchConfig;

public class SearchConfigService {

	private SearchConfigDao searchConfigDao;

	public static final Log logger = LogFactory.getLog(SearchConfigService.class);

	public void setSearchConfigDao(SearchConfigDao searchConfigDao) {
		this.searchConfigDao = searchConfigDao;
	}

	public String addNewSearchConfig(Map<String, String> paramsMap) {

		String configJson = paramsMap.get("jsonStr");
		String scheduPattern = paramsMap.get("schedu");
		String databases = paramsMap.get("databases");
		String id = UUID.randomUUID().toString();
		Timestamp createTime = new Timestamp(new Date().getTime());

		SearchConfig searchConfig = new SearchConfig();

		searchConfig.setCheckedDatabase(databases);
		searchConfig.setConfigJson(configJson);
		searchConfig.setCreateTime(createTime);
		searchConfig.setId(id);
		searchConfig.setSchedulePattern(scheduPattern);

		searchConfigDao.addSearchConfig(searchConfig);

		System.out.println(configJson);
		return "{\"result\":\"SUCCESS\"}";
	}

	public String getConfigList() {
		@SuppressWarnings("rawtypes")
		List list = searchConfigDao.getSearchConfigList();
		return new Gson().toJson(list);
	}
}
