package com.pdx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("auto_test_case")
@ApiModel(value="TestCase对象", description="")
public class TestCase implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "用例名称")
    private String name;

    @ApiModelProperty(value = "业务场景描述")
    private String businessDesc;

    @ApiModelProperty(value = "请求数据")
    private String requestData;

    @ApiModelProperty(value = "请求地址")
    private String requestUrl;

    @ApiModelProperty(value = "响应断言")
    private String expectResponse;

    @ApiModelProperty(value = "用例等级")
    private Integer caseRank;

    @ApiModelProperty(value = "运行状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
