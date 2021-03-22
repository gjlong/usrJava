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
    public void testLog(String logContent){
        log.debug("测试log debug");
        log.info("测试log info");
        log.error("测试log error");
        log.info(logContent);
    }

    public static void main(String[] args) {
        double y=1.0;
        for(int i=0;i<=1000;i++){
            double π=3*Math.pow(2, i)*y;
            System.out.println("第"+i+"次切割,为正"+(6+6*i)+"边形，圆周率π≈"+π);
            y=Math.sqrt(2-Math.sqrt(4-y*y));
        }
    }
}
