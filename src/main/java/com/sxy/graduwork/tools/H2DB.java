package com.sxy.graduwork.tools;

import java.sql.Connection;
import java.sql.DriverManager;

public class H2DB {
	public static void main(String[] args) {
		Connection connection = null;
		try {
			Class.forName("org.h2.Driver");
			H2DB h2db = new H2DB();

			String path = h2db.getClass().getResource("/db").getPath();
			System.out.println(path);
			connection = DriverManager.getConnection("jdbc:h2:" + path + "/application");
			System.out.println("Connect OK");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
