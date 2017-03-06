package com.sxy.graduwork.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import com.sxy.graduwork.context.SystemParameters;
import com.sxy.graduwork.searchconfig.ACMSearchConfig;
import com.sxy.graduwork.tools.HttpClientTool;
import com.sxy.graduwork.tools.NetTool;

public class ACMService extends AbstractDatabaseService {

	private String CFID;
	private String CFTOKEN;
	private Map<String, String> acmConfigMap;

	private ACMSearchConfig acmSearchConfig;

	/**
	 * Link should look like
	 * http://dl.acm.org/dl.cfm?CFID=904558317&CFTOKEN=79764837
	 * 
	 * @param link
	 */
	private void setCFs(String link) {
		CFID = NetTool.getUrlParameter(link, "CFID");
		CFTOKEN = NetTool.getUrlParameter(link, "CFTOKEN");
	}

	/**
	 * Get CFID and CFTOKEN
	 * 
	 * @return
	 */
	public String connectHost() {
		String scheme = acmConfigMap.get("scheme");
		String host = acmConfigMap.get("host");
		String content = null;
		try {
			URI uri = new URIBuilder().setScheme(scheme).setHost(host).build();
			content = HttpClientTool.getContentStringByUri(uri);
			Document document = Jsoup.parse(content);
			Node node = document.getElementById("port");
			Node n = node.childNode(1);
			String fullLink = n.attr("href");
			setCFs(fullLink);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public String getSearchResultPage(String queryStr, String bfr, String dte) {
		String content = null;
		String host = acmConfigMap.get("host");
		String scheme = acmConfigMap.get("scheme");
		String resultPath = acmConfigMap.get("resultPath");

		try {
			URI uri = new URIBuilder().setScheme(scheme).setHost(host).setPath(resultPath).setParameter("query", queryStr)
					.setParameter("within", "owners.owner=HOSTED").setParameter("filtered", "").setParameter("bfr", bfr).setParameter("dte", dte)
					.build();
			content = HttpClientTool.getContentStringByUri(uri);

			// »ñÈ¡results divÔªËØ

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}


	@Override
	public void prepare() {
		super.prepare();
		acmConfigMap = super.getDbrConfig().getDbrMap().get(SystemParameters.ACM_NAME);
		System.out.println(connectHost());
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
