package com.sxy.graduwork;

import com.sxy.graduwork.context.ScriptAccessXmlApplicationContext;
import com.sxy.graduwork.tools.PropertiesTool;

public class App {

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		// insert current classpath into configuration
		PropertiesTool pt = new PropertiesTool("config/configuration");
		pt.addProperty("classpath", pt.getClass().getResource("/").getPath().substring(1));
		// insert db path
		pt.addProperty("dbcp.url", "jdbc:h2:" + pt.getValue("classpath") + "db/application");
		pt.store();
		ScriptAccessXmlApplicationContext context = new ScriptAccessXmlApplicationContext("classpath:spring/*");
		int res = context.initContext();
		if (res == ScriptAccessXmlApplicationContext.INIT_OK) {
			System.out.println("System start with " + (System.currentTimeMillis() - t1) + "ms");
			int code = context.startListen();
			if (code == ScriptAccessXmlApplicationContext.LISTEN_RUNNING) {
			}
		}
		System.out.println(res);

		context.close();
	}
}
