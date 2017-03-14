package com.sxy.graduwork.searchconfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.sxy.graduwork.context.SystemParameters;

public class DatabaseResourceConfig {
	private List<Map<String, String>> dbrList;
	private Map<String, Map<String, String>> dbrMap;
	private Gson gson = new Gson();
	private static Log logger = LogFactory.getLog(DatabaseResourceConfig.class);

	public List<Map<String, String>> getDbrList() {
		return dbrList;
	}

	public Map<String, Map<String, String>> getDbrMap() {
		return dbrMap;
	}

	public DatabaseResourceConfig() {

	}
	
	/**
	 * Initial an object based on db.json
	 */
	public void init() {
		if (dbrMap != null) {
			dbrMap.clear();
		} else {
			dbrMap = new HashMap<String, Map<String, String>>();
		}
		// 读取db.json
		try {
			// 从默认的位置加载db.json
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(SystemParameters.DEFAULT_DB_CONFIG_PATH);
			Reader reader = new InputStreamReader(inputStream);
			dbrList = gson.fromJson(reader, List.class);
			logger.info("Loaded db.json: " + dbrList);
			inputStream.close();
			reader.close();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		// 将配置存入map里，便于读取
		for (Map<String, String> map : dbrList) {
			String dbrShortName = map.get("shortName");
			dbrMap.put(dbrShortName, map);
		}
	}
}
