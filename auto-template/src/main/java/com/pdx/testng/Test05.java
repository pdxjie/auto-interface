package com.pdx.testng;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
 * @Author 派同学
 * @Description 参数 @Paramters
 * @Date 2023/8/26
 **/
public class Test05 {


//    @Test
//    @Parameters({"name", "password"})
//    public void test01 (@Optional("zhangsan") String name, @Optional("123") String password) {
//        System.out.println(name + password);
//        Assert.assertEquals(1, 1);
//    }

    @Test(dataProvider = "getNames")
    public void test01 (String name, String password) {
        Assert.assertEquals(1, 1);
    }

    @DataProvider(name = "getNames")
    public Object[][] test02 () {
        return new Object[][]{
                {"zhangsan", "123"},
                {"lisi", "123"}
        };
    }
}
