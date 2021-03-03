package com.usrJava.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.usrJava.entity.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: UsrAPIUtil.java
 * @Package com.usrJava.util
 * @Description: 添加描述
 * @author gjlong
 * @date 2021年01月12日 18:38
 */
@Component("UsrAPIUtil")
public class UsrAPIUtil {
    public static Logger log = LoggerFactory.getLogger(UsrAPIUtil.class);

    public final static String ACCESS_TOKEN = "https://api.mp.usr.cn/usrCloud/user/login";//用户登录认证
    public final static String GET_USER_DEVICES_URL="https://api.mp.usr.cn/usrCloud/vn/dev/getDevsForVn";//获取某个用户的设备列表
    public final static String GET_DEVICE_URL="https://api.mp.usr.cn/usrCloud/dev/getDevice";//获取设备详情
    public final static String GET_DATAS_URL="https://api.mp.usr.cn/usrCloud/datadic/getDatas";//获取变量列表
    public final static String GET_DATA_POINT_INFO="https://api.mp.usr.cn/usrCloud/datadic/getDataPointInfoByDevice";//根据设备获取数据点信息
    public final static String GET_DEVICE_DATA_POINT_HISTORY="https://api.mp.usr.cn/usrCloud/dev/getDeviceDataPointHistory";//获取设备数据点历史记录
    public final static String GET_DEVICES_URL="https://openapi.mp.usr.cn/usrCloud/dev/getDevs";//根据条件获取设备列表
    public final static String GET_PROJECT_LIST_URL="https://openapi.mp.usr.cn/usrCloud/projectInfo/queryProjectList";//获取项目列表
    public final static String GET_ALARM_HISTORY_URL="https://api.mp.usr.cn/usrCloud/trigger/getAlarmHistory";//获取报警记录，非官方公开的接口
    public final static String EDIT_ALARM_INFO_URL="https://api.mp.usr.cn/usrCloud/trigger/editAlarmInfo";//处理报警记录，非官方公开的接口
    public final static String GET_ALARM_HISTORY_STATUS_COUNT_URL="https://api.mp.usr.cn/usrCloud/trigger/getAlarmHistoryStatusCount";//获取报警记录状态数，非官方公开的接口
    public final static String GET_DEVICE_TAGS_URL="https://api.mp.usr.cn/usrCloud/vn/dev/tag/getTags";//获取某个用户下所有标签分类信息，包括每个标签下面的设备数，非官方公开的接口


    /**
     * 获取token
     * @param account
     * @param password
     */
    public static void getAccessToken(String account,String password) {
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("account",account);
        paramMap.put("password",DigestUtils.md5DigestAsHex(password.getBytes()));
        Gson gson=new Gson();
        JSONObject jsonObject = httpsRequest(ACCESS_TOKEN, "POST", gson.toJson(paramMap));
        String resultJsonString=jsonObject.get("data").toString();
        JsonObject userDataJsonObject=JsonParser.parseString(resultJsonString).getAsJsonObject();
        AccessToken.INSTANCE.setToken(userDataJsonObject.get("token").getAsString());
        AccessToken.INSTANCE.setUid(userDataJsonObject.get("uid").getAsString());
    }

    /**
     * 根据用户id获取该用户下的设备列表
     * @param uid
     * @return
     */
    public static List<Device> getDevicesByUserId(String uid){
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("token",AccessToken.INSTANCE.getToken());
        paramMap.put("uid",uid);
        Gson gson=new Gson();
        JSONObject jsonObject = httpsRequest(GET_USER_DEVICES_URL, "POST", gson.toJson(paramMap));
        String jsonObjectString=jsonObject.toString();
        JsonElement jsonElement=JsonParser.parseString(jsonObjectString).getAsJsonObject().get("data").getAsJsonObject().get("devices");
        List<Device> deviceList=gson.fromJson(jsonElement,new TypeToken<List<Device>>(){}.getType());
        return deviceList;
    }

    /**
     * 获取某个项目id
     */
    public static int getProjectIdByProjectName(String projectName){
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("token",AccessToken.INSTANCE.getToken());
        paramMap.put("projectName",projectName);
        Gson gson=new Gson();
        JSONObject jsonObject = httpsRequest(GET_PROJECT_LIST_URL, "POST", gson.toJson(paramMap));
        String jsonObjectString=jsonObject.toString();
        int projectId=JsonParser.parseString(jsonObjectString).getAsJsonObject().get("data").getAsJsonObject().get("list").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsInt();
        return projectId;
    }

    /**
     * 根据设备状态查找设备总数
     * @param projectId
     * @param searchByDeviceStatus
     * @return
     */
    public static int getDevicesCountByCondition(int projectId,Integer searchByDeviceStatus){
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("token",AccessToken.INSTANCE.getToken());
        paramMap.put("projectId",projectId);
        paramMap.put("searchByDeviceStatus",searchByDeviceStatus);//0为离线,1为在线，5为数据点报警
        Gson gson=new Gson();
        JSONObject jsonObject = httpsRequest(GET_DEVICES_URL, "POST", gson.toJson(paramMap));
        String jsonObjectString=jsonObject.toString();
        int devicesCount=JsonParser.parseString(jsonObjectString).getAsJsonObject().get("data").getAsJsonObject().get("total").getAsInt();
        return devicesCount;

    }

    /**
     * 根据条件查找设备列表
     * @param projectId
     */
    public static List<Device> getDevicesByCondition(int projectId,String deviceName,Integer searchByDeviceStatus){
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("token",AccessToken.INSTANCE.getToken());
        paramMap.put("projectId",projectId);
        paramMap.put("search_param",deviceName);//设备名
        paramMap.put("searchByDeviceStatus",searchByDeviceStatus);//0为离线,1为在线，5为数据点报警
        Gson gson=new Gson();
        JSONObject jsonObject = httpsRequest(GET_DEVICES_URL, "POST", gson.toJson(paramMap));
        String jsonObjectString=jsonObject.toString();
        JsonElement jsonElement=JsonParser.parseString(jsonObjectString).getAsJsonObject().get("data").getAsJsonObject().get("dev");
        List<Device> deviceList=gson.fromJson(jsonElement,new TypeToken<List<Device>>(){}.getType());
        return deviceList;

    }

    /**
     * 获取设备基本信息
     */
    public static DeviceDetailDataResult getDeviceById(String deviceId){
        Map<String,String> paramMap=new HashMap<>();
        paramMap.put("token",AccessToken.INSTANCE.getToken());
        paramMap.put("deviceId",deviceId);
        Gson gson=new Gson();
        JSONObject jsonObject = httpsRequest(GET_DEVICE_URL, "POST", gson.toJson(paramMap));
        String jsonObjectString=jsonObject.toString();
        JsonElement jsonElement=JsonParser.parseString(jsonObjectString).getAsJsonObject().get("data").getAsJsonObject().get("device");
        DeviceDetailDataResult deviceDetailDataResult=gson.fromJson(jsonElement,new TypeToken<DeviceDetailDataResult>(){}.getType());
        return deviceDetailDataResult;

    }

    /**
     * 获取设备的传感器列表
     */
    public static List<Slave> getDevicePoints(String uid, String[] deviceIdArray){
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("token",AccessToken.INSTANCE.getToken());
        paramMap.put("uidForNewPermission",uid);//目标所属用户id
        paramMap.put("deviceIds",deviceIdArray);

        Gson gson=new Gson();
        JSONObject jsonObject = httpsRequest(GET_DATA_POINT_INFO, "POST", gson.toJson(paramMap));
        String jsonObjectString=jsonObject.toString();
        JsonElement slaveListJsonElement=JsonParser.parseString(jsonObjectString).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject().get("slaves");
        List<Slave> slaveList=gson.fromJson(slaveListJsonElement,new TypeToken<List<Slave>>(){}.getType());
        return slaveList;


    }

    /**
     * 获取设备传感器历史记录
     * @param deviceId
     * @param slaveIndex
     * @param dataPointId
     * @param itemId
     * @param pageNo
     * @param pageSize
     * @param timeSort
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<BaseDataResult> getDevicePointHistory(String deviceId,String slaveIndex,String dataPointId,String itemId,int pageNo,int pageSize,String timeSort,String startDate,String endDate){


        Map<String,Object> paramMap=new HashMap<>();
        JsonObject devDatapointsJsonObject=new JsonObject();
        devDatapointsJsonObject.addProperty("deviceNo",deviceId);
        devDatapointsJsonObject.addProperty("slaveIndex",slaveIndex);
        devDatapointsJsonObject.addProperty("dataPointId",dataPointId);
        devDatapointsJsonObject.addProperty("itemId",itemId);

        JsonArray devDatapointsJsonArray=new JsonArray();
        devDatapointsJsonArray.add(devDatapointsJsonObject);
        paramMap.put("devDatapoints",devDatapointsJsonArray);

        paramMap.put("pageNo",pageNo);
        paramMap.put("pageSize",pageSize);//单次返回最高5000条记录
        paramMap.put("timeSort",timeSort);//倒序查询 "desc"
        try {
            paramMap.put("start",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate).getTime());
            paramMap.put("end",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Gson gson=new Gson();
        JSONObject resultJsonObject = httpsRequest(GET_DEVICE_DATA_POINT_HISTORY, "POST", gson.toJson(paramMap),AccessToken.INSTANCE.getToken());
        String resultJsonObjectString=resultJsonObject.toString();

        JsonElement devicePointHistoryJsonElement=JsonParser.parseString(resultJsonObjectString).getAsJsonObject().get("data").getAsJsonObject().get("list").getAsJsonArray().get(0).getAsJsonObject().get("list").getAsJsonArray();
        List<BaseDataResult> devicePointHistoryList=gson.fromJson(devicePointHistoryJsonElement,new TypeToken<List<BaseDataResult>>(){}.getType());
       return devicePointHistoryList;
    }

    /**
     * 报警历史记录
     * @param projectId
     * @param offset
     * @param limit
     * @param startDate
     * @param endDate
     * @return
     */
    public static Map<String,Object> getDeviceAlarmHistory(int projectId,Integer offset,Integer limit,String startDate,String endDate){
        JsonObject devDatapointsJsonObject=new JsonObject();
        devDatapointsJsonObject.add("dataId",null);
        devDatapointsJsonObject.add("did",null);//设备SN
        devDatapointsJsonObject.addProperty("itemId","");//暂时没用，传递一个空字符串
        devDatapointsJsonObject.addProperty("offset",offset);//0 从第几条数据开始查询。从0开始，第一页为0，第二页为limit*(2-1)=20。第n页为limit*(n-1)
        devDatapointsJsonObject.addProperty("limit",limit);//20
        devDatapointsJsonObject.addProperty("projectId",projectId);
        devDatapointsJsonObject.add("slaveIndex",null);

        try {
            if(startDate!=null) {
                devDatapointsJsonObject.addProperty("timeStart", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate).getTime() / 1000);//时间戳
            }
            if(endDate!=null) {
                devDatapointsJsonObject.addProperty("timeEnd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate).getTime() / 1000);//时间戳
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Gson gson=new Gson();
        JSONObject resultJsonObject = httpsRequest(GET_ALARM_HISTORY_URL, "POST", gson.toJson(devDatapointsJsonObject),AccessToken.INSTANCE.getToken());
        String resultJsonObjectString=resultJsonObject.toString();
        JsonObject dataResultJsonObject=JsonParser.parseString(resultJsonObjectString).getAsJsonObject().get("data").getAsJsonObject();
        JsonElement alarmHistoryListJsonElement=dataResultJsonObject.get("alarmHistorys");
        int total=dataResultJsonObject.get("total").getAsInt();
        List<AlarmHistoryDataResult> alarmHistoryList=gson.fromJson(alarmHistoryListJsonElement,new TypeToken<List<AlarmHistoryDataResult>>(){}.getType());
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("total",total);
        resultMap.put("alarmHistoryList",alarmHistoryList);
        return resultMap;
    }

    /**
     * 处理未读的报警信息
     * @param historyId
     */
    public static void processDeviceAlarmHistory(int historyId){
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("hid",historyId);//报警信息id
        jsonObject.addProperty("status",2);//0为未处理，1为误报，2为已处理
        jsonObject.addProperty("description","已处理当前报警");
        Gson gson=new Gson();
        JSONObject resultJsonObject = httpsRequest(EDIT_ALARM_INFO_URL, "POST", gson.toJson(jsonObject),AccessToken.INSTANCE.getToken());
        String resultJsonObjectString=resultJsonObject.toString();
        //System.out.println(resultJsonObjectString);
    }

    /**
     * 获取未处理和已处理的报警记录数
     * @param projectId
     * @param startDate
     * @param endDate
     * @return
     */
    public static Map<String,Integer> getAlarmHistoryStatusCountUrl(int projectId,String startDate,String endDate){
        JsonObject alarmHistoryStatusConditionJsonObject=new JsonObject();
        alarmHistoryStatusConditionJsonObject.addProperty("projectId",projectId);//项目id
        alarmHistoryStatusConditionJsonObject.addProperty("suid",AccessToken.INSTANCE.getUid());//用户id

        try {
            if(startDate!=null) {
                alarmHistoryStatusConditionJsonObject.addProperty("timeStart", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate).getTime() / 1000);//1588059614L
                alarmHistoryStatusConditionJsonObject.addProperty("timeStartUnixTimeStamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate).getTime());//1588059614000L
            }
            if(endDate!=null) {
                alarmHistoryStatusConditionJsonObject.addProperty("timeEnd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate).getTime() / 1000);//1590651614L
                alarmHistoryStatusConditionJsonObject.addProperty("timeEndUnixTimeStamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate).getTime());//1590651614000L
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Gson gson=new Gson();
        JSONObject resultJsonObject = httpsRequest(GET_ALARM_HISTORY_STATUS_COUNT_URL, "POST", gson.toJson(alarmHistoryStatusConditionJsonObject),AccessToken.INSTANCE.getToken());
        String resultJsonObjectString=resultJsonObject.toString();
        JsonObject dataResultJsonObject=JsonParser.parseString(resultJsonObjectString).getAsJsonObject().get("data").getAsJsonObject();
        int processedAlarmCount=dataResultJsonObject.get("processedCount").getAsInt();
        int notProcessedAlarmCount=dataResultJsonObject.get("noProcessedCount").getAsInt();
        Map<String,Integer> resultMap=new HashMap<>();
        resultMap.put("processedAlarmCount",processedAlarmCount);
        resultMap.put("notProcessedAlarmCount",notProcessedAlarmCount);
        return resultMap;
    }

    /**
     * 根据tags查找设备数量
     * @return
     */
    public static List<DeviceTagResult> getDevicesCountByTags(){
        JsonObject selectTagsConditionJsonObject=new JsonObject();
        selectTagsConditionJsonObject.addProperty("uid",AccessToken.INSTANCE.getUid());//用户id

        Gson gson=new Gson();
        JSONObject resultJsonObject = httpsRequest(GET_DEVICE_TAGS_URL, "POST", gson.toJson(selectTagsConditionJsonObject),AccessToken.INSTANCE.getToken());
        String resultJsonObjectString=resultJsonObject.toString();
        JsonObject dataResultJsonObject=JsonParser.parseString(resultJsonObjectString).getAsJsonObject().get("data").getAsJsonObject();
        JsonElement deviceTagListJsonElement=dataResultJsonObject.get("list");
        List<DeviceTagResult> deviceTagResultList=gson.fromJson(deviceTagListJsonElement,new TypeToken<List<DeviceTagResult>>(){}.getType());
        return deviceTagResultList;
    }

    /**
     * GPS定位数据转换
     * @param gpsValue
     * @return
     */
    public static String getGPSToStringByInteger(Integer gpsValue){
        String hexString=Integer.toHexString(gpsValue);//转换成16进制数据,例如将十进制值1122110100转换成16进制值
        Float gpsFloatValue=Float.intBitsToFloat(Integer.valueOf(hexString.trim(),16));//将16进制转换成float类型
        String gpsStringValue=(String.format("%.6f",gpsFloatValue));//将float类型转换成字符串，精确到小数点后六位
        return gpsStringValue;
    }

    /**
     * 定时获取access_token
     */
    @Scheduled(fixedDelay = (2*3600-200)*1000,initialDelay = 1000)//启动之后一秒执行，每7000秒刷新一次
    public static void getToken() {
        UsrAPIUtil.getAccessToken("account","password");
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl,
                                          String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
                    .openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestProperty("Content-Type","application/json");

            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:", e);
        }
        return jsonObject;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @param token 要提交的token，用来设置header
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl,
                                          String requestMethod, String outputStr,String token) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
                    .openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);


            httpUrlConn.setRequestProperty("Content-Type","application/json");
            httpUrlConn.setRequestProperty("token",token);

            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:", e);
        }
        return jsonObject;

    }
}
