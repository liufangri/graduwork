package graduwork;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Test;

import com.google.gson.Gson;

public class NetTest {
	@Test
	public void testJSONConfic() {
		Gson gson = new Gson();
		List<Map<String, String>> jsonList = new ArrayList<Map<String, String>>();
		FileReader reader;
		try {
			reader = new FileReader("D:\\JavaWorkspace1\\graduwork\\src\\main\\resources\\app\\DBConfigs\\DB.json");
			BufferedReader reader2 = new BufferedReader(reader);
			String buffer = "";
			StringBuffer sb = new StringBuffer();
			buffer = reader2.readLine();
			while (buffer != null) {
				sb.append(buffer);
				buffer = reader2.readLine();
			}
			jsonList = gson.fromJson(sb.toString(), jsonList.getClass());
			for (Map<String, String> map : jsonList) {
				System.out.println(map);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String json = "";
		jsonList = gson.fromJson(json, List.class);
	}

	public void testConnection() {
		URI uri;
		try {
			uri = new URIBuilder().setScheme("http").setHost("dl.acm.org").build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setURI(uri);

			HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
