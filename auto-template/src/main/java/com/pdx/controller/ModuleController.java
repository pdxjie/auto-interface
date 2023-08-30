package com.pdx.controller;


import com.pdx.entity.Module;
import com.pdx.modal.vo.ModuleVo;
import com.pdx.response.Result;
import com.pdx.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * <p>
 * 项目模块 前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2023-08-27
 */
@Api(tags = "项目模块接口")
@RestController
@RequestMapping("/pdx/module")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @ApiOperation(value = "根据项目ID获取项目模块列表")
    @GetMapping("/modules")
    public Result<?> getModulesByItemId (@PathParam("itemId") String itemId) {
        return moduleService.getModulesByItemId(itemId);
    }

    @ApiOperation(value = "创建模块, 只允许存在两级")
    @PostMapping("/insert")
    public Result<?> insertModule (@RequestBody ModuleVo module) {
        return moduleService.insertModule(module);
    }

    @ApiOperation(value = "修改模块")
    @PostMapping("/update")
    public Result<?> updateModule (@RequestBody Module module) {
        return moduleService.updateModule(module);
    }

    @ApiOperation(value = "获取模块信息")
    @GetMapping("/{id}")
    public Result<?> moduleInfo (@PathVariable("id") String id) {
        return moduleService.getModuleById(id);
    }

    @ApiOperation(value = "删除模块")
    @DeleteMapping("/{id}")
    public Result<?> removeModuleById (@PathVariable("id") String id) {
        return moduleService.removeModuleById(id);
    }
}

