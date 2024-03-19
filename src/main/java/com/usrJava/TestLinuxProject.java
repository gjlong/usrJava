package com.usrJava;

import com.testJavaseMybatis.service.UrlsService;
import com.testJavaseMybatis.service.impl.UrlsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * *@ClassName: TestLinuxProject
 * *@Package com.usrJava
 * *@Description: 添加描述
 * *@author Harry
 * *@date 2024年03月19日 上午11:44
 **/
public class TestLinuxProject {
    public static Logger log = LoggerFactory.getLogger(TestLinuxProject.class);
    private UrlsService urlsService = new UrlsServiceImpl();

    public void testLog(String logContent) {
        log.debug("测试log debug");
        log.info("测试log info");
        log.error("测试log error");
        log.info(logContent);
    }
}
