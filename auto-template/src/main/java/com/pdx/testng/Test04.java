package com.pdx.testng;

import cn.hutool.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * @Author 派同学
 * @Description 手写断言
 * @Date 2023/8/26
 **/
public class Test04 {

    @Test
    public void test01 () {
        JSONObject j1 = new JSONObject();
        JSONObject j2 = new JSONObject();

        j1.put("status", "success");
        j1.put("code", "0000");
        j1.put("msg", "成功");

        j2.put("status", "success");
        j2.put("code", "0000");
        j2.put("msg", "成功");
        j2.put("transNo", "1234");
        j2.put("submit", "5667575");
        Assert.assertTrue(true);
    }
}
