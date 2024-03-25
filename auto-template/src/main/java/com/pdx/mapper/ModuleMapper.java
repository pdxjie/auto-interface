package com.pdx.mapper;

import com.pdx.entity.Module;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 项目模块 Mapper 接口
 * </p>
 *
 * @author 派同学
 * @since 2023-08-27
 */
public interface ModuleMapper extends BaseMapper<Module> {

    List<Module> selectModulesByItemId(@Param("itemId") String itemId);
}
