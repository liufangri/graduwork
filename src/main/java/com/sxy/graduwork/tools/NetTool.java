package com.sxy.graduwork.tools;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

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

}
