package com.sxy.graduwork.service;

import java.io.File;

import com.google.gson.Gson;
import com.sxy.graduwork.searchconfig.DatabaseResourceConfig;

public abstract class AbstractDatabaseService {

	private DatabaseResourceConfig dbrConfig;

	public DatabaseResourceConfig getDbrConfig() {
		return dbrConfig;
	}

	public void setDbrConfig(DatabaseResourceConfig dbrConfig) {
		this.dbrConfig = dbrConfig;
	}

	private static Gson gson = new Gson();

	/**
	 * Prepare before accessing database.
	 */
	public void prepare() {
		// TODO: 一般的工作
	}

	/**
	 * Get Endnote file from a database site.
	 * 
	 * @return file
	 */
	public abstract File getEndnoteFile();

	/**
	 * Get a full-text download link from a database site.
	 * 
	 * @return URL string
	 */
	public abstract String getFullTextURL();

	/**
	 * Import .enw file into endnote
	 */
	public void exportToEndnote() {
		// TODO: 导入endnotes文件到Endnote, Endnote处于已经运行的状态，如果没有运行，延续到下次的计划导入时间

	}

}
