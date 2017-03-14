package com.sxy.graduwork.schedu;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.google.gson.Gson;
import com.sxy.graduwork.context.DatabaseServiceManager;
import com.sxy.graduwork.po.Article;
import com.sxy.graduwork.po.SearchConfig;
import com.sxy.graduwork.searchconfig.BasicSearchConfig;
import com.sxy.graduwork.searchconfig.DatabaseResourceConfig;
import com.sxy.graduwork.service.AbstractDatabaseService;

public class SearchJob implements Job, InterruptableJob {

	private DatabaseServiceManager databaseServiceManager;
	private DatabaseResourceConfig databaseResourceConfig;

	private SearchConfig searchConfig;

	private static Log logger = LogFactory.getLog(SearchJob.class);


	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public void setDatabaseResourceConfig(DatabaseResourceConfig databaseResourceConfig) {
		this.databaseResourceConfig = databaseResourceConfig;
	}

	public void setSearchConfig(SearchConfig searchConfig) {
		this.searchConfig = searchConfig;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Gson gson = new Gson();
		String json = searchConfig.getConfigJson();
		BasicSearchConfig basicSearchConfig = gson.fromJson(json, BasicSearchConfig.class);
		Map<String, AbstractDatabaseService> map = databaseServiceManager.getDbServiceMap();
		for (Entry<String, AbstractDatabaseService> entry : map.entrySet()) {
			AbstractDatabaseService service = entry.getValue();
			logger.info("Executing search job.");
			service.prepare();
			Map<String, Article> articleMap = service.getSearchResultArticleMap(basicSearchConfig);
			service.exportToEndnote(articleMap);
		}

	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
	}

}
