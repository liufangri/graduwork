package graduwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class NetTest {
	String CFID;
	String CFTOKEN;

	@SuppressWarnings("unchecked")
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
			reader2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String json = "";
		jsonList = gson.fromJson(json, List.class);
	}

	// @Test
	public void testConnection() {
		URI uri;
		try {
			String host = "dl.acm.org";
			uri = new URIBuilder().setScheme("http").setHost(host).build();
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setURI(uri);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = null;

			response = httpClient.execute(httpGet);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String htmlContent = saveHtml(entity, "C:\\Users\\sxy90\\Desktop\\AMC.html");
				Document document = Jsoup.parse(htmlContent);
				Element e = document.getElementById("port");
				Element home = e.child(1);
				String homeUrl = home.attr("href");
				setCFs(homeUrl);
				Elements elements = document.getElementsByAttributeValueContaining("href", "advsearch.cfm");
				Element element = null;
				if (elements.size() != 0) {
					element = elements.get(0);
					String advSearchURL = element.attr("href");
					uri = new URIBuilder().setScheme("http").setHost(host).setPath("/" + advSearchURL).build();
					HttpGet httpGet2 = new HttpGet(uri);
					httpGet2.setHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
					response = httpClient.execute(httpGet2);
					entity = response.getEntity();
					saveHtml(entity, "C:\\Users\\sxy90\\Desktop\\AMCSearch.html");

				}

			}

			uri = new URIBuilder().setScheme("http").setHost(host).setPath("/results.cfm").setParameter("query", "(+Spring)")
					.setParameter("within", "owners.owner=HOSTED").setParameter("filtered", "").setParameter("bfr", "").build();
			System.out.println(uri.toString());
			HttpGet httpGet2 = new HttpGet(uri);

			httpGet2.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			response = httpClient.execute(httpGet2);
			entity = response.getEntity();
			saveHtml(entity, "C:\\Users\\sxy90\\Desktop\\ACMResult.html");

			response.close();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setCFs(String homeUrl) {

		CFID = getUrlParameter(homeUrl, "CFID");
		CFTOKEN = getUrlParameter(homeUrl, "CFTOKEN");
	}

	private String getUrlParameter(String url, String paramName) {
		int pos1 = url.indexOf(paramName);
		if (pos1 == -1) {
			return null;
		}
		int pos2 = url.substring(pos1, url.length()).indexOf("&");
		if (pos2 == -1) {
			pos2 = url.length();
		} else {
			pos2 += pos1;
		}
		String result = url.substring(pos1 + paramName.length() + 1, pos2);
		return result;
	}

	public void testResult() throws IOException {
		testConnection();
		String htmlContent = getHtmlContent("C:\\Users\\sxy90\\Desktop\\ACMResult.html");
		Document document = Jsoup.parse(htmlContent);
		Element results = document.getElementById("results");
		List<String> URLlist = new ArrayList<String>();
		Elements elements = results.getElementsByClass("title");
		for (Element e : elements) {
			String url = e.child(0).attr("href");
			System.out.println(url);
			URLlist.add(url);
		}
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			String uriStr = URLlist.get(0);
			Map<String, String> params = parseACMInfo(uriStr);
			URIBuilder builder = new URIBuilder().setHost("dl.acm.org").setScheme("http").setPath("/citation.cfm");
			for (Entry<String, String> entry : params.entrySet()) {
				builder.setParameter(entry.getKey(), entry.getValue());
			}
			URI uri = builder.build();
			System.out.println(uri);
			HttpGet httpGet = new HttpGet(uri);
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			CloseableHttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			saveHtml(entity, "C:\\Users\\sxy90\\Desktop\\ACMArtical.html");

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	private String getHtmlContent(String path) throws FileNotFoundException, IOException {
		StringBuffer sBuffer = new StringBuffer();
		FileReader fileReader = new FileReader(path);
		BufferedReader reader = new BufferedReader(fileReader);
		String buffer = "";

		while (buffer != null) {
			sBuffer.append(buffer);
			buffer = reader.readLine();
		}
		reader.close();
		fileReader.close();
		String htmlContent = sBuffer.toString();
		return htmlContent;
	}

	// [host/]downformats.cfm?id=2911184&parent_id=2911172&expformat=endnotes&CFID=704006689&CFTOKEN=26722465
	// https://dl.acm.org/purchase.cfm?id=162887&CFID=885130552&CFTOKEN=31047748

	public void getEndnote() throws FileNotFoundException, IOException {
		testResult();
		String host = "dl.acm.org";
		String htmlcontent = getHtmlContent("C:\\Users\\sxy90\\Desktop\\ACMArtical.html");
		Document document = Jsoup.parse(htmlcontent);
		Elements elements = document.getElementsByAttributeValue("name", "citation_doi");
		Element element = elements.first();
		String citation_doi = element.attr("content");
		System.out.println(citation_doi);
		elements = document.getElementsByAttributeValue("name", "citation_abstract_html_url");
		element = elements.first();
		String citation_abstract_html_url = element.attr("content");
		System.out.println(citation_abstract_html_url);

		elements = document.getElementsByAttributeValue("name", "citation_pdf_url");
		element = elements.first();
		String citation_pdf_url = element.attr("content");
		System.out.println(citation_pdf_url);

		String parentId = getFatherID(citation_doi);
		String id = getID(citation_doi);
		URI uri;
		URIBuilder builder = new URIBuilder();
		builder.setHost(host).setScheme("http").setPath("/downformats.cfm");
		builder.setParameter("id", id);
		builder.setParameter("parent_id", parentId);
		builder.setParameter("expformat", "endnotes");
		builder.setParameter("CFID", CFID).setParameter("CFTOKEN", CFTOKEN);
		try {
			uri = builder.build();
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet(uri);
			get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			CloseableHttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			saveHtml(entity, "C:\\Users\\sxy90\\Desktop\\pdfpage.html");

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public void testParent() {
		String doi = "10.1145/162754.162887";
		System.out.println("parent id:" + getFatherID(doi));
	}

	public void testId() {
		String doi = "10.1145/162754.162887";
		System.out.println("id:" + getID(doi));
	}

	private Map<String, String> parseACMInfo(String uriStr) {
		Map<String, String> params = new HashMap<String, String>();
		String id = getUrlParameter(uriStr, "id");
		params.put("id", id);

		return params;
	}

	private String saveHtml(HttpEntity entity, String path) throws IOException {
		InputStream iStream = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
		StringBuffer content = new StringBuffer();
		String buffer = "";
		buffer = reader.readLine();

		while (buffer != null) {
			System.out.println(buffer);
			content.append(buffer + "\n");
			buffer = reader.readLine();
		}
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		String htmlContent = content.toString();
		FileWriter writer = new FileWriter(file);
		writer.write(htmlContent);
		writer.close();
		return htmlContent;
	}

	private String getFatherID(String doi) {
		String later = doi.split("/")[1];
		return later.substring(0, later.indexOf('.'));
	}

	private String getID(String doi) {
		String later = doi.split("/")[1];
		return later.substring(later.indexOf(".") + 1, later.length());
	}
}
