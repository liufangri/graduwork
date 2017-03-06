package com.sxy.graduwork.dao;

import java.util.List;

import org.hibernate.Session;

import com.sxy.graduwork.po.SearchConfig;

public class SearchConfigDao extends BaseDao {

	public void addSearchConfig(SearchConfig searchConfig) {
		Session session = getCurrentSession();
		session.save(searchConfig);
		
	}

	public List<SearchConfig> getSearchConfigList() {
		Session session = getCurrentSession();
		List<SearchConfig> result = (List<SearchConfig>) (session.createQuery("select s from SearchConfig s").list());
		return result;
	}

}
