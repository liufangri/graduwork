package com.sxy.graduwork.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.sxy.graduwork.po.Article;

public class ArticleDao extends BaseDao {
	@SuppressWarnings("unchecked")
	public boolean isArticleAlreadyExist(String doi) {
		String queryStr = "SELECT COUNT(a) FROM Article a WHERE a.doi = ?";
		Session session = getCurrentSession();
		Query query = session.createQuery(queryStr);
		query.setString(0, doi);
		List<Object> list = (List<Object>) query.list();
		if ((Long) list.get(0) > 0) {
			return true;
		}
		return false;
	}

	public void saveArticle(Article article) {
		Session session = getCurrentSession();
		session.save(article);
	}
}
