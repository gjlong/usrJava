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


    /**
     * 提取日期字符串内容
     */
    public void makeString(String s){

        //去掉括号内容
        while (s.contains("(") && s.contains(")")) {
            int start = s.indexOf("(");
            int end = s.indexOf(")");
            s = s.substring(0, start) + s.substring(end + 1);
        }
        System.out.println(s);
        if (s.contains(" to ")) { //假如存在to，按to分割前后日期
            //去掉From
            // 不区分大小写地移除"From"并修剪空白
            s = s.replaceAll("(?i)from", "").trim();//去掉"from"
            String[] parts = s.split("\\s+to\\s+");
            for (String part : parts) {
                System.out.println(part);
            }
        }else if(s.contains("至")){ //假如存在"至"，按“至”分割前后日期
            s = s.replaceAll("(?i)從", "").trim();//去掉"從"
            String[] parts = s.split("\\s+至\\s+");
            for (int p=0;p<parts.length;p++) {
                parts[p] = parts[p].replaceAll("(\\d{2}-[A-Z]{3}-\\d{4}).*", "$1");//去掉时分秒，只保留日期
                //System.out.println(parts[p]);
                parts[p]=formattedEnglishDate(parts[p]);
                System.out.println(parts[p]);

            }

        }

    }

    /**
     * 把国际日期格式转换成国内日期格式
     * @param dateStr
     * @return
     */
    public static String formattedChineseDate(String dateStr) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        // 解析输入字符串为 LocalDate
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);
        // 定义输出格式
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // 格式化为目标格式
        String formattedDate = date.format(outputFormatter);
        return formattedDate;
    }

    /**
     * 把英文月份的日期格式转换成普通日期格式
     * @param dateStr
     * @return
     */
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

    //添加阿里云ocr方法
    //创建接口请求客户端
    public static com.aliyun.ocr_api20210707.Client createClient() throws Exception {
        String accesskeyid="accesskeyid";
        String accesssecret="accesssecret";
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(accesskeyid)
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(accesssecret);
        config.endpoint = "ocr-api.cn-hangzhou.aliyuncs.com";
        return new com.aliyun.ocr_api20210707.Client(config);
    }

    //表格识别方法
    public void ocrInsurance() {
        final String localImageFileName = "/src/main/resources/images/example.jpg";
        try {
            Client client = createClient();
            //加载测试图片
            InputStream imageStream = new FileInputStream(localImageFileName);
            RecognizeGeneralStructureRequest recognizeGeneralStructureRequest=new RecognizeGeneralStructureRequest();
            List<String> keys=new ArrayList<>();

            keys.add("header");
            keys.add("PolicyholderName");
            keys.add("PolicyNo.");
            keys.add("PeriodofInsurance");

            recognizeGeneralStructureRequest.setBody(imageStream).setKeys(keys);
            RecognizeGeneralStructureResponse response = client.recognizeGeneralStructure(recognizeGeneralStructureRequest);
            String jsonString = new Gson().toJson(response.getBody().getData().toMap());
            System.out.println(jsonString);
            JsonObject ocrResult=JsonParser.parseString(jsonString).getAsJsonObject().get("SubImages").getAsJsonArray().get(0).getAsJsonObject().get("KvInfo").getAsJsonObject().get("Data").getAsJsonObject();
            // 遍历所有属性
            JsonObject insuranceObject=new JsonObject();

            for(int i=0;i<keys.size();i++){
                String key=keys.get(i);
                System.out.println(ocrResult.get(key));
                switch (i){
                    case 0:
                        insuranceObject.add("company",ocrResult.get(key));
                        break;
                    case 1:
                        insuranceObject.add("username",ocrResult.get(key));
                        break;
                    case 2:
                        insuranceObject.add("policyNo",ocrResult.get(key));
                        break;
                    case 3:
                        insuranceObject.add("date",ocrResult.get(key));
                        break;
                }
            }
            System.out.println(insuranceObject.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    //驾驶证识别方法
    public void ocrCar() {
        final String localImageFileName = "/src/main/resources/images/example.jpg"; // 本地图片路径
        try (InputStream imageStream = new FileInputStream(localImageFileName)) {
            RecognizeAllTextRequest request = new RecognizeAllTextRequest()
                    .setType("Advanced")        // 指定 Type（此参数为必填参数）
                    .setBody(imageStream)       // 指定本地图片路径
                    .setOutputOricoord(true);   // 设置返回原图坐标。您可以设置更多二级参数。
            // 您也可以在 request 中指定更多参数。例如对于 Type=Advanced，可以指定 OutputCharInfo=true（输出单字信息）
            RecognizeAllTextRequest.RecognizeAllTextRequestAdvancedConfig advancedConfig = new RecognizeAllTextRequest.RecognizeAllTextRequestAdvancedConfig()
                    .setIsLineLessTable(false)
                    .setOutputTable(true);
            request.setAdvancedConfig(advancedConfig);
            Client client = createClient();
            RecognizeAllTextResponse response = client.recognizeAllText(request);
            String jsonString=new Gson().toJson(response.getBody().getData().toMap());
            JsonArray jsonArray= JsonParser.parseString(jsonString).getAsJsonObject().getAsJsonArray("SubImages").get(0).getAsJsonObject().getAsJsonObject("TableInfo").getAsJsonArray("TableDetails").get(0).getAsJsonObject().get("CellDetails").getAsJsonArray();
            for(int i=0;i<jsonArray.size();i++){
                String contentString=jsonArray.get(i).getAsJsonObject().get("CellContent").getAsString();
                String index=String.valueOf(i+1);
                if(contentString.startsWith(index)==true){
                    contentString=contentString.substring(index.length());
                }
                System.out.println(contentString);
            }

        } catch (TeaException e) {
            System.out.println(e.getStatusCode());
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //自动爬取最新数据 爬取文章内容

    public static void main(String[] args) {
        TestLinuxProject testLinuxProject=new TestLinuxProject();
        testLinuxProject.formattedEnglishDate("1/6/2024");
    }

}
