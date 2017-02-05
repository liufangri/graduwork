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
	 * return <code>null</code>.
	 * 
	 * @param url
	 * @param paramName
	 * @return This parameter value or <code>null</code> if this parameter
	 *         doesn't exsit.
	 */
	public static String getUrlParameter(String url, String paramName) {
		int pos1 = url.indexOf(paramName);
		if (pos1 == -1) {
			return null;
		}
		int pos2 = url.substring(pos1, url.length()).indexOf("&");
		if (pos2 == -1) {
			pos2 = url.length();
		} else {
			pos2 += pos1;
		}
		String result = url.substring(pos1 + paramName.length() + 1, pos2);
		return result;
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
