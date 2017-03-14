package graduwork;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.google.gson.Gson;
import com.sxy.graduwork.context.SystemParameters;
import com.sxy.graduwork.po.Article;
import com.sxy.graduwork.schedu.ScheduJobManager;
import com.sxy.graduwork.searchconfig.BasicSearchConfig;
import com.sxy.graduwork.tools.JSONTool;
import com.sxy.graduwork.tools.MD5;
import com.sxy.graduwork.tools.NetTool;
import com.sxy.graduwork.tools.PropertiesTool;

public class TestClass {

	public void testPropertiesTool() {
		try {
			PropertiesTool propertiesTool = new PropertiesTool("config/parameters.properties");
			propertiesTool.refresh();
			System.out.println(propertiesTool.getValue("port"));
			propertiesTool.addProperty("port", "233").store();
			System.out.println(propertiesTool.getValue("port"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testPort() {
		System.out.println(NetTool.isPortAvailable(7890));
	}

	public void testSplit() {
		String aString = "?a=1&b=&c=";
		String[] aStrings = aString.split("&");
		for (String a : aStrings) {
			System.out.println(a);
		}
	}

	public void testString() {
		System.out.println(new Object[] { "123", "123" });
		System.out.println(hashCode());
		String[] a = new String[] { "1", "2" };
		System.out.println(a instanceof Object[]);
	}

	public void testJSONTool() {
		Object x = new Object() {
			private int a = 1;

			@SuppressWarnings("unused")
			public int getA() {
				return a;
			}

			private Date b = new Date();

			@SuppressWarnings("unused")
			public Date getB() {
				return b;
			}
		};
		List<Object> b = new ArrayList<Object>();
		b.add(x);
		Map<String, Object> c = new HashMap<String, Object>();
		c.put("1", x);
		System.out.println(JSONTool.parseJSON(c));
		// System.out.println(JSONTool.parseJSON(b));
	}

	public void testLog() {

	}

	public void testGetParameter() {
		String vaString = NetTool.getUrlParameter("http://dl.acm.org/dl.cfm?CFID=904558317&CFTOKEN=79764837", "CFID");
		vaString = vaString.trim();
		System.out.println(vaString + " " + vaString.length());
	}

	public void testSystemUserDir() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(SystemParameters.DEFAULT_DB_CONFIG_PATH);
		Reader reader = new InputStreamReader(inputStream);
		Gson gson = new Gson();
		List dbList = gson.fromJson(reader, List.class);
		System.out.println(dbList);

	}

	public void testGson() {
		BasicSearchConfig config = new Gson().fromJson(
				"{\"anyField\":[{\"match\":\"+\",\"value\":\"123\"}],\"title\":[{\"match\":\"\",\"value\":\"123\"}],\"author\":[{\"match\":\"-\",\"value\":\"123\"}],\"digest\":[],\"dte\":\"1947\"}",
				BasicSearchConfig.class);
		System.out.println(config.toString());
	}

	public void testExtents() {
		TestA a = new TestA();

	}

	public void TestCalendar() {
		Calendar current = Calendar.getInstance();
		current.add(Calendar.DAY_OF_MONTH, 1);
		System.out.println(current.get(Calendar.DAY_OF_WEEK));
	}

	public void TestSchedu() {
		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			scheduler.start();

			JobDataMap newJobDataMap = new JobDataMap();
			Article article = new Article();
			article.setAuthors("sxy");
			newJobDataMap.put("article", article);
			JobDetail job = JobBuilder.newJob(MyJob.class).withIdentity("MyJob", "Job1").setJobData(newJobDataMap).build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1").startNow()
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

			scheduler.scheduleJob(job, trigger);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			scheduler.shutdown();

		} catch (SchedulerException se) {
			se.printStackTrace();
		}
	}

	public void testCron() {
		System.out.println(ScheduJobManager.createCronExpression("W:1,3,4,5"));
		System.out.println(ScheduJobManager.createCronExpression("M:1,15,L"));
	}

	public void testMulty() {
		String lock = "1";
		Thread thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (lock) {

					if (lock.equals("1")) {
						try {
							System.out.println("Thread1 running");
							lock.wait();
							System.out.println("Thread1 stopping");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		Thread thread2 = new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (lock) {

					System.out.println("Thread2 running");
					try {

						Thread.sleep(3000);
						lock.notifyAll();
						System.out.println("Thread2 stopping");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});

		thread1.start();
		thread2.start();
	}

	public void testMD5() {
		System.out.println(MD5.Md5_32("10.1145/2532780.2532802"));
	}

	public void testExport() {
		try {
			Runtime.getRuntime().exec("D:\\JavaWorkspace\\graduwork\\target\\classes\\cache\\A9AD38215264A042B9F4D50247DDECAA.enw");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
