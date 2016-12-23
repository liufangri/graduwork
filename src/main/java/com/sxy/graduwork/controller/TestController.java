package com.sxy.graduwork.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.sxy.graduwork.po.Test;
import com.sxy.graduwork.tools.JSONTool;

public class TestController {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	public String work() {
		Test test = (Test) currentSession().byId(Test.class);
		return JSONTool.parseJSON(test);
	}
}
