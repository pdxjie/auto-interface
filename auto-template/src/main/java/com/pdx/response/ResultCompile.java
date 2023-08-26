package com.pdx.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * @Author 派同学
 * @Description 编译响应体
 * @Date 2023/8/20
 **/
@Data
public class ResultCompile {

    @ApiModelProperty(value = "编译错误类型 0 正确 1 编译出错 2 运行出错")
    private int error;

    @ApiModelProperty(value = "具体的出错原因")
    private String reason;

    @ApiModelProperty(value = "输出内容")
    private String stdout;

    @ApiModelProperty(value = "错误内容")
    private String stderr;

}
