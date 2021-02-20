package com.usrJava.entity;

/**
 * @Title: BaseDataResult.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2020年05月16日 17:10
 */
public class BaseDataResult {
    private Long time;//时间戳
    private Integer value;//传感器值

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
