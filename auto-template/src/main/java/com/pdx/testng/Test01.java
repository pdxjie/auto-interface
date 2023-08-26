package com.pdx.testng;

import org.testng.Assert;
import org.testng.annotations.*;

/*
 * @Author 派同学
 * @Description 注解
 * @Date 2023/8/26
 **/
public class Test01 {

    @BeforeClass
    public void beforeClass () {
        System.out.println("class 运行前执行...");
    }

    @BeforeTest
    public void beforeTest () {
        System.out.println("test 运行前执行...");
    }

    @AfterTest
    public void afterTest () {
        System.out.println("test 运行后执行...");
    }

    @AfterClass
    public void afterClass () {
        System.out.println("class 运行后执行...");
    }

    @AfterMethod
    public void afterMethod () {
        System.out.println("method 运行后执行...");
    }

    @BeforeMethod
    public void beforeMethod () {
        System.out.println("method 运行前执行...");
    }

    @Test
    public void test01 () {
        int i = 0;
        Assert.assertEquals(0 , i);
    }

    @Test(groups = "测试组1")
    public void test02 () {
        int i = 0;
        Assert.assertEquals(0 , i);
    }

    @BeforeGroups
    public void beforeGroup () {
        System.out.println("groups 运行前执行...");
    }

    @AfterGroups
    public void afterGroup () {
        System.out.println("groups 运行前执行...");
    }
}
