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
}
