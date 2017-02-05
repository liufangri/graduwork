package com.sxy.graduwork.searchconfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.gson.Gson;
import com.sxy.graduwork.po.SearchField;

public class BasicSearchConfig {
	/**
	 * Any field
	 */
	private List<SearchField> anyField;

	/**
	 * Title
	 */
	private List<SearchField> title;

	/**
	 * Author
	 */
	private List<SearchField> author;

	/**
	 * Abstract
	 */
	private List<SearchField> digest;

	/**
	 * Publish year range. Left limit(>=).
	 */
	private String dte;
	/**
	 * Publish year range. Right limit(<=).
	 */
	private String bfr;

	public List<SearchField> getAnyField() {
		return anyField;
	}

	public void setAnyField(List<SearchField> anyField) {
		this.anyField = anyField;
	}

	public List<SearchField> getTitle() {
		return title;
	}

	public void setTitle(List<SearchField> title) {
		this.title = title;
	}

	public List<SearchField> getAuthor() {
		return author;
	}

	public void setAuthor(List<SearchField> author) {
		this.author = author;
	}

	public List<SearchField> getDigest() {
		return digest;
	}

	public void setDigest(List<SearchField> digest) {
		this.digest = digest;
	}

	public String getDte() {
		return dte;
	}

	public void setDte(String dte) {
		this.dte = dte;
	}

	public String getBfr() {
		return bfr;
	}

	public void setBfr(String bfr) {
		this.bfr = bfr;
	}

	public BasicSearchConfig() {

	}

	public BasicSearchConfig(String json) {
		BasicSearchConfig config = new Gson().fromJson(json, BasicSearchConfig.class);
		this.anyField = config.getAnyField();
		this.title = config.getTitle();
		this.digest = config.getDigest();
		this.author = config.getAuthor();
		this.bfr = config.getBfr();
		this.dte = config.getDte();
		config = null;
	}

	public BasicSearchConfig fromJSON(String json) {
		try {
			return new Gson().fromJson(json, BasicSearchConfig.class);
		} catch (Exception e) {
			return null;
		}
	}

	public BasicSearchConfig fromStream(InputStream in) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(new InputStreamReader(in), BasicSearchConfig.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public boolean isAnyFieldNotEnmpty() {
		return anyField != null && !anyField.isEmpty();
	}

	public boolean isAuthorNotEnmpty() {
		return author != null && !author.isEmpty();
	}

	public boolean isDigestNotEnmpty() {
		return digest != null && !digest.isEmpty();
	}

	public boolean isTitleNotEnmpty() {
		return title != null && !title.isEmpty();
	}

}
