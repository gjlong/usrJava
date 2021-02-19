package com.usrJava.entity;

/**
 * @Title: DeviceDetailDataResult.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2020年05月26日 17:39
 */
public class DeviceDetailDataResult {
    private String deviceId;//设备的sn
    private String address;//地址
    private String groupId;//分组id
    private int onlineStatus;//离线状态 0:离线  1:在线
    private DeviceStatus deviceStatus;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 由于平台接口返回的数据存在一个设备状态的json数据结构，所以创建一个内部类来对应该结构
     */
    public class DeviceStatus{
        private int datapointAlarm;//数据点报警  0：不报警  1：报警中

        public int getDatapointAlarm() {
            return datapointAlarm;
        }

        public void setDatapointAlarm(int datapointAlarm) {
            this.datapointAlarm = datapointAlarm;
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public DeviceStatus getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(DeviceStatus deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
}
