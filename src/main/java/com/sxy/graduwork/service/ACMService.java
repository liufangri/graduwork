package com.sxy.graduwork.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.sxy.graduwork.context.SystemParameters;
import com.sxy.graduwork.dao.ArticleDao;
import com.sxy.graduwork.po.Article;
import com.sxy.graduwork.searchconfig.ACMSearchConfig;
import com.sxy.graduwork.searchconfig.BasicSearchConfig;
import com.sxy.graduwork.tools.HttpClientTool;
import com.sxy.graduwork.tools.MD5;
import com.sxy.graduwork.tools.NetTool;
import com.sxy.graduwork.tools.PropertiesTool;

public class ACMService extends AbstractDatabaseService {

	private String CFID;
	private String CFTOKEN;
	private Map<String, String> acmConfigMap;
	private static Log logger = LogFactory.getLog(ACMService.class);
	private ArticleDao articleDao;


	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}


	/**
	 * Link may look like
	 * http://dl.acm.org/dl.cfm?CFID=904558317&CFTOKEN=79764837
	 * 
	 * @param link
	 */
	private void setCFs(String link) {
		CFID = NetTool.getUrlParameter(link, "CFID");
		CFTOKEN = NetTool.getUrlParameter(link, "CFTOKEN");
	}

	/**
	 * Get CFID and CFTOKEN
	 * 
	 * @return
	 */
	public String connectHost() {
		String scheme = acmConfigMap.get("scheme");
		String host = acmConfigMap.get("host");
		String content = null;
		try {
			URI uri = new URIBuilder().setScheme(scheme).setHost(host).build();
			content = HttpClientTool.getContentStringByUri(uri);
			Document document = Jsoup.parse(content);
			Node node = document.getElementById("port");
			Node n = node.childNode(3);
			String fullLink = n.attr("href");
			setCFs(fullLink);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public String getSearchResultPage(int page, BasicSearchConfig searchConfig) {
		ACMSearchConfig acmSearchConfig = new ACMSearchConfig(searchConfig);
		String queryStr = acmSearchConfig.toACMQueryString();
		String bfr = acmSearchConfig.getBfr();
		String dte = acmSearchConfig.getDte();

		String content = null;
		String host = acmConfigMap.get("host");
		String scheme = acmConfigMap.get("scheme");
		String resultPath = acmConfigMap.get("resultPath");
		int pageSize = Integer.parseInt(acmConfigMap.get("pageSize"));
		int startPosition = (page - 1) * pageSize;

		try {
			URI uri;
			URIBuilder builder = new URIBuilder().setScheme(scheme).setHost(host).setPath(resultPath).setParameter("query", queryStr)
					.setParameter("within", "owners.owner=HOSTED").setParameter("filtered", "").setParameter("bfr", bfr).setParameter("dte", dte)
					.setParameter("CFID", CFID).setParameter("CFTOKEN", CFTOKEN).setParameter("srt", "publicationDate");
			if (startPosition != 0) {
				builder.setParameter("start", String.valueOf(startPosition));
			}
			uri = builder.build();
			content = HttpClientTool.getContentStringByUri(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	@Override
	public void prepare() {
		super.prepare();
		acmConfigMap = super.getDatabaseResourceConfig().getDbrMap().get(SystemParameters.ACM_SHORT_NAME);
		connectHost();

		// System.out.println(connectHost());
	}

	@Override
	public Map<String, Article> getSearchResultArticleMap(BasicSearchConfig searchConfig) {
		Map<String, Article> filePathMap = new HashMap<>();
		Map<String, String> fullTextURLMap = new HashMap<>();
		// 第一次获取搜索界面，取得一些参数
		String searchResultContentHTML = this.getSearchResultPage(1, searchConfig);
		Document document = Jsoup.parse(searchResultContentHTML);
		int articalNumRange = getNumRange(document);
		// 根据总数来判断分页数目
		int pageNum = articalNumRange / 20 + 1;
		for (int i = 1; i <= 1; i++) {
			getEndnoteFilesByPage(i, filePathMap, fullTextURLMap, searchConfig);
		}

		return filePathMap;
	}

	@Override
	public Map<String, String> getFullTextURLMap() {

		return null;
	}

	/**
	 * 获取该页面的所有论文页面URL
	 * 
	 * @param citationURLList
	 * @param document
	 */
	private void getCitationURL(List<String> citationURLList, Document document) {
		Elements elements = document.getElementsByClass("title");
		for (Element element : elements) {
			String URL = element.childNodes().get(1).attr("href");
			citationURLList.add(URL);
		}
	}

	/**
	 * 获取搜索结果数量
	 * 
	 * @param document
	 * @return
	 */
	private int getNumRange(Document document) {
		Elements elements = document.getElementsByClass("pagerange");
		if (elements == null || elements.size() == 0) {
			return 0;
		}
		Element element = elements.get(0);
		// Inner string: Result 1 &ndash; 20 of 5,394
		String innerStr = element.html();
		String rangeStr = innerStr.substring(innerStr.indexOf("of") + 3, innerStr.length()).replace(",", "");
		int range = Integer.parseInt(rangeStr);
		return range;
	}

	/**
	 * 根据分页获取.enw文件和全文链接
	 * 
	 * @param page
	 * 
	 * @return false:终止获取过程
	 */
	private boolean getEndnoteFilesByPage(int page, Map<String, Article> filePathMap, Map<String, String> fullTextURLMap,
			BasicSearchConfig searchConfig) {
		// 每页结果的论文主页地址
		List<String> citationURLList = new ArrayList<>();
		String searchResultContentHTML = getSearchResultPage(page, searchConfig);
		if (searchResultContentHTML == null || "".equals(searchResultContentHTML)) {
			return false;
		}
		Document document = Jsoup.parse(searchResultContentHTML);
		this.getCitationURL(citationURLList, document);
		for (String urlPath : citationURLList) {
			if (!addFileAndURLFromCitationURL(urlPath, filePathMap, fullTextURLMap)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 访问论文主页并从主页获取信息，下载.enw文件。
	 * 
	 * @param urlPath
	 * @return 如果该论文不存在，就返回true
	 */
	private boolean addFileAndURLFromCitationURL(String urlPath, Map<String, Article> filePathMap, Map<String, String> fullTextURLMap) {
		URI uri = null;
		URIBuilder builder = new URIBuilder();
		String realPath = urlPath.substring(0, urlPath.indexOf("?"));
		String idParam = NetTool.getUrlParameter(urlPath, "id");
		String CFIDParam = NetTool.getUrlParameter(urlPath, "CFID");
		String CFTOKENParam = NetTool.getUrlParameter(urlPath, "CFTOKEN");
		builder.setScheme(acmConfigMap.get("scheme")).setHost(acmConfigMap.get("host")).setPath("/" + realPath).setParameter("id", idParam)
				.setParameter("CFID", CFIDParam).setParameter("CFTOKEN", CFTOKENParam);
		try {
			uri = builder.build();
			try {
				Thread.sleep((int) Math.random() * 3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String citationHTML = HttpClientTool.getContentStringByUri(uri);
			Article article = new Article();

			Document document = Jsoup.parse(citationHTML);

			String doi = document.getElementsByAttributeValue("name", acmConfigMap.get("doiAttrName")).attr("content");

			if (doi == null || "".equals(doi)) {
				// 跳过没有doi的论文
				return true;
			}

			String title = document.getElementsByAttributeValue("name", acmConfigMap.get("titleAttrName")).attr("content");
			String publishDate = document.getElementsByAttributeValue("name", acmConfigMap.get("dateAttrName")).attr("content");
			String pdfURL = document.getElementsByAttributeValue("name", acmConfigMap.get("ftAttrName")).attr("content");
			String authors = document.getElementsByAttributeValue("name", acmConfigMap.get("authorAttrName")).attr("content");
			String abstractHtmlURL = document.getElementsByAttributeValue("name", acmConfigMap.get("abstractAttrName")).attr("content");
			String parentId = getFatherID(abstractHtmlURL);
			String id = idParam;

			URI endnoteURI = null;
			// No need of CFs
			endnoteURI = new URIBuilder().setScheme(acmConfigMap.get("scheme")).setHost(acmConfigMap.get("host"))
					.setPath(acmConfigMap.get("exportPath")).setParameter("id", id).setParameter("parent_id", parentId)
					.setParameter("expformat", "endnotes").build();
			PropertiesTool pt = new PropertiesTool("config/configuration");
			String relativePath = "cache/" + MD5.Md5_32(doi) + ".enw";
			String filePath = pt.getValue("classpath") + relativePath;

			logger.info("Downloading endnote export file: \"" + title + "\", file path: " + filePath);
			// 在保存之前判断是否数据库已经存在这篇论文
			if (!articleDao.isArticleAlreadyExist(doi)) {
				// 保存到数据库里，下载enw文件
				try {
					Thread.sleep((int) Math.random() * 3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				HttpClientTool.saveContentOfUri(endnoteURI, filePath);
				logger.info("Downloaded endnote export file: \"" + title + "\", file path: " + filePath);
				fullTextURLMap.put(doi, pdfURL);

				article.setDoi(doi);
				article.setAuthors(authors);
				article.setImportMark("1");
				article.setSource("ACM");
				article.setImportTime(new Timestamp(new Date().getTime()));
				article.setPublicationDate(publishDate);
				article.setTitle(title);
				article.setPdfURL(pdfURL);
				article.setEnwLocation(relativePath);

				articleDao.saveArticle(article);
				logger.info("Save " + title + " success.");
				filePathMap.put(doi, article);
			} else {
				// 该论文已经被保存
				logger.info("Article " + title + " already exists.");
				return false;
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private String getFatherID(String absURL) {
		String later = NetTool.getUrlParameter(absURL, "id");
		return later.substring(0, later.indexOf('.'));
	}


	// private String getID(String doi) {
	// String later = doi.split("/")[1];
	// return later.substring(later.indexOf(".") + 1, later.length());
	// }

	@Test
	public void testExp() {
		Map<String, Article> articleMap = new HashMap<>();
		Article article1 = new Article();
		article1.setEnwLocation("cache/8142E7CCC70B6955BCB0330DB6F95D8F.enw");
		Article article2 = new Article();
		article2.setEnwLocation("cache/818CE9D118E45EDB745388EFAF1D9EFC.enw");

		articleMap.put("123", article1);
		articleMap.put("1233", article2);
		exportToEndnote(articleMap);

	}
}
