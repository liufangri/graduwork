package com.sxy.graduwork.service;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sxy.graduwork.context.SystemParameters;
import com.sxy.graduwork.searchconfig.ACMSearchConfig;

public class ACMService extends DatabaseService {

	private String CFID;
	private String CFTOKEN;
	private static Gson gson = new Gson();
	private ACMSearchConfig acmSearchConfig;

	@Override
	public void prepare() {
		super.prepare();
		// ªÒ»°ACMµƒ≈‰÷√
		List<Map<String, String>> DBList;
		try {
			DBList = gson.fromJson(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(SystemParameters.DB_CONFIG_PATH)), List.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public File getEndnoteFile() {

		return null;
	}

	@Override
	public String getFullTextURL() {

		return null;
	}

}
