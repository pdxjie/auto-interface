package com.pdx.testng;

import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * @Author 派同学
 * @Description @Test注解属性
 * @Date 2023/8/26
 **/
public class Test02 {

    @Test(enabled = false)
    public void test01 () {
        System.out.println("方法1");
        Assert.assertEquals(1, 1);
    }

    @Test
    public void test02 () {
        System.out.println("方法2");
        Assert.assertEquals(1, 1);
    }

    @Test(description = "用例描述")
    public void test03 () {
        System.out.println("方法3");
        Assert.assertEquals(1, 1);
    }

    @Test(groups = "测试组01")
    public void test04 () {
        System.out.println("方法4");
        Assert.assertEquals(1, 1);
    }

    @Test(groups = "测试组01")
    public void test05 () {
        System.out.println("方法5");
        Assert.assertEquals(1, 1);
    }

    // 依赖的所有组的用例都得执行成功，如果所依赖的组里的用例失败的，那么该条就会被忽略
    @Test(dependsOnGroups = "测试组01")
    public void test06 () {
        System.out.println("方法6");
        Assert.assertEquals(1, 1);
    }
}
