package com.usrJava.entity;

/**
 * @Title: AlarmHistoryDataResult.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2020年05月29日 9:50
 */
public class AlarmHistoryDataResult {
    private int id;
    private String deviceId;
    private String deviceName;//设备名称
    private String slaveName;//从机名称
    private float alarmValue;//当前报警值 0和1 本来变量类型是int，由于接口返回的数据格式变成0.0，所以更改为float
    private String content;//报警内容
    private int alarmState;//报警状态 0为恢复正常，1为报警
    private int status;//处理状态 0为未处理，2为已处理
    private int alarmTime;//报警时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public void setSlaveName(String slaveName) {
        this.slaveName = slaveName;
    }

    public float getAlarmValue() {
        return alarmValue;
    }

    public void setAlarmValue(float alarmValue) {
        this.alarmValue = alarmValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;
    }
}
