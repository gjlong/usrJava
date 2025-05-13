package com.usrJava;

import com.aliyun.ocr_api20210707.Client;
import com.aliyun.ocr_api20210707.models.*;
import com.aliyun.tea.*;
import com.aliyun.teaopenapi.models.Config;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.testJavaseMybatis.service.UrlsService;
import com.testJavaseMybatis.service.impl.UrlsServiceImpl;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Pattern;

/**
 * *@ClassName: TestLinuxProject
 * *@Package com.usrJava
 * *@Description: 添加描述
 * *@author Harry
 * *@date 2024年03月19日 上午11:44
 **/
public class TestLinuxProject {
    public static Logger log = LoggerFactory.getLogger(TestLinuxProject.class);
    private UrlsService urlsService = new UrlsServiceImpl();

    public void testLog(String logContent) {
        log.debug("测试log debug");
        log.info("测试log info");
        log.error("测试log error");
        log.info(logContent);
        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);
        String hostname = "http://localhost:8080";
        HttpHost localhost = new HttpHost(hostname, 80);
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);
        CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(cm).setUserAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0").build();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        HttpPost httpPost = new HttpPost("");
        httpPost.setConfig(requestConfig);
        HttpClientContext httpClientContext = HttpClientContext.create();
        try {
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost, httpClientContext);
            Document document = Jsoup.parse(EntityUtils.toString(closeableHttpResponse.getEntity(), "gb2312"));
            System.out.println(document);
            log.info("document");
            Elements bodyElements=document.getElementsByAttribute("body");
            Elements elements = document.getElementsByClass("wrapCon").first().getElementsByClass("rightFirNews");
            //右侧新闻
            for (Element t : elements) {
                for (Element temp : t.select("a")) {
                    System.out.println(temp.attr("href"));
                    System.out.println(temp.text());
                    log.info(temp.text());
                }
            }
            log.info("bodyElements",bodyElements);
            Map<String, String> contentMap = new HashMap<>();
            Iterator<Map.Entry<String, String>> entries = contentMap.entrySet().iterator();
            

        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    


    public static String formattedEnglishDate(String dateStr) {
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive() // 忽略大小写
                .appendPattern("dd-")
                .appendText(java.time.temporal.ChronoField.MONTH_OF_YEAR, TextStyle.SHORT)
                .appendPattern("-yyyy")
                .toFormatter(Locale.ENGLISH);
        try {
            // 解析输入字符串为 LocalDate
            LocalDate date = LocalDate.parse(dateStr, inputFormatter);
            // 定义输出格式
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            // 格式化为目标格式
            String formattedDate = date.format(outputFormatter);
            return formattedDate;
        } catch (DateTimeParseException e) {
            return dateStr;
        }
    }

    public static void main(String[] args) {
        TestLinuxProject testLinuxProject=new TestLinuxProject();
        testLinuxProject.formattedEnglishDate("1/6/2024");
    }

}
