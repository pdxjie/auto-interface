package com.pdx.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pdx.entity.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.*;

/*
 * @Author 派同学
 * @Description 测试报告
 * @Date 2023/9/6
 **/
@Slf4j
public class GeneratorReport {

    private String param;

    @BeforeSuite
    @Parameters("param")
    public void beforeSuite(String param) {
        this.param = param;
    }

    @Test(dataProvider = "fetchParam")
    public void generatorResultReport(TestCase caseInfo) {
        JSONObject result = null;
        JSONObject requestData = JSONObject.parseObject(caseInfo.getRequestData());
        JSONObject expectData = JSONObject.parseObject(caseInfo.getExpectResponse());
        // headers
        Map<String, Object> headers = new HashMap<>();
        if (StringUtils.isNotEmpty(caseInfo.getHeaders())) {
            JSONArray jsonArray = JSONObject.parseArray(caseInfo.getHeaders());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                headers.put(jsonObject.getString("paramsKey"), jsonObject.getString("paramsVal"));
            }
        }
        switch (caseInfo.getRequestType()) {
            // GET 请求
            case 1:
                result = HttpUtils.getJson(caseInfo.getRequestUrl(), headers);
                break;
            // POST 请求
            case 2:
                result = HttpUtils.postJson(caseInfo.getRequestUrl(), requestData, headers);
                break;
            // PUT 请求
            case 3:
                result = HttpUtils.putJson(caseInfo.getRequestUrl(), requestData, headers);
                break;
            // DELETE 请求
            case 4:
                result = HttpUtils.deleteJson(caseInfo.getRequestUrl(), headers);
                break;
        }
        boolean success = HttpUtils.isSuccess(expectData, result);
        // 断言
        assert result != null;
        Assert.assertTrue(success, result.toJSONString());
    }

    @DataProvider(name = "fetchParam")
    public Iterator<Object[]> fetchLinks() throws Exception{
        List<TestCase> cases = new ArrayList<>();
        // 将参数转为 用例集合
        JSONArray caseArray = JSONObject.parseArray(this.param);
        log.info("param ---> {}", this.param);
        log.info("caseArray ---> {}", caseArray);
        caseArray.forEach(caseJson -> {
            TestCase testCase = JSONUtil.toBean(caseJson.toString(), TestCase.class);
            cases.add(testCase);
        });

        // 将用例集合转换到 Object 里
        List<Object[]> links = new ArrayList<>();
        cases.forEach(caseInfo -> {
            links.add(new Object[]{caseInfo});
        });
        return links.iterator();
    }
}
