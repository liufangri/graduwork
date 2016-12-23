package com.sxy.graduwork.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author sunxiaoyu
 *
 */
public class JSONTool {

	static class ParseException extends RuntimeException {

		private static final long serialVersionUID = 1123212131L;

		public ParseException() {
			super();
		}

		public ParseException(String message) {
			super(message);
		}
	}

	/**
	 * Parse from an object to JSON format String
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String parseJSON(Object object) {
		StringBuffer sb = new StringBuffer();
		sb.append("");
		try {
			object.hashCode();
		} catch (StackOverflowError e) {
			throw new ParseException("Stack over flow! Check the Object.");
		}
		if (object instanceof List) {
			sb.append("[");
			for (Object obj : (List) object) {
				String subString = parseJSON(obj);
				sb.append(subString + ",");
			}
			if (sb.charAt(sb.length() - 1) == ',') {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
		} else if (object instanceof Map) {
			sb.append("{");
			for (Entry<Object, Object> entry : ((Map<Object, Object>) object).entrySet()) {
				if (entry.getValue() instanceof String) {
					sb.append("\"" + entry.getKey() + "\":" + "\"" + entry.getValue().toString() + "\",");
				} else {// Other kind of values which can convert to String
						// directly should treat like String
					String subString = parseJSON(entry.getValue());
					sb.append(subString + ",");
				}
			}
			if (sb.charAt(sb.length() - 1) == ',') {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("}");
		} else if (object instanceof Set) {
			sb.append("[");
			for (Object obj : (Set) object) {
				String subString = parseJSON(obj);
				sb.append(subString + ",");
			}
			if (sb.charAt(sb.length() - 1) == ',') {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
		} else if (object instanceof Object[]) {
			sb.append("[");
			for (Object obj : (Object[]) object) {
				String subString = parseJSON(obj);
				sb.append(subString + ",");
			}
			if (sb.charAt(sb.length() - 1) == ',') {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
		} else {
			sb.append("{");
			Class clazz = object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				String getMethodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1, fieldName.length());
				Method getMethod = null;
				try {
					getMethod = clazz.getMethod(getMethodName);
				} catch (NoSuchMethodException e) {
					continue;
				} catch (SecurityException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				}
				getMethod.setAccessible(true);
				Object value = "";
				try {
					value = getMethod.invoke(object);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					continue;
				}
				sb.append("\"" + fieldName + "\":" + "\"" + value.toString() + "\",");
			}
			if (sb.charAt(sb.length() - 1) == ',') {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("}");
		}
		return sb.toString();
	}

}
