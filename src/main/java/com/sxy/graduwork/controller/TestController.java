package com.sxy.graduwork.controller;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
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
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			return session;

		} catch (HibernateException e) {
			session = sessionFactory.openSession();
			return session;
		}
	}

	@SuppressWarnings("unchecked")
	public String work() {

		Session session = currentSession();
		Query query = session.createQuery("select e from Test e where e.id = '1'");
		List<Test> tests = (List<Test>) query.list();
		Test test = tests.get(0);
		return JSONTool.parseJSON(test);
	}

	@SuppressWarnings("unchecked")
	public String test() {

		Session session = currentSession();
		Query query = session.createQuery("select e from Test e where e.id = '1'");
		List<Test> tests = (List<Test>) query.list();
		Test test = tests.get(0);
		return JSONTool.parseJSON(test);
	}
}
