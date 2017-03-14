package com.sxy.graduwork.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
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
		String content = HttpClientTool.getContentStringByHttpEntity(entity);
		response.close();
		httpClient.close();
		return content;
	}

	/**
	 * Save Content from a URI into a file.
	 * 
	 * @param uri
	 * @param path
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static File saveContentOfUri(URI uri, String path) throws ClientProtocolException, IOException {
		String content = getContentStringByUri(uri);
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		String htmlContent = content.toString();
		FileWriter writer = new FileWriter(file);
		writer.write(htmlContent);
		writer.close();
		return file;
	}
}
