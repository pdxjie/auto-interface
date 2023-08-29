package com.pdx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.entity.*;
import com.pdx.mapper.*;
import com.pdx.modal.dto.ItemDto;
import com.pdx.modal.vo.QueryItemVo;
import com.pdx.response.Result;
import com.pdx.service.ItemService;
import com.pdx.utils.JwtUtil;
import com.pdx.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Resource
    private UserItemMapper userItemMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private SecurityUtil securityUtil;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ModuleItemMapper moduleItemMapper;

    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 条件分页查询项目
     *
     * @param vo
     * @return
     */
    @Override
    public Result<?> homePages(QueryItemVo vo) {
        // 获取当前用户
        String accessToken = securityUtil.getPrincipal();
        String userId = jwtUtil.getUserIdByJwtToken(accessToken);

        // 分页数据
        Integer startPage = (vo.getCurrent() - 1) * vo.getPageSize();
        // SQL 查询
        List<ItemDto> itemDtos = baseMapper.queryItemByCondition(userId, vo.getName(), startPage, vo.getPageSize());
        itemDtos.forEach(itemDto -> {
            User user = userMapper.selectById(itemDto.getUserId());
            if (itemDto.getIdentity() == 1 && itemDto.getUserId().equals(userId)) {
                itemDto.setItemOwner(true);
            }
            itemDto.setNickName(user.getNickName());
            itemDto.setPercent(0);
        });
        // TODO 后续封装 自动化测试进度 以及 项目的状态 未开始 进行中 已结束
        // 查询总数
        Integer count = baseMapper.queryItemTotalCount(userId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("items", itemDtos);
        resultMap.put("total", count);
        return Result.success(resultMap);
    }

    /**
     * 添加项目
     *
     * @param item
     * @return
     */
    @Override
    @Transactional
    public Result<?> insertItem(Item item) {
        // 获取当前用户
        String accessToken = securityUtil.getPrincipal();
        String userId = jwtUtil.getUserIdByJwtToken(accessToken);
        // 添加 Item
        String itemId = UUID.randomUUID().toString();
        item.setId(itemId);
        item.setUpdateTime(new Date());
        boolean save = save(item);
        // 添加 UserItem
        UserItem userItem = new UserItem();
        userItem.setId(UUID.randomUUID().toString());
        userItem.setItemId(itemId);
        userItem.setUserId(userId);
        userItem.setIdentity(1);
        userItem.setCreateTime(new Date());
        userItem.setUpdateTime(new Date());
        // 默认创建一个公共模块
        ModuleItem moduleItem = new ModuleItem();
        moduleItem.setId(UUID.randomUUID().toString());
        moduleItem.setItemId(itemId);
        moduleItem.setModuleId(itemId);
        moduleItem.setCreateTime(new Date());
        moduleItem.setUpdateTime(new Date());
        moduleItemMapper.insert(moduleItem);
        // 创建一个模块
        Module module = new Module();
        module.setId(itemId);
        module.setName("公共模块");
        module.setParentId("0");
        module.setCreateTime(new Date());
        module.setUpdateTime(new Date());
        moduleMapper.insert(module);
        int result = userItemMapper.insert(userItem);
        return (save && result > 0) ? Result.success() : Result.fail();
    }

    /**
     * 更新项目信息
     * @param item
     * @return
     */
    @Override
    public Result<?> updateItem(Item item) {
        item.setUpdateTime(new Date());
        boolean result = updateById(item);
        return result ? Result.success() : Result.fail();
    }

    /**
     * 获取信息
     * @param id
     * @return
     */
    @Override
    public Result<?> selectInfo(String id) {
        Item itemInfo = getById(id);
        return Result.success(itemInfo);
    }

    /**
     * 删除项目信息
     * @param id
     * @return
     */
    @Override
    public Result<?> removeItemById(String id) {
        boolean result = removeById(id);
        return result ? Result.success() : Result.fail();
    }
}
