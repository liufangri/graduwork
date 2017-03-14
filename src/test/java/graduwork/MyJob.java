package graduwork;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sxy.graduwork.po.Article;

public class MyJob implements Job {

	private Article article;

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public MyJob() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(article.toString());

	}

}