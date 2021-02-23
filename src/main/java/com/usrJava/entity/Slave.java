package com.usrJava.entity;

/**
 * @Title: Slave.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2020年05月15日 16:29
 */
import java.util.List;

public class Slave {
    private int id;
    private String userId;
    private int pageNo;
    private int pageSize;
    private int deviceTemplateId;
    private String slaveIndex;
    private String slaveName;
    private int slaveAddr;//从机地址
    private String comIdx;//从机序号
    private int relProtocolId;
    private int weight;
    private String createDt;//从机创建时间
    private String updateDt;//从机数据更新时间
    private List<IotPoint> iotDataDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getDeviceTemplateId() {
        return deviceTemplateId;
    }

    public void setDeviceTemplateId(int deviceTemplateId) {
        this.deviceTemplateId = deviceTemplateId;
    }

    public String getSlaveIndex() {
        return slaveIndex;
    }

    public void setSlaveIndex(String slaveIndex) {
        this.slaveIndex = slaveIndex;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public void setSlaveName(String slaveName) {
        this.slaveName = slaveName;
    }

    public int getSlaveAddr() {
        return slaveAddr;
    }

    public void setSlaveAddr(int slaveAddr) {
        this.slaveAddr = slaveAddr;
    }

    public String getComIdx() {
        return comIdx;
    }

    public void setComIdx(String comIdx) {
        this.comIdx = comIdx;
    }

    public int getRelProtocolId() {
        return relProtocolId;
    }

    public void setRelProtocolId(int relProtocolId) {
        this.relProtocolId = relProtocolId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }

    public List<IotPoint> getIotDataDescription() {
        return iotDataDescription;
    }

    public void setIotDataDescription(List<IotPoint> iotDataDescription) {
        this.iotDataDescription = iotDataDescription;
    }
}
