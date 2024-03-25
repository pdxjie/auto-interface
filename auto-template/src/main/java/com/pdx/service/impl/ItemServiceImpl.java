package com.pdx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.entity.*;
import com.pdx.mapper.*;
import com.pdx.modal.dto.ItemDto;
import com.pdx.modal.vo.QueryItemVo;
import com.pdx.response.Result;
import com.pdx.service.ItemService;
import com.pdx.utils.JwtUtil;
import com.pdx.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private ModuleCaseMapper moduleCaseMapper;

    @Resource
    private TestCaseMapper caseMapper;

    @Resource
    private LikeItemMapper likeItemMapper;

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
            List<TestCase> cases = baseMapper.selectTestCaseByItemId(itemDto.getId());
            // 项目状态
            List<TestCase> unStart = cases.stream().filter(testCase -> testCase.getStatus() == 1).collect(Collectors.toList());
            List<TestCase> ending = cases.stream().filter(testCase -> testCase.getStatus() == 3).collect(Collectors.toList());
            if (unStart.size() == cases.size()) {
                itemDto.setStatus(1);
            } else if (ending.size() == cases.size()) {
                itemDto.setStatus(3);
            } else {
                itemDto.setStatus(2);
            }
            User userOne = userMapper.selectById(itemDto.getUserId());
            itemDto.setNickName(userOne.getNickName());
            double v = 0;
            if (ending.isEmpty()) {
                v = 0;
            } else {
                // 进度
                double percent = ending.size() / (double) cases.size();
                DecimalFormat df = new DecimalFormat("#.##");
                String format = df.format(percent);
                Double count = Double.valueOf(format);
                 v = count * 100;
            }

            itemDto.setNickName(user.getNickName());
            itemDto.setPercent(v);
        });
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

    /**
     * 克隆项目
     * @param itemId
     * @return
     */
    @Override
    public Result<?> cloneItem(String itemId) {
        String assessToken = securityUtil.getPrincipal();
        String userId = jwtUtil.getUserIdByJwtToken(assessToken);
        // 查询出当前的项目
        Item item = getById(itemId);
        String newId = UUID.randomUUID().toString();
        item.setId(newId);
        item.setUpdateTime(new Date());
        item.setCreateTime(new Date());
        item.setName(item.getName() + "-副本");
        // 查询出当前项目对应的用例
        Map<String, String> caseMap = new HashMap<>();
        List<TestCase> testCases = baseMapper.selectTestCaseByItemId(itemId);
        testCases.forEach(testCase -> {
            String caseId = UUID.randomUUID().toString();
            caseMap.put(testCase.getId(), caseId);
            testCase.setId(caseId);
            testCase.setStatus(1);
            testCase.setCreateTime(new Date());
            testCase.setUpdateTime(new Date());
        });
        // 查询出当前项目对应的模块
        Map<String, String> moduleMap = new HashMap<>();
        List<Module> modules = moduleMapper.selectModulesByItemId(itemId);
        modules.forEach(module -> {
            String moduleId = UUID.randomUUID().toString();
            moduleMap.put(module.getId(), moduleId);
            module.setId(moduleId);
            module.setCreateTime(new Date());
            module.setUpdateTime(new Date());
        });
        List<ModuleItem> moduleItems = moduleItemMapper.selectList(new QueryWrapper<ModuleItem>().eq("item_id", itemId));
        Map<String, String> moduleItemMap = new HashMap<>();
        moduleItems.forEach(moduleItem -> {
            String newModuleId = moduleMap.get(moduleItem.getModuleId());
            String moduleItemId = UUID.randomUUID().toString();
            moduleItemMap.put(moduleItem.getId(), moduleItemId);
            moduleItem.setId(moduleItemId);
            moduleItem.setItemId(newId);
            moduleItem.setModuleId(newModuleId);
            moduleItem.setCreateTime(new Date());
            moduleItem.setUpdateTime(new Date());
        });
        // 查询出当前项目对应的模块用例关联
        List<ModuleCase> moduleCases = moduleCaseMapper.selectList(new QueryWrapper<ModuleCase>().in("module_id", moduleMap.keySet()));
        moduleCases.forEach(moduleCase -> {
            String newModuleCaseId = UUID.randomUUID().toString();
            String newCaseId = caseMap.get(moduleCase.getCaseId());
            String newModuleId = moduleMap.get(moduleCase.getModuleId());
            moduleCase.setId(newModuleCaseId);
            moduleCase.setCaseId(newCaseId);
            moduleCase.setModuleId(newModuleId);
            moduleCase.setCreateTime(new Date());
            moduleCase.setUpdateTime(new Date());
        });
        // 插入新的项目
        save(item);
        // 插入新的用户项目关联
        UserItem userItem = new UserItem();
        userItem.setId(UUID.randomUUID().toString());
        userItem.setItemId(newId);
        userItem.setUserId(userId);
        userItem.setIdentity(1);
        userItem.setCreateTime(new Date());
        userItem.setUpdateTime(new Date());
        userItemMapper.insert(userItem);
        // 插入新的模块
        modules.forEach(module -> moduleMapper.insert(module));
        // 插入新的模块项目关联
        moduleItems.forEach(moduleItem -> moduleItemMapper.insert(moduleItem));
        // 插入新的模块用例关联
        moduleCases.forEach(moduleCase -> moduleCaseMapper.insert(moduleCase));
        // 插入新的用例
        testCases.forEach(testCase -> caseMapper.insert(testCase));
        return Result.success();
    }

    /**
     * 公共项目广场
     * @param vo
     * @return
     */
    @Override
    public Result<?> publicItem(QueryItemVo vo) {
        String principal = securityUtil.getPrincipal();
        String userId = jwtUtil.getUserIdByJwtToken(principal);
        Page<Item> page = new Page<>(vo.getCurrent(), vo.getPageSize());
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 2);
        if (StringUtils.isNotEmpty(vo.getName())) {
            queryWrapper.like("name", vo.getName());
        }
        Page<Item> itemPage = page(page, queryWrapper);
        List<ItemDto> itemDtos = new ArrayList<>();
        itemPage.getRecords().forEach(item -> {
            ItemDto itemDto = new ItemDto();
            BeanUtils.copyProperties(item, itemDto);
            UserItem itemId = userItemMapper.selectOne(new QueryWrapper<UserItem>().eq("item_id", item.getId()));
            User user = userMapper.selectById(itemId.getUserId());
            itemDto.setNickName(user.getNickName());
            itemDto.setUserId(user.getId());
            itemDto.setAvatar(user.getAvatar());
            // 是否点赞
            Integer count = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("user_id", userId).eq("type", 1));
            itemDto.setLike(count > 0);
            // 是否收藏
            Integer collectCount = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("user_id", userId).eq("type", 2));
            itemDto.setCollect(collectCount > 0);
            // 点赞数
            Integer likeCount = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("type", 1));
            itemDto.setLikeCount(likeCount);
            // 收藏数
            Integer collectCount1 = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("type", 2));
            itemDto.setCollectCount(collectCount1);
            itemDtos.add(itemDto);
        });
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("items", itemDtos);
        resultMap.put("total", itemPage.getTotal());
        return Result.success(resultMap);
    }

    @Override
    public Result<?> likeItem(QueryItemVo vo) {
        String principal = securityUtil.getPrincipal();
        String userId = jwtUtil.getUserIdByJwtToken(principal);
        List<LikeItem> likeItems = likeItemMapper.selectList(new QueryWrapper<LikeItem>().eq("user_id", userId).eq("type", 1));
        List<String> itemIds = likeItems.stream().map(LikeItem::getItemId).collect(Collectors.toList());
        Page<Item> page = new Page<>(vo.getCurrent(), vo.getPageSize());
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        Map<String, Object> resultMap = new HashMap<>();
        List<ItemDto> itemDtos = new ArrayList<>();
        if (StringUtils.isNotEmpty(vo.getName())) {
            queryWrapper.like("name", vo.getName());
        }
        if (!itemIds.isEmpty()) {
            queryWrapper.in("id", itemIds);
        } else {
            resultMap.put("items", itemDtos);
            resultMap.put("total", itemDtos.size());
            return Result.success(resultMap);
        }
        Page<Item> itemPage = page(page, queryWrapper);
        itemPage.getRecords().forEach(item -> {
            ItemDto itemDto = new ItemDto();
            BeanUtils.copyProperties(item, itemDto);
            UserItem itemId = userItemMapper.selectOne(new QueryWrapper<UserItem>().eq("item_id", item.getId()));
            User user = userMapper.selectById(itemId.getUserId());
            itemDto.setNickName(user.getNickName());
            itemDto.setUserId(user.getId());
            itemDto.setAvatar(user.getAvatar());
            // 是否点赞
            Integer count = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("user_id", userId).eq("type", 1));
            itemDto.setLike(count > 0);
            // 是否收藏
            Integer collectCount = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("user_id", userId).eq("type", 2));
            itemDto.setCollect(collectCount > 0);
            // 点赞数
            Integer likeCount = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("type", 1));
            itemDto.setLikeCount(likeCount);
            // 收藏数
            Integer collectCount1 = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("type", 2));
            itemDto.setCollectCount(collectCount1);
            itemDtos.add(itemDto);
        });
        resultMap.put("items", itemDtos);
        resultMap.put("total", itemPage.getTotal());
        return Result.success(resultMap);
    }

    @Override
    public Result<?> collectItem(QueryItemVo vo) {
        String principal = securityUtil.getPrincipal();
        String userId = jwtUtil.getUserIdByJwtToken(principal);
        List<LikeItem> likeItems = likeItemMapper.selectList(new QueryWrapper<LikeItem>().eq("user_id", userId).eq("type", 2));
        List<String> itemIds = likeItems.stream().map(LikeItem::getItemId).collect(Collectors.toList());
        Page<Item> page = new Page<>(vo.getCurrent(), vo.getPageSize());
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        Map<String, Object> resultMap = new HashMap<>();
        List<ItemDto> itemDtos = new ArrayList<>();
        if (StringUtils.isNotEmpty(vo.getName())) {
            queryWrapper.like("name", vo.getName());
        }
        if (!itemIds.isEmpty()) {
            queryWrapper.in("id", itemIds);
        } else {
            resultMap.put("items", itemDtos);
            resultMap.put("total", itemDtos.size());
            return Result.success(resultMap);
        }
        Page<Item> itemPage = page(page, queryWrapper);
        itemPage.getRecords().forEach(item -> {
            ItemDto itemDto = new ItemDto();
            BeanUtils.copyProperties(item, itemDto);
            UserItem itemId = userItemMapper.selectOne(new QueryWrapper<UserItem>().eq("item_id", item.getId()));
            User user = userMapper.selectById(itemId.getUserId());
            itemDto.setNickName(user.getNickName());
            itemDto.setUserId(user.getId());
            itemDto.setAvatar(user.getAvatar());
            // 是否点赞
            Integer count = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("user_id", userId).eq("type", 1));
            itemDto.setLike(count > 0);
            // 是否收藏
            Integer collectCount = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("user_id", userId).eq("type", 2));
            itemDto.setCollect(collectCount > 0);
            // 点赞数
            Integer likeCount = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("type", 1));
            itemDto.setLikeCount(likeCount);
            // 收藏数
            Integer collectCount1 = likeItemMapper.selectCount(new QueryWrapper<LikeItem>().eq("item_id", item.getId()).eq("type", 2));
            itemDto.setCollectCount(collectCount1);
            itemDtos.add(itemDto);
        });
        resultMap.put("items", itemDtos);
        resultMap.put("total", itemPage.getTotal());
        return Result.success(resultMap);
    }
}
