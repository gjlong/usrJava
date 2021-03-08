package com.usrJava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gjlong
 * @Title: UsrTestUtil.java
 * @Package com.usrJava
 * @Description: 添加描述
 * @date 2021年03月06日 上午9:30
 */
public class UsrTestUtil {
    public static Logger log = LoggerFactory.getLogger(UsrTestUtil.class);
    public void testLog(){
        log.debug("测试log debug");
    }
}
