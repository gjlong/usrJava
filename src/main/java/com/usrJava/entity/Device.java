package com.usrJava.entity;

/**
 * @Title: Device.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2020年05月06日 14:58
 */
public class Device {
    private String id;
    private String devid;//设备的SN
    private String name;//设备名称
    private int onlineStatus;//离线状态 0:离线 1:在线
    private String latitude;//纬度
    private String longitude;//经度
    private String gpsString;//gps坐标对应的地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGpsString() {
        return gpsString;
    }

    public void setGpsString(String gpsString) {
        this.gpsString = gpsString;
    }
}
