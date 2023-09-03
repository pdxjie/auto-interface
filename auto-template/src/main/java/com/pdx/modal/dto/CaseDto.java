package com.pdx.modal.dto;

import com.pdx.entity.TestCase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * @Author 派同学
 * @Description TODO
 * @Date 2023/8/31
 **/
@Data
public class CaseDto extends TestCase {

    @ApiModelProperty(value = "所属模块")
    private String moduleName;
}
