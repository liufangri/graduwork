package com.sxy.graduwork.context;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.sxy.graduwork.service.AbstractDatabaseService;

public class DatabaseServiceManager implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	private Map<String, AbstractDatabaseService> dbServiceMap;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void init() {
		ListableBeanFactory lbf = ((ScriptAccessXmlApplicationContext) applicationContext).getBeanFactory();
		dbServiceMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(lbf, AbstractDatabaseService.class, true, true);
	}

	public AbstractDatabaseService getServiceByID(String name) {
		return dbServiceMap.get("name");
	}
}
