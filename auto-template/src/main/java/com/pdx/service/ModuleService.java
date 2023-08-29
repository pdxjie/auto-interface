package com.pdx.service;

import com.pdx.entity.Module;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pdx.response.Result;

/**
 * <p>
 * 项目模块 服务类
 * </p>
 *
 * @author 派同学
 * @since 2023-08-27
 */
public interface ModuleService extends IService<Module> {

    Result<?> getModulesByItemId(String itemId);

    Result<?> insertModule(Module module);

    Result<?> updateModule(Module module);

    Result<?> getModuleById(String id);

    Result<?> removeModuleById(String id);
}
