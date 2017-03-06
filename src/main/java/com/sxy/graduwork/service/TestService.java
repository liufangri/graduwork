package com.sxy.graduwork.service;

import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.gson.Gson;
import com.sxy.graduwork.po.Test;

public class TestService {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session currentSession() {
		Session session = null;
		session = sessionFactory.getCurrentSession();
		return session;

	}

	@SuppressWarnings("unchecked")
	public String work() {
		Session session = currentSession();
		Query query = session.createQuery("select e from Test e where e.id = '1'");
		List<Test> tests = (List<Test>) query.list();
		Test test = tests.get(0);
		return new Gson().toJson(test);
	}

	@SuppressWarnings("unchecked")
	public String test() {
		Session session = currentSession();
		Query query = session.createQuery("select e from Test e");
		List<Test> tests = (List<Test>) query.list();
		return new Gson().toJson(tests);
	}

	public String addTest() {
		Session session = currentSession();
		Test test = new Test();
		test.setId(UUID.randomUUID().toString());
		test.setName("123");
		session.save(test);
		return "{\"resData\":\"Save OK.\"}";
	}
}
