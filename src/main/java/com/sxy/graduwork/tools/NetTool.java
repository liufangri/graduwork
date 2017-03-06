package com.sxy.graduwork.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class NetTool {

	private static void bindPort(String host, int port) throws Exception {
		Socket s = new Socket();
		s.bind(new InetSocketAddress(host, port));
		s.close();
	}

	/**
	 * Check this port is occupied or not.
	 * 
	 * @author jupiterbee@LinuxÉçÇø
	 * @param port
	 * @return returns <b>true</b> if this port is <b>available</b>.
	 */
	public static boolean isPortAvailable(int port) {
		try {
			bindPort("127.0.0.1", port);
			bindPort(InetAddress.getLocalHost().getHostAddress(), port);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	/**
	 * Get a parameter from a URL, if this URL doesn't contain this parameter,
	 * return empty string.
	 * 
	 * @param url
	 * @param paramName
	 * @return This parameter value or empty string if this parameter doesn't
	 *         exist.
	 */

	public static String getUrlParameter(String url, String paramName) {
		int pos1 = url.indexOf(paramName);
		if (pos1 == -1) {
			return null;
		}
		char[] b = new char[512];
		int i = pos1, j = 0;

		while (i < url.length() && url.charAt(i) != '=') {
			i += 1;
		}
		i += 1;
		while (i < url.length() && url.charAt(i) != '&') {
			b[j] = url.charAt(i);
			j += 1;
			i += 1;
		}
		String str = new String(b);
		return str.trim();
	}

	/**
	 * Save text resource(not binary file).
	 * 
	 * @param entity
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String saveText(HttpResponse response, String path) throws IOException {
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer content = new StringBuffer();
		String temp = "";
		temp = reader.readLine();

		while (temp != null) {
			content.append(temp + "\n");
			temp = reader.readLine();
		}
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		String htmlContent = content.toString();
		FileWriter writer = new FileWriter(file);
		writer.write(htmlContent);
		writer.close();
		return htmlContent;
	}
}
