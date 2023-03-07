package com.usrJava;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.testJavaseMybatis.service.UrlsService;
import com.testJavaseMybatis.service.impl.UrlsServiceImpl;
import com.testJavaseMybatis.urls.model.Urls;
import org.apache.commons.lang.StringUtils;
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
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author gjlong
 * @Title: UsrTestUtil.java
 * @Package com.usrJava
 * @Description: 添加描述
 * @date 2021年03月06日 上午9:30
 */

public class UsrTestUtil {
    public static Logger log = LoggerFactory.getLogger(UsrTestUtil.class);
    private UrlsService urlsService = new UrlsServiceImpl();

    public void testLog(String logContent) {
        log.debug("测试log debug");
        log.info("测试log info");
        log.error("测试log error");
        log.info(logContent);
    }

    public class GetThread extends Thread {
        private final CloseableHttpClient closeableHttpClient;
        private final HttpContext httpContext;
        private final HttpGet httpGet;
        private BlockingQueue<String> blockingQueue;
        private Map<String, Integer> urlMap;

        public GetThread(CloseableHttpClient closeableHttpClient, HttpGet httpGet, BlockingQueue<String> blockingQueue, Map<String, Integer> urlMap) {
            this.closeableHttpClient = closeableHttpClient;
            this.httpContext = HttpClientContext.create();
            this.httpGet = httpGet;
            this.blockingQueue = blockingQueue;
            this.urlMap = urlMap;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("线程id：" + Thread.currentThread().getName());
                    System.out.println("当前队列大小：" + blockingQueue.size());
                    System.out.println("当前map集合大小：" + urlMap.size());
                    //获取队列的url
                    String url = blockingQueue.take();
                    System.out.println("当前执行的url：" + url);
                    url = StringUtils.remove(url, "#");
                    if (url.isEmpty() == false && StringUtils.contains(url, "mailto:") == false) {
                        HttpGet httpGet = new HttpGet(url);
                        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(5000).build();
                        httpGet.setConfig(requestConfig);
                        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet, httpContext);
                        Document document = null;
                        try {
                            HttpEntity httpEntity = closeableHttpResponse.getEntity();
                            //System.out.println("url is " + httpGet.getURI());

                            document = Jsoup.parse(EntityUtils.toString(httpEntity, "gb2312"), url);
                            //System.out.println("文档内容："+document);
                            Elements elements = document.getElementsByAttribute("href");
                            String tempUrl;
                            for (Element t : elements) {
                                //System.out.println(t.attr("abs:href"));
                                //判断该url是否已经记录
                                tempUrl = t.attr("abs:href");
                                if (urlMap.containsKey(tempUrl) == false) {
                                    urlMap.put(tempUrl, 1);
                                    blockingQueue.put(tempUrl);
                                }

                            }

                            Urls urls = new Urls();
                            urls.setUrl(url);
                            urls.setDatetime(new Date());
                            if (urlsService.deleteSampleUrls(url) == false) {
                                urlsService.addUrlsBySingleSession(urls);
                            }

                            //保存document
                            /*MessageDigest md = MessageDigest.getInstance("MD5");
                            md.update(document.title().getBytes("UTF-8"));
                            byte[] result = md.digest();


                            FileOutputStream fos=new FileOutputStream(""+new String(document.title().getBytes("UTF-8"))+".xml",false);
                            OutputStreamWriter osw=new OutputStreamWriter(fos,"utf-8");
                            osw.write(document.html());
                            osw.close();*/
                        } finally {
                            closeableHttpResponse.close();
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void getHuanQiuNewsList() {
        String url = "https://www.huanqiu.com/";
        String hostname = url;
        Map<String, String> contentMap = new HashMap<>();
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
                    contentMap.put(temp.attr("href"), temp.text());
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
                    contentMap.put(temp.attr("href"), temp.text());
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
                    contentMap.put("https://opinion.huanqiu.com/article/" + e.getAsJsonObject().get("aid").getAsString(), e.getAsJsonObject().get("title").getAsString());
                }
            }

            //泛型迭代器遍历数据
            Iterator<Map.Entry<String, String>> entries = contentMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entryTemp = entries.next();
                System.out.println(entryTemp.getKey());
                System.out.println(entryTemp.getValue());
            }

            //科技板块
            System.out.println("科技模块信息");
            String techUrl = "https://tech.huanqiu.com/api/list2?node=/e3pmh164r/e3pmtm015&offset=0&limit=20";
            String techJsonString = EntityUtils.toString(closeableHttpClient.execute(new HttpGet(techUrl)).getEntity(), "gb2312");
            JsonArray techJsonArray = JsonParser.parseString(techJsonString).getAsJsonObject().get("list").getAsJsonArray();
            System.out.println(techJsonArray.size());
            for (JsonElement e : techJsonArray) {
                if (e.getAsJsonObject().get("aid") != null) {
                    System.out.println("https://opinion.huanqiu.com/article/" + e.getAsJsonObject().get("aid").getAsString());
                    System.out.println(e.getAsJsonObject().get("title"));
                    System.out.println(e.getAsJsonObject().get("summary"));
                    contentMap.put("https://opinion.huanqiu.com/article/" + e.getAsJsonObject().get("aid").getAsString(), e.getAsJsonObject().get("title").getAsString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getWebHtml() {

        try {
            String url = "https://www.huanqiu.com/";
            String hostname = url;
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


            BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
            blockingQueue.put("https://www.huanqiu.com/");
            //创建hashmap记录url地址访问状态
            Map<String, Integer> urlMap = Collections.synchronizedMap(new HashMap<String, Integer>());

            //System.out.println(blockingQueue.take());

            GetThread[] getThreads = new GetThread[20];
            for (int threadNum = 0; threadNum < getThreads.length; threadNum++) {
                httpGet = new HttpGet();
                httpGet.setConfig(requestConfig);
                getThreads[threadNum] = new GetThread(closeableHttpClient, httpGet, blockingQueue, urlMap);
            }
            for (int startThreadNum = 0; startThreadNum < getThreads.length; startThreadNum++) {
                getThreads[startThreadNum].start();
            }
            for (int joinThreadNum = 0; joinThreadNum < getThreads.length; joinThreadNum++) {
                getThreads[joinThreadNum].join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    //获取新版微博
    public void getNewWeibo() {
        try {
            String url = "https://weibo.com/ajax/side/hotSearch";//直接获取热搜接口的数据
            String hostname = url;
            Map<String, String> contentMap = new HashMap<>();
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

            //发起请求，获取页面数据
            String opinionJsonString = EntityUtils.toString(closeableHttpClient.execute(new HttpGet(url)).getEntity(), "gb2312");
            System.out.println(opinionJsonString);
            String hotgovJsonElement = JsonParser.parseString(opinionJsonString).getAsJsonObject().get("data").getAsJsonObject().get("hotgov").getAsString();
            System.out.println(hotgovJsonElement);
            JsonArray realtimeJsonArray = JsonParser.parseString(opinionJsonString).getAsJsonObject().get("data").getAsJsonObject().get("realtime").getAsJsonArray();
            //推送消息
            String tempRealtimeString;
            List<String> tempRealtimeStringList = new ArrayList();//创建一个list
            for (int realtimeJsonArrayIndex = 0; realtimeJsonArrayIndex < realtimeJsonArray.size(); realtimeJsonArrayIndex++) {
                tempRealtimeString = realtimeJsonArray.get(realtimeJsonArrayIndex).getAsString();
                System.out.println(tempRealtimeString);
                //时间加上消息的格式
                tempRealtimeStringList.add(tempRealtimeString);
            }
            System.out.println(tempRealtimeStringList.size());

            //模拟浏览器加载网页的方式解析该内容
            //改用htmlunit
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            HtmlPage htmlPage = webClient.getPage("https://weibo.com/login.php");
            webClient.waitForBackgroundJavaScript(1000);//等待异步js执行完毕，否则不会生成数据dom格式
            System.out.println(htmlPage.asXml());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //测试方法
    public void test() {
        System.out.println("run test");
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("test1");
        try {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            HtmlPage htmlPage = webClient.getPage("https://www.xiaohongshu.com/explore");
            webClient.waitForBackgroundJavaScript(1000);//等待异步js执行完毕，否则不会生成数据dom格式
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            System.out.println(htmlPage.asXml());
            //输出网页内容
            //爬取小红书
            //https://edith.xiaohongshu.com/api/sns/web/v1/homefeed




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UsrTestUtil usrTestUtil = new UsrTestUtil();
        usrTestUtil.test();

    }
}
