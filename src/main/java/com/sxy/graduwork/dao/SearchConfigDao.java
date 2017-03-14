package com.sxy.graduwork.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.sxy.graduwork.po.SearchConfig;

public class SearchConfigDao extends BaseDao {

	public void addSearchConfig(SearchConfig searchConfig) {
		Session session = getCurrentSession();
		session.save(searchConfig);
		
	}

	public List<SearchConfig> getSearchConfigList() {
		Session session = getCurrentSession();
		@SuppressWarnings("unchecked")
		List<SearchConfig> result = (List<SearchConfig>) (session.createQuery("SELECT s FROM SearchConfig s WHERE s.deleteMark != '1'").list());
		return result;
	}

	public void deleteEntityByIdLogic(String id) {
		Session session = getCurrentSession();
		Query query = session.createQuery("UPDATE SearchConfig s SET s.deleteMark = '1' WHERE s.id = ?");
		query.setParameter(0, id);
		query.executeUpdate();
	}

}
