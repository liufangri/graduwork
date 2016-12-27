package com.sxy.graduwork;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
		pt.setPath("log4j.properties").addProperty("log4j.appender.R.File", pt.getClass().getResource("/").getPath().substring(1) + "log/testlog.log").store();
		Log logger = LogFactory.getLog(App.class);
		ScriptAccessXmlApplicationContext context = new ScriptAccessXmlApplicationContext("classpath:spring/*");
		int res = context.initContext();
		if (res == ScriptAccessXmlApplicationContext.INIT_OK) {
			logger.info("System started in " + (System.currentTimeMillis() - t1) + "ms");
			int code = context.startListen();
			if (code == ScriptAccessXmlApplicationContext.LISTEN_RUNNING) {
				logger.info("System is already started!");
			}
		}
		System.out.println(res);
		context.close();
	}
}
