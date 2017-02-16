package com.sxy.graduwork.service;

import java.io.File;

public abstract class DatabaseService {
	/**
	 * Prepare before accessing database.
	 */
	public void prepare() {
		// TODO: һ��Ĺ���
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
		// TODO: ����endnotes�ļ���Endnote, Endnote�����Ѿ����е�״̬�����û�����У��������´εļƻ�����ʱ��

	}
}
