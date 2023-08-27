package com.pdx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 项目模块
 * </p>
 *
 * @author 派同学
 * @since 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("auto_module")
@ApiModel(value="Module对象", description="项目模块")
public class Module implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    private Integer sort;

    private Date createTime;

    private Date updateTime;


}
