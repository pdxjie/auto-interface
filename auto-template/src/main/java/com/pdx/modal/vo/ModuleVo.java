package com.pdx.modal.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 项目模块
 * </p>
 *
 * @author 派同学
 * @since 2023-08-27
 */
@Data
public class ModuleVo implements Serializable {

    @ApiModelProperty(value = "模块名称")
    private String name;

    @ApiModelProperty(value = "模块排序")
    private Integer sort;

    @ApiModelProperty(value = "父级ID")
    private String parentId;

    @ApiModelProperty("项目ID")
    private String itemId;
}
