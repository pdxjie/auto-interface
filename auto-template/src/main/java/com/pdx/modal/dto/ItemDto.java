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

    @ApiModelProperty(value = "自动化测试进度")
    private double percent;

    @ApiModelProperty(value = "状态 1 未开始 2 进行中 3 已结束")
    private Integer status;

    @ApiModelProperty(value = "类型 公共 OR 私有")
    private Integer type;

    @ApiModelProperty(value = "是否点赞")
    private boolean isLike;

    @ApiModelProperty(value = "是否收藏")
    private boolean isCollect;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "收藏数")
    private Integer collectCount;

    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
