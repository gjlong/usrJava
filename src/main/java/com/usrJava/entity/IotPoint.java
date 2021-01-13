package com.usrJava.entity;

/**
 * @Title: IotPoint.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2020年05月15日 16:35
 */
public class IotPoint {
    private int id;
    private int weight;
    private String name;//名称
    private String formula;//公式
    private IotPointModbus iotModbusDataCmd;//cmd配置信息
    private int permissionOp;//可操作权限 0 不可操作 1 可操作 默认可见
    private int permissionShow;//可见权限 0 不可见 1 可见 默认可见
    private int store;//存储
    private int precision;//小数位数
    private int type;//类型：0：数值型 1：开关型 3：定位性 4：字符型
    private String unit;//单位
    private int valueKind;//数据类型

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public IotPointModbus getIotModbusDataCmd() {
        return iotModbusDataCmd;
    }

    public void setIotModbusDataCmd(IotPointModbus iotModbusDataCmd) {
        this.iotModbusDataCmd = iotModbusDataCmd;
    }

    public int getPermissionOp() {
        return permissionOp;
    }

    public void setPermissionOp(int permissionOp) {
        this.permissionOp = permissionOp;
    }

    public int getPermissionShow() {
        return permissionShow;
    }

    public void setPermissionShow(int permissionShow) {
        this.permissionShow = permissionShow;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getValueKind() {
        return valueKind;
    }

    public void setValueKind(int valueKind) {
        this.valueKind = valueKind;
    }
}
