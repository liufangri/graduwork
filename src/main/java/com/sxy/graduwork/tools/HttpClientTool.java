package com.sxy.graduwork.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.sxy.graduwork.network.ChromeHttpGet;

public class HttpClientTool {
	/**
	 * Get the content of HttpEntity
	 * 
	 * @param entity
	 * @return the content of HttpEntity as String
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static String getContentStringByHttpEntity(HttpEntity entity) throws UnsupportedOperationException, IOException {

		InputStream iStream = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
		StringBuffer content = new StringBuffer();
		String buffer = "";
		buffer = reader.readLine();

		while (buffer != null) {
			System.out.println(buffer);
			content.append(buffer + "\n");
			buffer = reader.readLine();
		}
		return content.toString();
	}

	/**
	 * Get content from a URI
	 * 
	 * @param uri
	 * @return the content from a URI as String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getContentStringByUri(URI uri) throws ClientProtocolException, IOException {
		ChromeHttpGet httpGet = new ChromeHttpGet(uri);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		response.close();
		httpClient.close();
		String content = HttpClientTool.getContentStringByHttpEntity(entity);
		return content;
	}
}
