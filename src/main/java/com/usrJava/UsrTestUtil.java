package com.usrJava;

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

/**
 * @author gjlong
 * @Title: UsrTestUtil.java
 * @Package com.usrJava
 * @Description: 添加描述
 * @date 2021年03月06日 上午9:30
 */
public class UsrTestUtil {
    public static Logger log = LoggerFactory.getLogger(UsrTestUtil.class);
    public void testLog(String logContent){
        log.debug("测试log debug");
        log.info("测试log info");
        log.error("测试log error");
        log.info(logContent);
    }

    public static void main(String[] args) {
        String url="https://www.huanqiu.com/";
        String hostname=url;
        try {
            //设置连接池
            PoolingHttpClientConnectionManager cm=new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(20);
            HttpHost localhost=new HttpHost(hostname,80);
            cm.setMaxPerRoute(new HttpRoute(localhost),50);
            CloseableHttpClient closeableHttpClient=HttpClients.custom().setConnectionManager(cm).setUserAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0").build();

            CloseableHttpResponse closeableHttpResponse=null;

            Document document=null;
            //获取cookies
            HttpClientContext httpClientContext=HttpClientContext.create();
            HttpGet httpGet=new HttpGet(url);
            RequestConfig requestConfig=RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpGet.setConfig(requestConfig);
            closeableHttpResponse=closeableHttpClient.execute(httpGet,httpClientContext);
            CookieStore cookieStore=httpClientContext.getCookieStore();
            List<Cookie> cookies=cookieStore.getCookies();

            System.out.println(cookies.size());
            document=Jsoup.parse(EntityUtils.toString(closeableHttpClient.execute(new HttpGet(url)).getEntity(),"gb2312"));
            Elements elements=document.getElementsByClass("wrapCon").first().getElementsByClass("rightFirNews");
            for(Element t :elements){
                //System.out.println(t);
                System.out.println(t.select("a"));
                System.out.println(t.select("a").html());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
