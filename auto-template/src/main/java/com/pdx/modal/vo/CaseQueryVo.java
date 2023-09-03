package com.pdx.modal.vo;

import com.pdx.utils.PageParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * @Author 派同学
 * @Description TODO
 * @Date 2023/8/31
 **/
@Data
public class CaseQueryVo extends PageParams {

    @ApiModelProperty(value = "模块ID")
    private String moduleId;

    @ApiModelProperty(value = "用例名称")
    private String caseName;
}
