package com.pdx.modal.common;

import com.pdx.AutoApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;

/*
 * @Author 派同学
 * @Description TODO
 * @Date 2023/8/26
 **/
@Slf4j
@WebAppConfiguration
@SpringBootTest(classes = AutoApplication.class)
public class BaseCaseModal extends AbstractTestNGSpringContextTests {
}
