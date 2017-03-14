package com.sxy.graduwork.schedu;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;

import com.google.gson.Gson;
import com.sxy.graduwork.po.SearchConfig;
import com.sxy.graduwork.searchconfig.BasicSearchConfig;
import com.sxy.graduwork.service.SearchConfigService;

public class ScheduJobManager {

	private SearchConfigService searchConfigService;
	private Gson gson = new Gson();

	public void setSearchConfigService(SearchConfigService searchConfigService) {
		this.searchConfigService = searchConfigService;
	}

	private Scheduler scheduler;

	private static Log logger = LogFactory.getLog(ScheduJobManager.class);

	public Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * ��ʼ�����ж�ʱ����
	 */
	public void init() {
		List<SearchConfig> configs = searchConfigService.getConfigList();

		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		createScheduJobs(configs);

	}

	/**
	 * ����һ����ʱ��������ע�ᵽϵͳ��
	 * 
	 * @param config
	 */
	public void createScheduJob(SearchConfig config, String searchJobGroupID) {

		// ��ʱ����ĳ�Ա����
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("searchConfig", gson.fromJson(config.getConfigJson(), BasicSearchConfig.class));
		// ��ʱ��������
		String searchJobID = "Job_" + config.getId();
		String triggerID = "Trigger_" + config.getId();
		String cronPattern = createCronExpression(config.getSchedulePattern());
		JobDetail job = newJob(SearchJob.class).setJobData(dataMap).withIdentity(searchJobID, searchJobGroupID).build();
		Trigger trigger = newTrigger().withIdentity(triggerID, searchJobGroupID).withSchedule(cronSchedule(cronPattern))
				.forJob(searchJobID, searchJobGroupID).build();
		// ������ʹ�����ע�ᵽϵͳ��
		try {
			scheduler.scheduleJob(job, trigger);
			logger.info("Search job: " + searchJobID + " added and schedued.");
		} catch (SchedulerException e) {
			logger.error("Search job: " + searchJobID + " add failed!");
			e.printStackTrace();
		}
	}

	/**
	 * �������������б�����������
	 * 
	 * @param configs
	 * @throws SchedulerException
	 */
	private void createScheduJobs(List<SearchConfig> configs) {
		String searchJobGroupID = "SearchJobGroup";
		for (SearchConfig config : configs) {
			createScheduJob(config, searchJobGroupID);
		}
	}

	/**
	 * ˢ�����еĶ�ʱ��������ж�ʱ��������ִ�У��ȴ���ִ�����
	 */
	public void refresh() {

		// ���»�ȡ���ݿ����������
		List<SearchConfig> configs = searchConfigService.getConfigList();
		List<JobExecutionContext> jobExecutionContexts = null;
		// ��ȡ��������ִ�е�����
		try {
			jobExecutionContexts = scheduler.getCurrentlyExecutingJobs();
			if (jobExecutionContexts == null || jobExecutionContexts.size() == 0) {
				// ��ʼˢ��
				logger.info("Starting to refresh schedued search tasks");
				scheduler.clear();
				createScheduJobs(configs);
			} else {
				logger.info("Refresh scheduler failed, some tasks still running.");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ��ϵͳ��ɾ�����ж�ʱ����
	 */
	public void deleteAll() {
		try {
			scheduler.clear();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ָֹ���Ķ�ʱ����
	 * 
	 * @param jobName
	 * @param groupName
	 */
	public void abandonRunningJob(String jobName, String groupName) {
		JobKey key = new JobKey(jobName, groupName);

		try {
			if (scheduler.interrupt(key)) {
				logger.info("Interrupted job: " + key.getName());
			}

		} catch (UnableToInterruptJobException e) {
			logger.error("Interrupt job: " + key.getName() + " failed");
			e.printStackTrace();
		}
	}

	/**
	 * ��ֹ��������ִ���еĶ�ʱ����
	 */
	public void abandonAllRunningJob() {
		List<JobExecutionContext> contexts;
		try {
			contexts = scheduler.getCurrentlyExecutingJobs();
			for (JobExecutionContext context : contexts) {
				JobKey key = context.getJobDetail().getKey();
				if (scheduler.interrupt(key)) {
					logger.info("Interrupted job: " + key.getName());
				} else {
					logger.error("Interrupt job: " + key.getName() + " failed");
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ��ָ��name�Ķ�ʱ����
	 * 
	 * @param jobName
	 */
	public void deleteScheduedJob(String jobName) {
		try {
			if (scheduler.deleteJob(JobKey.jobKey(jobName, "SearchJobGroup"))) {
				logger.info("Delete schedu job: " + jobName + " succeed.");
			} else {
				logger.error("Delete schedu job: " + jobName + " failed.");
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����Cron Expression
	 * 
	 * @param scheduPattern
	 * @return
	 */
	public static String createCronExpression(String scheduPattern) {
		StringBuffer resultBuffer = new StringBuffer();
		// ���ÿ�ʼʱ��Ϊ����ʮ��
		resultBuffer.append("0 0 10 ");
		if (scheduPattern.startsWith("W:")) {
			String weeks = scheduPattern.substring(2, scheduPattern.length());
			resultBuffer.append("? * " + weeks);
		} else if (scheduPattern.startsWith("M:")) {
			String dates = scheduPattern.substring(2, scheduPattern.length());
			resultBuffer.append(dates + " * ?");
		}
		return resultBuffer.toString();
	}

	/**
	 * �رն�ʱ����
	 */
	public void shutdown() {
		try {
			scheduler.shutdown();
			logger.info("Scheduler shutdown.");
		} catch (SchedulerException e) {
			logger.info("Scheduler failed to shutdown.");
			e.printStackTrace();
		}
	}
}
