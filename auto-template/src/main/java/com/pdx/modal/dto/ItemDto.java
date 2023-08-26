package com.pdx.modal.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 派同学
 * @since 2023-08-26
 */
@Data
public class ItemDto implements Serializable {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "图标")
    private String cover;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "项目描述")
    private String description;

    @ApiModelProperty(value = "权限 1 公共 2 私有")
    private Integer authority;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "截止时间")
    private Date dateLine;

    @ApiModelProperty(value = "负责人")
    private String userId;

    @ApiModelProperty(value = "负责人")
    private String nickName;

    @ApiModelProperty(value = "是否是项目负责人")
    private boolean isItemOwner;

    @ApiModelProperty(value = "身份")
    private Integer identity;
}
