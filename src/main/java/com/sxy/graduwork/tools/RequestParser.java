package com.sxy.graduwork.tools;

import java.util.HashMap;
import java.util.Map;

import com.sxy.graduwork.po.RequestInfo;

public class RequestParser {
	public static final int REQUEST_METHOD = 1;
	public static final int REQUEST_PROPERTY = 2;
	public static final int REQUEST_ILLEGAL = -1;

	public static RequestInfo parseRequest(String message) {
		int kind = getRequestType(message);
		RequestInfo ri = new RequestInfo();
		ri.setKind(kind);
		switch (kind) {
		case REQUEST_METHOD:
			String beanName = getRequestBeanName(message);
			ri.setBeanName(beanName);
			if (beanName != null && !beanName.equals("")) {
				String methodName = getRequestMethodName(message);
				ri.setMethodName(methodName);
				if (beanName != null && !beanName.equals("")) {
					Map<String, String> paramMap = getRequestParms(message);
					ri.setParamMap(paramMap);
				} else {
					break;
				}

			} else {
				break;
			}

		case REQUEST_PROPERTY:

			break;
		case REQUEST_ILLEGAL:

			break;
		default:// do resolve as request is illegal

			break;
		}
		return ri;
	}

	public static int getRequestType(String message) {
		if (message.startsWith("@method:")) {
			return REQUEST_METHOD;
		} else if (message.startsWith("@property:")) {
			return REQUEST_PROPERTY;
		} else {
			return REQUEST_ILLEGAL;
		}
	}

	public static String getRequestMethodName(String message) {
		int pos0 = message.indexOf('!');
		int pos1 = message.indexOf('?');
		if (pos0 != -1 && pos1 != -1 && pos0 < pos1) {
			return message.substring(pos0 + 1, pos1);
		} else if (pos0 != -1 && pos1 == -1) {
			return message.substring(pos0 + 1, message.length());
		} else {
			return null;
		}
	}

	public static String getRequestBeanName(String message) {
		int pos0 = message.indexOf(':');
		int pos1 = message.indexOf('!');
		if (pos0 != -1 && pos1 != -1 && pos0 < pos1) {
			return message.substring(pos0 + 1, pos1);
		} else {
			return null;
		}
	}

	public static Map<String, String> getRequestParms(String message) {
		int pos0 = message.indexOf('?');
		if (pos0 == -1) {
			return null;
		} else {
			String[] paramStrs = message.substring(pos0 + 1, message.length()).split("&");
			if (paramStrs.length < 1) {
				return null;
			} else {
				Map<String, String> params = new HashMap<String, String>();
				for (int i = 0; i < paramStrs.length; i++) {
					String parmStr = paramStrs[i];
					String[] keyValue = parmStr.split("=");
					if (keyValue.length > 1) {
						params.put(keyValue[0], keyValue[1]);
					} else if (keyValue.length == 1) {
						params.put(keyValue[0], "");
					} else {

						return null;
					}
				}
				return params;
			}
		}

	}
}
