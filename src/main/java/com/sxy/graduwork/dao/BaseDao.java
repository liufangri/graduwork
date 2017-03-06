package com.sxy.graduwork.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BaseDao {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getCurrentSession() {
		Session session = null;
		session = sessionFactory.getCurrentSession();
		return session;
	}
}
