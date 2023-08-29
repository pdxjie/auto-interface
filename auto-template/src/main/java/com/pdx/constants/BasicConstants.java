package com.pdx.constants;

import java.util.UUID;

/*
 * @Author 派同学
 * @Description 基础常量
 * @Date 2023/7/24
 **/
public interface BasicConstants {

    // 基础扫描路径
    public static final String BASIC_SCAN_PATH = "com.pdx";

    public static final String TOKEN = "token";

    // 邮箱前缀 key
    public static final String CAPTCHA_PREFIX = "email_code";

    // 默认头像
    public static final String DEFAULT_AVATAR = "https://pics0.baidu.com/feed/d1a20cf431adcbefdd06061abb774ad1a2cc9fe4.jpeg?token=15acde01d7585b00cccd65143b10e2eb";

    // 用户ID
    public static final String USER_ID_KEY = "userId";

    // 生产token时间
    public static final String CLAIM_DATE_KEY = "claim_date_key";

    // 默认简介
    public static final String DEFAULT_REMARK = "说些什么介绍一下自己吧~";

    // 内网IP
    public static final String DEFAULT_ADDRESS = "内网IP";
}
