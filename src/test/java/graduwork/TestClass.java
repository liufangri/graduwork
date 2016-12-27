package graduwork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sxy.graduwork.tools.JSONTool;
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
}
