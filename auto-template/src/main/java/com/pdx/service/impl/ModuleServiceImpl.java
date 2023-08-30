package com.pdx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.entity.Module;
import com.pdx.entity.ModuleCase;
import com.pdx.entity.ModuleItem;
import com.pdx.exception.BusinessException;
import com.pdx.mapper.ModuleCaseMapper;
import com.pdx.mapper.ModuleItemMapper;
import com.pdx.mapper.ModuleMapper;
import com.pdx.modal.vo.ModuleVo;
import com.pdx.response.ResponseCode;
import com.pdx.response.Result;
import com.pdx.service.ModuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目模块 服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2023-08-27
 */
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module> implements ModuleService {

    @Resource
    private ModuleItemMapper moduleItemMapper;

    @Resource
    private ModuleCaseMapper moduleCaseMapper;

    /**
     * 根据项目ID获取模块列表
     * @param itemId
     * @return
     */
    @Override
    public Result<?> getModulesByItemId(String itemId) {
        QueryWrapper<ModuleItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id", itemId);
        List<String> moduleIds = moduleItemMapper.selectList(queryWrapper).stream().map(ModuleItem::getModuleId).collect(Collectors.toList());
        QueryWrapper<Module> wrapper = new QueryWrapper<>();
        // 查询出所有的模块
        List<Module> modules = baseMapper.selectBatchIds(moduleIds);
        List<Module> moduleTree = modules.stream().filter(module -> {
            return "0".equals(module.getParentId());
        }).map(module -> {
            module.setChildren(getChildren(module, modules));
            return module;
        }).sorted((m1, m2) -> {
            return (m1.getSort() == null ? 0 : m1.getSort()) - (m2.getSort() == null ? 0 : m2.getSort());
        }).collect(Collectors.toList());
        return Result.success(moduleTree);
    }

    @Override
    public Result<?> insertModule(ModuleVo moduleVo) {
        Module parentModule = null;
        if (StringUtils.isNotEmpty(moduleVo.getParentId()) && !"0".equals(moduleVo.getParentId())) {
            parentModule = getById(moduleVo.getParentId());
        }
        // 判断父模块是否为空
        if (StrUtil.isEmpty(moduleVo.getParentId())) {
            moduleVo.setParentId("0");
        }
        // 如果父模块不为空，则查询父模块的父模块的父级是否为0
        if (null != parentModule && !"0".equals(parentModule.getParentId())) {
            throw new BusinessException(ResponseCode.ERROR_PARAM);
        }
        // 判断模块名称是否重复
        QueryWrapper<Module> wrapper = new QueryWrapper<>();
        wrapper.eq("name", moduleVo.getName());
        wrapper.eq("parent_id", moduleVo.getParentId());
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ResponseCode.ERROR_PARAM);
        }
        // 创建新的 Module 对象
        Module module = new Module();
        BeanUtils.copyProperties(moduleVo, module);
        String moduleId = UUID.randomUUID().toString();
        module.setId(moduleId);
        module.setCreateTime(new Date());
        module.setUpdateTime(new Date());
        // 项目 和 模块 关联
        ModuleItem moduleItem = new ModuleItem();
        moduleItem.setId(UUID.randomUUID().toString());
        moduleItem.setModuleId(moduleId);
        moduleItem.setItemId(moduleVo.getItemId());
        moduleItem.setCreateTime(new Date());
        moduleItem.setUpdateTime(new Date());
        moduleItemMapper.insert(moduleItem);
        // 保存数据
        boolean result = save(module);
        return result ? Result.success() : Result.fail();
    }

    @Override
    public Result<?> updateModule(Module module) {
        if (StrUtil.isEmpty(module.getId())) {
            throw new BusinessException(ResponseCode.ERROR_PARAM);
        }
        module.setUpdateTime(new Date());
        boolean result = updateById(module);
        return result ? Result.success() : Result.fail();
    }

    @Override
    public Result<?> getModuleById(String id) {
        Module module = getById(id);
        return Result.success(module);
    }

    @Override
    @Transactional
    public Result<?> removeModuleById(String id) {
        // 前提：每个项目的公共模块 ID 与 项目的 ID 一致
        // 1. 查询出该模块所属项目
        ModuleItem moduleItem = moduleItemMapper.selectOne(new QueryWrapper<ModuleItem>().eq("module_id", id));
        // 2. 判断模块下的用例是否存在
        List<ModuleCase> moduleCases = moduleCaseMapper.selectList(new QueryWrapper<ModuleCase>().eq("module_id", id));
        // 如果用例为空，则直接删除即可
        if (null == moduleCases || moduleCases.size() == 0) {
            int result = baseMapper.deleteById(id);
            return result > 0 ? Result.success() : Result.fail();
        } else {
            moduleCases.forEach(moduleCase -> {
                UpdateWrapper<ModuleCase> wrapper = new UpdateWrapper<>();
                wrapper.set("module_id", moduleItem.getItemId()).eq("id", moduleCase.getId());
                moduleCaseMapper.update(moduleCase, wrapper);
            });
            int result = baseMapper.deleteById(id);
            return result > 0 ? Result.success() : Result.fail();
        }
    }

    /**
     * 封装子模块数据
     *
     * @param module
     * @param modules
     * @return
     */
    private List<Module> getChildren(Module module, List<Module> modules) {
        List<Module> children = modules.stream().filter(m -> {
            return m.getParentId().equals(module.getId());
        }).map(m -> {
            m.setChildren(getChildren(m, modules));
            return m;
        }).sorted((m1, m2) -> {
            return (m1.getSort() == null ? 0 : m1.getSort()) - (m2.getSort() == null ? 0 : m2.getSort());
        }).collect(Collectors.toList());
        return children;
    }
}
