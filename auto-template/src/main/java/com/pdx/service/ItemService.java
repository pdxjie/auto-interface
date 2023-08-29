package com.pdx.service;

import com.pdx.entity.Item;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pdx.modal.vo.QueryItemVo;
import com.pdx.response.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
public interface ItemService extends IService<Item> {

    /**
     * 条件分页查询项目
     *
     * @param vo
     * @return
     */
    Result<?> homePages(QueryItemVo vo);

    /**
     * 添加项目
     *
     * @param item
     * @return
     */
    Result<?> insertItem(Item item);

    Result<?> updateItem(Item item);

    Result<?> selectInfo(String id);

    Result<?> removeItemById(String id);
}