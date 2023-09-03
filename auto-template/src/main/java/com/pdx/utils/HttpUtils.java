package com.pdx.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/*
 * @Author 派同学
 * @Description 封装http请求
 * @Date 2023/9/3
 **/
public class HttpUtils {


    /**
     * 发送 POST 请求
     * @param url 请求地址
     * @param jsonObject 请求参数
     */
    public static JSONObject postJson (String url, JSONObject jsonObject) {
        // 创建HttpClient对象
        HttpClient httpClient = HttpClientBuilder.create().build();
        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        // 定义 JSON 参数
        String secretKey = "auto_lab";
        // 设置参数
        httpPost.setEntity(new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8));
        httpPost.setHeader("Content-Type", "application/json");
        try {
            // 发送 POST 请求
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // 响应内容
            String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            return JSONObject.parseObject(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 校验接口的返回结果与预期结果是否一致
     * @return
     */
    public static boolean isSuccess (JSONObject expectData, JSONObject realResult) {
        if (expectData == null || realResult == null) {
            return false;
        }
        if (expectData.size() != realResult.size()) {
            return false;
        }
        // 期望值的 code 响应码
        String expectSuccessCode = expectData.getString("code");
        // 实际值的 code 响应码
        String realSuccessCode = realResult.getString("code");
        // 判断响应码是否一致 如果不一致直接返回false
        if (expectSuccessCode.equals(realSuccessCode)) {
            return recursiveComparison(expectData, realResult);
        }
        return false;
    }

    /**
     * 递归比较两个 JSONObject 对象是否相等
     * @param expect 期望值
     * @param result 实际值
     * @return true or false
     */
    public static boolean recursiveComparison (JSONObject expect, JSONObject result) {

        // 判断相等和null
        if (expect == result || expect == null || result == null) {
            return expect == result;
        }
        // 获得键集合
        Set<String> keys = expect.keySet();
        // 递归比较键值
        for (String key : keys) {
            Object expectVal = expect.get(key);
            Object resultVal = result.get(key);
            if (expectVal == null && resultVal != null || expectVal != null && resultVal == null) {
                return false;
            }
            if (expectVal instanceof JSONObject) {
                if (!recursiveComparison((JSONObject)expectVal, (JSONObject)resultVal)) {
                    return false;
                }
            // 如果是字符串，无需比较两个变量的值是否相等，只需比较两者的类型是否相等即可
            } else if (expectVal instanceof String) {
                if (!expectVal.getClass().equals(resultVal.getClass())) {
                    return false;
                }
            }
        }
        return true;
    }

}
