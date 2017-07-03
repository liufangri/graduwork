package com.sxy.graduwork.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.sxy.graduwork.po.Article;
import com.sxy.graduwork.po.SearchConfig;
import com.sxy.graduwork.searchconfig.BasicSearchConfig;
import com.sxy.graduwork.searchconfig.DatabaseResourceConfig;
import com.sxy.graduwork.tools.PropertiesTool;

public abstract class AbstractDatabaseService {

	private DatabaseResourceConfig databaseResourceConfig;
	private SearchConfigService searchConfigService;

	private static Log logger = LogFactory.getLog(AbstractDatabaseService.class);

	public void setSearchConfigService(SearchConfigService searchConfigService) {
		this.searchConfigService = searchConfigService;
	}

	public DatabaseResourceConfig getDatabaseResourceConfig() {
		return databaseResourceConfig;
	}

	public void setDatabaseResourceConfig(DatabaseResourceConfig databaseResourceConfig) {
		this.databaseResourceConfig = databaseResourceConfig;
	}

	private static Gson gson = new Gson();

	/**
	 * Prepare before accessing database.
	 */
	public void prepare() {
		// TODO: 一般的工作
	}

	/**
	 * Get searchResult
	 * 
	 * @param paramsMap
	 * @return
	 */
	public String getSearchResultForFront(Map<String, String> paramsMap) {
		SearchConfig config = searchConfigService.createSearchConfig(paramsMap);
		BasicSearchConfig searchConfig = gson.fromJson(config.getConfigJson(), BasicSearchConfig.class);
		Map<String, Article> articleMap = getSearchResultArticleMap(searchConfig);
		String result = gson.toJson(articleMap);
		return result;
	}

	/**
	 * Get Endnote file from a database site.
	 * 
	 * @param searchConfig
	 *            TODO
	 * 
	 * @return file
	 */
	public abstract Map<String, Article> getSearchResultArticleMap(BasicSearchConfig searchConfig);


	/**
	 * Import .enw file into endnote
	 */
	public void exportToEndnote(Map<String, Article> articleMap) {
		// 导入enw文件到Endnote, Endnote处于已经运行的状态，如果没有运行，会直接打开
		final Runtime runtime = Runtime.getRuntime();
		PropertiesTool pt = new PropertiesTool();
		final String endnotePath = pt.getValue("endnote_location");
		String classpath = pt.setPath("config/configuration").getValue("classpath");
		final File folder = new File(classpath);
		final String lock = "lock";
		new Thread() {
			@Override
			public void run() {
				synchronized (lock) {
					try {
						// Try to run endnote
						logger.info("Trying to start EndNote.");
						runtime.exec(new String[] { endnotePath + "EndNote.exe" }, null, folder);
						Thread.sleep(15000);
						logger.info("EndNote executed.");
						lock.notifyAll();

					} catch (IOException e1) {
						e1.printStackTrace();
						return;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}.start();

		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Entry<String, Article> entry : articleMap.entrySet()) {
			Article article = entry.getValue();
			String path = article.getEnwLocation();
			logger.info("Exporting file: " + path);
			try {
				Process process = runtime.exec(new String[] { endnotePath + "EndNote.exe", classpath + path }, null, folder);
				process.waitFor();
			} catch (IOException e) {
				logger.error("Export failed.");
				e.printStackTrace();
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
