package com.sxy.graduwork.po;

import java.sql.Timestamp;

import com.google.gson.Gson;

public class Article {
	private String doi;
	private String title;
	private String authors;
	private String publicationDate;
	private String source;
	private String importMark;
	private Timestamp importTime;
	private String enwLocation;
	private String pdfURL;

	public String getPdfURL() {
		return pdfURL;
	}

	public void setPdfURL(String pdfURL) {
		this.pdfURL = pdfURL;
	}

	public String getEnwLocation() {
		return enwLocation;
	}

	public void setEnwLocation(String enwLocation) {
		this.enwLocation = enwLocation;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getImportMark() {
		return importMark;
	}

	public void setImportMark(String importMark) {
		this.importMark = importMark;
	}

	public Timestamp getImportTime() {
		return importTime;
	}

	public void setImportTime(Timestamp importTime) {
		this.importTime = importTime;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
