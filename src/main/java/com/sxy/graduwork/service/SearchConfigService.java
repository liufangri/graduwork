package com.sxy.graduwork.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.gson.Gson;
import com.sxy.graduwork.dao.SearchConfigDao;
import com.sxy.graduwork.po.SearchConfig;
import com.sxy.graduwork.schedu.ScheduJobManager;

public class SearchConfigService implements ApplicationContextAware {

	private SearchConfigDao searchConfigDao;
	private ApplicationContext applicationContext;


	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public static final Log logger = LogFactory.getLog(SearchConfigService.class);



	public void setSearchConfigDao(SearchConfigDao searchConfigDao) {
		this.searchConfigDao = searchConfigDao;
	}

	/**
	 * �����������ò����õ���ʱ������
	 * 
	 * @param paramsMap
	 * @return
	 */
	public String addNewSearchConfig(Map<String, String> paramsMap) {

		SearchConfig searchConfig = createSearchConfig(paramsMap);
		searchConfigDao.addSearchConfig(searchConfig);
		ScheduJobManager scheduJobManager = (ScheduJobManager) applicationContext.getBean("scheduJobManager");
		scheduJobManager.createScheduJob(searchConfig, "SearchJobGroup");
		return "{\"result\":\"SUCCESS\"}";
	}

	/**
	 * ���ݲ�������������
	 * 
	 * @param paramsMap
	 * @return
	 */
	public SearchConfig createSearchConfig(Map<String, String> paramsMap) {
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
		searchConfig.setDeleteMark("0");
		return searchConfig;
	}

	public String getConfigListString() {
		@SuppressWarnings("rawtypes")
		List list = searchConfigDao.getSearchConfigList();
		return new Gson().toJson(list);
	}

	public List<SearchConfig> getConfigList() {
		List<SearchConfig> list = searchConfigDao.getSearchConfigList();
		return list;
	}

	/**
	 * �߼�ɾ���������ã�����ϵͳ��ȥ���ö�ʱ����
	 * 
	 * @param paramsMap
	 * @return
	 */
	public String deleteConfigLogic(Map<String, String> paramsMap) {
		String id = paramsMap.get("id");
		searchConfigDao.deleteEntityByIdLogic(id);
		ScheduJobManager scheduJobManager = (ScheduJobManager) applicationContext.getBean("scheduJobManager");
		scheduJobManager.abandonRunningJob("Job_" + id, "SearchJobGroup");
		scheduJobManager.deleteScheduedJob("Job_" + id);
		return "{\"result\":\"SUCCESS\"}";
	}

}
