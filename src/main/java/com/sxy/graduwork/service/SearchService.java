package com.sxy.graduwork.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.sxy.graduwork.context.DatabaseServiceManager;
import com.sxy.graduwork.po.Article;
import com.sxy.graduwork.po.SearchConfig;
import com.sxy.graduwork.searchconfig.BasicSearchConfig;
import com.sxy.graduwork.searchconfig.DatabaseResourceConfig;

public class SearchService {

	private DatabaseServiceManager databaseServiceManager;
	private DatabaseResourceConfig databaseResourceConfig;
	private SearchConfigService searchConfigService;

	private static Log logger = LogFactory.getLog(SearchService.class);
	private static Gson gson = new Gson();

	public void setSearchConfigService(SearchConfigService searchConfigService) {
		this.searchConfigService = searchConfigService;
	}

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public void setDatabaseResourceConfig(DatabaseResourceConfig databaseResourceConfig) {
		this.databaseResourceConfig = databaseResourceConfig;
	}

	public String search(Map<String, String> paramsMap) {
		String databaseStr = paramsMap.get("databases");
		SearchConfig config = searchConfigService.createSearchConfig(paramsMap);
		String json = config.getConfigJson();
		BasicSearchConfig searchConfig = gson.fromJson(json, BasicSearchConfig.class);
		String[] databases = databaseStr.split(",");
		Map<String, Article> resultMap = new HashMap<>();

		for (String databaseShortName : databases) {
			String serviceId = databaseResourceConfig.getDbrMap().get(databaseShortName).get("serviceID");
			AbstractDatabaseService service = databaseServiceManager.getServiceByID(serviceId);
			service.prepare();
			Map<String, Article> articleMap = service.getSearchResultArticleMap(searchConfig);
			resultMap.putAll(articleMap);
			service.exportToEndnote(articleMap);
		}

		return gson.toJson(resultMap);

	}
}
