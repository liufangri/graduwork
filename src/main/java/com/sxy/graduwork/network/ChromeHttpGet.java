package com.sxy.graduwork.network;

import java.net.URI;

import org.apache.http.client.methods.HttpGet;

/**
 * 
 * @author Y400
 *
 */
public class ChromeHttpGet extends HttpGet {
	public ChromeHttpGet() {
		super();
		this.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	}

	public ChromeHttpGet(URI uri) {
		super(uri);
		this.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
	}
}
