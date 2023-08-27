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
 * 
 * </p>
 *
 * @author 派同学
 * @since 2023-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("auto_module_case")
@ApiModel(value="ModuleCase对象", description="")
public class ModuleCase implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String moduleId;

    private String caseId;

    private Date createTime;

    private Date updateTime;


}
