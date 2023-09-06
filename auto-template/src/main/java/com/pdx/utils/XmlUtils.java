package com.pdx.utils;

import org.testng.TestNG;
import org.testng.xml.SuiteXmlParser;
import org.testng.xml.XmlSuite;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @Author 派同学
 * @Description 执行xml文件的工具类
 * @Date 2023/9/6
 **/
public class XmlUtils {

    public static void runXml(String param) {
        // xml文件名
        String xmlName = "testng.xml";
        // 获取xml文件流
        InputStream inputStream = XmlUtils.class.getClassLoader().getResourceAsStream(xmlName);
        // 创建TestNG对象
        TestNG tng = new TestNG();
        // 创建SuiteXmlParser对象
        SuiteXmlParser xmlParser = new SuiteXmlParser();
        // 创建List<XmlSuite>对象
        List<XmlSuite> suites = new ArrayList<>();
        // 解析xml文件
        XmlSuite xmlSuite = xmlParser.parse(xmlName, inputStream, true);
        // 将解析出来的xml文件添加到List<XmlSuite>中
        suites.add(xmlSuite);
        // 创建Map<String, String>对象
        Map<String, String> params = new HashMap<>();
        // 传入参数
        params.put("param", param);
        // 设置参数
        xmlSuite.setParameters(params);
        // 设置执行xml文件
        tng.setXmlSuites(suites);
        // 执行xml文件
        tng.run();

    }
}
