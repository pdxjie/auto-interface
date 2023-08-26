package com.pdx.testng;

import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * @Author 派同学
 * @Description 断言
 * @Date 2023/8/26
 **/
public class Test03 {

    @Test
    public void test01 () {
        Assert.assertEquals(1, 1);
    }

    @Test
    public void test02 () {
        Assert.assertEquals(1, 12);
    }

    @Test
    public void test03 () {
        Assert.assertEquals(1, 12, "与期望不符合");
    }

    @Test
    public void test04 () {
        Assert.assertTrue(true);
    }

    @Test
    public void test05 () {
        Assert.assertTrue(false, "期望值为true");
    }

    @Test
    public void test06 () {
        Assert.fail( "失败了");
    }

    @Test
    public void test07 () {
        String s = "null";
        Assert.assertNull(s, "断言失败");
    }
}
