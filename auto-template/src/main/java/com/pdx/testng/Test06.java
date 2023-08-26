package com.pdx.testng;

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
 * @Description TODO
 * @Date 2023/8/26
 **/
public class Test06 {
    public static void main(String[] args) {
        String xmlName = "testng.xml";
        InputStream inputStream = Test06.class.getClassLoader().getResourceAsStream(xmlName);
        TestNG testNG = new TestNG();
        SuiteXmlParser suiteXmlParser = new SuiteXmlParser();
        List<XmlSuite> xmlSuites = new ArrayList<>();
        XmlSuite xmlSuite = suiteXmlParser.parse(xmlName, inputStream, true);
        xmlSuites.add(xmlSuite);
        Map<String, String> paramters = new HashMap<>();
        paramters.put("sql", "qiqi");
        xmlSuite.setParameters(paramters);
        testNG.setXmlSuites(xmlSuites);
        testNG.run();
    }
}
