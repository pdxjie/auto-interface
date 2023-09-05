package com.pdx.modal.vo;

import com.pdx.entity.TestCase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * @Author 派同学
 * @Description TODO
 * @Date 2023/8/31
 **/
@Data
public class CaseInsertVo extends TestCase {

    @ApiModelProperty(value = "模块ID")
    private String moduleId;

    @ApiModelProperty(value = "请求头")
    private String headerMap;

}
