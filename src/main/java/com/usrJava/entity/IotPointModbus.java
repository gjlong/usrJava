package com.usrJava.entity;

/**
 * @Title: IotPointModbus.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2020年05月16日 16:42
 */
public class IotPointModbus {
    private int dataid;
    private String itemId;
    private int writeRead;
    private int relId;

    public int getDataid() {
        return dataid;
    }

    public void setDataid(int dataid) {
        this.dataid = dataid;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getWriteRead() {
        return writeRead;
    }

    public void setWriteRead(int writeRead) {
        this.writeRead = writeRead;
    }

    public int getRelId() {
        return relId;
    }

    public void setRelId(int relId) {
        this.relId = relId;
    }
}
