package com.sxy.graduwork.test;

import java.util.List;

import com.google.gson.Gson;
import com.sxy.graduwork.context.ScriptAccessXmlApplicationContext;
import com.sxy.graduwork.po.SearchConfig;
import com.sxy.graduwork.searchconfig.BasicSearchConfig;
import com.sxy.graduwork.service.ACMService;
import com.sxy.graduwork.service.SearchConfigService;
import com.sxy.graduwork.tools.PropertiesTool;

public class TestAcmService {
	

	public void testApp(){
		PropertiesTool pt = new PropertiesTool("config/configuration");
		String classpath = pt.getClass().getResource("/").getPath().substring(1);
		pt.addProperty("classpath", classpath);
		// insert db path
		pt.addProperty("dbcp.url", "jdbc:h2:" + pt.getValue("classpath") + "db/application");
		pt.store();
		pt.setPath("log4j.properties").addProperty("log4j.appender.R.File", classpath + "log/testlog.log").store();
		ScriptAccessXmlApplicationContext context = new ScriptAccessXmlApplicationContext("classpath:spring/*");
		ACMService acmService = context.getBean(ACMService.class);
		SearchConfigService searchConfigService = context.getBean(SearchConfigService.class);
		
		List<SearchConfig> configs = searchConfigService.getConfigList();
		for (SearchConfig config : configs) {
			BasicSearchConfig searchConfig = new Gson().fromJson(config.getConfigJson(), BasicSearchConfig.class);
			acmService.prepare();
			acmService.getSearchResultArticleMap(searchConfig);
		}

		
	}

	public static void main(String[] args) {
		TestAcmService testClass = new TestAcmService();
		testClass.testApp();
	}
}
