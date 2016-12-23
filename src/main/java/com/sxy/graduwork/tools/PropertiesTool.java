package com.sxy.graduwork.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * The location of default <code>.properties<code> file is
 * "classpath:parameters.properties"
 * 
 * @author sunxiaoyu
 *
 */
public class PropertiesTool {
	public static final String DEFAULT_PATH = "parameters.properties";
	private Properties properties;
	private String path = DEFAULT_PATH;

	public PropertiesTool() {
		try {
			this.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PropertiesTool(String path) {
		this.path = path;
		try {
			this.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public PropertiesTool setPath(String path) {
		this.path = path;
		try {
			this.refresh();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getPath() {
		return this.path;
	}

	public PropertiesTool addProperty(String key, String value) {
		properties.setProperty(key, value);
		return this;
	}

	/**
	 * IF this properties file can be modified manually, call method
	 * <code>refresh</code> before calling this method.
	 * 
	 * @param name
	 * @return
	 */
	public String getValue(String name) {
		return properties.getProperty(name);

	}

	/**
	 * In consideration of this properties file being modified manually every
	 * time before read it, call method <code>refresh</code> before calling this
	 * method.
	 * 
	 * @return
	 */
	public Set<Object> getKeySet() {
		return properties.keySet();
	}

	public Map<String, String> getMap() {
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<Object, Object> entry : entrySet) {
			map.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return map;
	}

	public void store() {
		OutputStream out;
		try {
			out = new FileOutputStream(this.getClass().getClassLoader().getResource(path).getPath());
			properties.store(out, "");
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Use classLoader to reload file from path and refresh
	 * <code>properties</code>.
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void refresh() throws IOException {
		properties = new Properties();

		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(this.path));
		} catch (IOException e) {
			throw new IOException("Refresh properties failed.");
		}

	}

}
