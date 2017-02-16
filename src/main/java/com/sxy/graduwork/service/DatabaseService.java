package com.sxy.graduwork.service;

import java.io.File;

public abstract class DatabaseService {
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
	 * Get a full-text download url from a database site(If there is).
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
