package graduwork;

import com.sxy.graduwork.context.ScriptAccessXmlApplicationContext;

public class ContextTest {
	// @Test
	public void testContextInit() {
		long t1 = System.currentTimeMillis();
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
