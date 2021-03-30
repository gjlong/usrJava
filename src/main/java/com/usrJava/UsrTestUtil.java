package com.usrJava;

import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.cookie.Cookie;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gjlong
 * @Title: UsrTestUtil.java
 * @Package com.usrJava
 * @Description: 添加描述
 * @date 2021年03月06日 上午9:30
 */
public class UsrTestUtil {
    public static Logger log = LoggerFactory.getLogger(UsrTestUtil.class);

    public void testLog(String logContent) {
        log.debug("测试log debug");
        log.info("测试log info");
        log.error("测试log error");
        log.info(logContent);
    }

    public static void main(String[] args) {
        String url = "https://www.huanqiu.com/";
        String hostname = url;
        Map<String,String> contentMap=new HashMap<>();
        try {
            //设置连接池
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(20);
            HttpHost localhost = new HttpHost(hostname, 80);
            cm.setMaxPerRoute(new HttpRoute(localhost), 50);
            CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(cm).setUserAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0").build();

            CloseableHttpResponse closeableHttpResponse = null;

            Document document = null;
            //获取cookies
            HttpClientContext httpClientContext = HttpClientContext.create();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpGet.setConfig(requestConfig);
            closeableHttpResponse = closeableHttpClient.execute(httpGet, httpClientContext);
            CookieStore cookieStore = httpClientContext.getCookieStore();
            List<Cookie> cookies = cookieStore.getCookies();

            //System.out.println(cookies.size());
            document = Jsoup.parse(EntityUtils.toString(closeableHttpClient.execute(new HttpGet(url)).getEntity(), "gb2312"));
            Elements elements = document.getElementsByClass("wrapCon").first().getElementsByClass("rightFirNews");
            //右侧新闻
            for (Element t : elements) {
                for (Element temp : t.select("a")) {
                    System.out.println(temp.attr("href"));
                    System.out.println(temp.text());
                    contentMap.put(temp.attr("href"),temp.text());
                    //System.out.println(temp.html());
                }
            }
            System.out.println("最新评论模块信息");
            //最新评论板块
            Elements commentNewsElement = document.getElementsByClass("leftSec").first().getElementsByClass("commentDetail").first().getElementsByClass("commentNews");
            for (Element t : commentNewsElement) {
                for (Element temp : t.select("a")) {
                    System.out.println(temp.attr("href"));
                    System.out.println(temp.text());
                    contentMap.put(temp.attr("href"),temp.text());
                }
            }
            //社评集板块
            System.out.println("社评集模块信息");
            String opinionUrl = "https://opinion.huanqiu.com/api/list2?node=/e3pmub6h5/e3prafm0g&offset=0&limit=20";
            String opinionJsonString = EntityUtils.toString(closeableHttpClient.execute(new HttpGet(opinionUrl)).getEntity(), "gb2312");
            JsonArray jsonArray = JsonParser.parseString(opinionJsonString).getAsJsonObject().get("list").getAsJsonArray();
            System.out.println(jsonArray.size());
            for (JsonElement e : jsonArray) {
                if (e.getAsJsonObject().get("aid") != null) {
                    System.out.println("https://opinion.huanqiu.com/article/" + e.getAsJsonObject().get("aid").getAsString());
                    System.out.println(e.getAsJsonObject().get("title"));
                    System.out.println(e.getAsJsonObject().get("summary"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
