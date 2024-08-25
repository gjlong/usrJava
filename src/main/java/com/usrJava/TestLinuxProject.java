package com.usrJava;

import com.google.gson.JsonObject;
import com.sun.org.apache.xml.internal.security.algorithms.SignatureAlgorithm;
import com.testJavaseMybatis.service.UrlsService;
import com.testJavaseMybatis.service.impl.UrlsServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static void main(String[] args) {
        TestLinuxProject testLinuxProject=new TestLinuxProject();
        testLinuxProject.testLog("");
    }

}
