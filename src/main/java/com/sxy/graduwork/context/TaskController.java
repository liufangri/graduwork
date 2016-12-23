package com.sxy.graduwork.context;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.sxy.graduwork.po.RequestInfo;
import com.sxy.graduwork.tools.RequestParser;

/**
 * Controls the events called by View
 * 
 * @author Y400
 *
 */
public class TaskController implements Runnable, ApplicationContextAware {

	public static final int INVOKE_ERROR = -1;
	public static final int INVOKE_ERROR_ILLEGAL_ACCESS = -2;
	public static final int INVOKE_ERROR_PARAMETER_ERROR = -3;
	public static final int INVOKE_ERROR_ILLEGAL_ARGUMENT = -4;
	public static final int INVOKE_ERROR_NO_SUCH_METHOD = -5;
	public static final int INVOKE_ERROR_NO_SUCH_BEAN = -6;
	public static final int EXECUTE_OK = 0;

	private ApplicationContext applicationContext;
	private RequestInfo requestInfo;
	private Socket socket;

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * This kind of method been invoked has zero parameter and public scope
	 * 
	 * @param beanName
	 * @param methodName
	 * @return
	 */
	public int invokeMethod(String beanName, String methodName, StringBuffer sb) {
		Object bean = applicationContext.getBean(beanName);
		if (bean == null) {
			return INVOKE_ERROR_NO_SUCH_BEAN;
		}
		Method method = null;
		try {
			method = bean.getClass().getMethod(methodName);
			Object result = method.invoke(bean);
			// Result should be a JSON String
			sb.append(result.toString());

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return INVOKE_ERROR_NO_SUCH_METHOD;
		} catch (SecurityException e) {
			e.printStackTrace();
			return INVOKE_ERROR;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return INVOKE_ERROR_ILLEGAL_ACCESS;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return INVOKE_ERROR_ILLEGAL_ARGUMENT;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return INVOKE_ERROR;
		}

		return EXECUTE_OK;
	}

	/**
	 * Call a stander method which have a <code>Map</code> parameter
	 * 
	 * @param beanName
	 * @param methodName
	 * @param paramMap
	 * @return
	 */
	public int invokeMethod(String beanName, String methodName, StringBuffer sb, Map<String, String> paramMap) {
		Object bean = applicationContext.getBean(beanName);
		if (bean == null) {
			return INVOKE_ERROR_NO_SUCH_BEAN;
		}
		Method method = null;
		try {
			method = bean.getClass().getMethod(methodName, Map.class);
			Object result = method.invoke(bean, paramMap);
			// Result should be a JSON String
			sb.append(result.toString());

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return INVOKE_ERROR_NO_SUCH_METHOD;
		} catch (SecurityException e) {
			e.printStackTrace();
			return INVOKE_ERROR;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return INVOKE_ERROR_ILLEGAL_ACCESS;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return INVOKE_ERROR_ILLEGAL_ARGUMENT;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return INVOKE_ERROR;
		}

		return EXECUTE_OK;
	}

	private String service(RequestInfo requestInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("");
		switch (requestInfo.getKind()) {
		case RequestParser.REQUEST_METHOD:
			int resCode;
			if (requestInfo.getParamMap() == null) {
				resCode = invokeMethod(requestInfo.getBeanName(), requestInfo.getMethodName(), sb);
			} else {
				resCode = invokeMethod(requestInfo.getBeanName(), requestInfo.getMethodName(), sb, requestInfo.getParamMap());
			}
			if (resCode < 0) {
				// Handle the error code
				return "Invoke failed, error code:" + resCode;
			} else {
				return sb.toString();
			}
		default:
			break;
		}
		return "Unkown request.";
	}

	public void run() {
		if (requestInfo != null) {
			String result = service(this.requestInfo);
			try {
				PrintWriter os = new PrintWriter(socket.getOutputStream());
				os.write(result);
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
