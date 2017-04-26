package com.sxy.graduwork;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sxy.graduwork.context.ScriptAccessXmlApplicationContext;
import com.sxy.graduwork.tools.PropertiesTool;

public class App {

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		// insert current classpath into configuration
		PropertiesTool pt = new PropertiesTool("config/configuration");
		String classpath = pt.getClass().getResource("/").getPath().substring(1);
		String dbPath = classpath + "db/application";
		String logPath = classpath + "log/testlog.log";
		pt.addProperty("classpath", classpath).addProperty("dbcp.url", "jdbc:h2:" + dbPath).store();
		// log path
		pt.setPath("log4j.properties").addProperty("log4j.appender.R.File", logPath).store();
		Log logger = LogFactory.getLog(App.class);
		ScriptAccessXmlApplicationContext context = new ScriptAccessXmlApplicationContext("classpath:spring/*");
		// 初始化服务
		int res = context.initContext();

		if (res == ScriptAccessXmlApplicationContext.INIT_OK) {
			logger.info("System started in " + (System.currentTimeMillis() - t1) + "ms");
			// 开启前台，注意electron安装位置
			Process electron;
			try {
				pt.setPath(PropertiesTool.DEFAULT_PATH);
				String electronPath = pt.getValue("electron_location");
				electron = Runtime.getRuntime().exec(new String[] { classpath + electronPath, "." }, null, new File(classpath + "app"));

				// 开始监听前台
				int code = context.startListen();

				if (code == ScriptAccessXmlApplicationContext.LISTEN_RUNNING) {
					logger.info("System is already started!");
				}
				if (code == ScriptAccessXmlApplicationContext.LISTEN_EXIT_OK) {
					logger.info("Stoping system...");
					electron.destroy();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		context.close();
		logger.info("System exited.");
		System.exit(0);
	}
}
