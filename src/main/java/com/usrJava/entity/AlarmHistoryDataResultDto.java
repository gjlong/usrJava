package com.usrJava.entity;

/**
 * @Title: AlarmHistoryDataResultDto.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2020年07月07日 16:30
 */
public class AlarmHistoryDataResultDto extends AlarmHistoryDataResult{
    private String latitude;
    private String longitude;
    private String gpsString;

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
