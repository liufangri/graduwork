package com.sxy.graduwork.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

import com.sxy.graduwork.po.RequestInfo;
import com.sxy.graduwork.tools.NetTool;
import com.sxy.graduwork.tools.PropertiesTool;
import com.sxy.graduwork.tools.RequestParser;

public class ScriptAccessXmlApplicationContext extends FileSystemXmlApplicationContext {
	public static final int INIT_OK = 0;
	public static final int INIT_FAIL = -1;
	public static final int INIT_PORT_ERROR = -2;

	public static final int LISTEN_IO_FAIL = -2;
	public static final int LISTEN_FAIL = -1;
	public static final int LISTEN_EXIT_OK = 0;

	public static final int LISTEN_RUNNING = 1;
	public static final int LISTEN_STOPED = 0;

	public static final int SERVER_RUNNING = 1;
	public static final int SERVER_STOPED = -1;

	private Log logger = LogFactory.getLog(ScriptAccessXmlApplicationContext.class);
	private int port;
	private ServerSocket server;
	private int serverState = SERVER_STOPED;
	private int listenState = LISTEN_STOPED;

	public int getPort() {
		return port;
	}

	public int getServerState() {
		return serverState;
	}

	public int getListenState() {
		return listenState;
	}

	public ServerSocket getServer() {
		return this.server;
	}

	public ScriptAccessXmlApplicationContext() {
		super();
	}

	public ScriptAccessXmlApplicationContext(String... locations) {
		super(locations);
	}

	public synchronized int initContext() {
		if (serverState == SERVER_STOPED) {
			boolean portAvailable = false;
			PropertiesTool pt = new PropertiesTool();
			String pv = "";
			pv = pt.getValue("port");
			try {
				port = Integer.parseInt(pv);
			} catch (Exception e) {
				logger.error("Can't config port:" + pv + ", check the properties setting.");
				return INIT_PORT_ERROR;
			}

			portAvailable = NetTool.isPortAvailable(port);
			while (!portAvailable) {
				// create a port randomly from 1024 to 65535
				port = 1024 + (int) (Math.random() * 64511);
				portAvailable = NetTool.isPortAvailable(port);

			}
			pt.addProperty("current_port", String.valueOf(port)).store();

			try {
				server = new ServerSocket(port);
				serverState = SERVER_RUNNING;
				logger.info("System using port:" + port);
			} catch (IOException e) {
				serverState = SERVER_STOPED;
				e.printStackTrace();
				logger.info("System cannot use port:" + port);
				return INIT_FAIL;
			}

		} else if (serverState == SERVER_RUNNING) {
			logger.info("System is already running at port: " + port + ".");
		}

		return INIT_OK;
	}

	public synchronized int startListen() {
		if (listenState == LISTEN_STOPED) {

			Socket socket = null;
			listenState = LISTEN_RUNNING;
			try {
				String message = "";
				// Code to exit listen
				while (!message.equals("\\exit system")) {
					socket = server.accept();
					BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					message = is.readLine();
					logger.info("System catches a message:" + message);
					RequestInfo ri = RequestParser.parseRequest(message);
					TaskExecutor taskExecutor = (TaskExecutor) this.getBean("taskExecutor");
					TaskController taskController = (TaskController) this.getBean("taskController");
					taskController.setRequestInfo(ri);
					taskController.setSocket(socket);
					taskExecutor.execute(taskController);
				}
			} catch (IOException e) {
				e.printStackTrace();
				listenState = LISTEN_STOPED;
				return LISTEN_IO_FAIL;
			}
		} else if (listenState == LISTEN_RUNNING) {
			logger.info("System is already listening.");
			return LISTEN_RUNNING;
		}
		return LISTEN_EXIT_OK;
	}

	@Override
	public void close() {
		try {
			server.close();
			super.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Cannot close server.");
		}
	}

}
