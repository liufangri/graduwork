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
		String classpath = pt.getClass().getResource("/").getPath().substring(1);
		pt.addProperty("classpath", classpath);
		// insert db path
		pt.addProperty("dbcp.url", "jdbc:h2:" + pt.getValue("classpath") + "db/application");
		pt.store();
		pt.setPath("log4j.properties").addProperty("log4j.appender.R.File", classpath + "log/testlog.log").store();
		Log logger = LogFactory.getLog(App.class);
		ScriptAccessXmlApplicationContext context = new ScriptAccessXmlApplicationContext("classpath:spring/*");
		// 初始化服务
		int res = context.initContext();
		// 检索并设置已经存在的配置项到任务里
		if (res == ScriptAccessXmlApplicationContext.INIT_OK) {
			logger.info("System started in " + (System.currentTimeMillis() - t1) + "ms");
			// 开启前台，注意electron安装位置
			// try {
			// Runtime.getRuntime().exec(new String[] {
			// "C:\\Users\\sxy90\\AppData\\Roaming\\npm\\electron.cmd", "." },
			// null,
			// new File(classpath + "app"));
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
			// 开始监听前台
			int code = context.startListen();

			if (code == ScriptAccessXmlApplicationContext.LISTEN_RUNNING) {
				logger.info("System is already started!");
			}
		}
		System.out.println(res);

		context.close();
	}
}
