package com.usrJava.entity;

/**
 * @Title: AccessToken.java
 * @Package com.usrJava.entity
 * @Description: 添加描述
 * @author gjlong
 * @date 2021年01月11日 下午6:13
 */
public enum AccessToken {
    INSTANCE;

    private String token;//登录成功时返回的登录凭证(注意:token有效时间为2小时，两小时内可以重复使用)
    private int expiresIn;
    private String uid;//用户id


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
