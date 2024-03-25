package com.pdx.mapper;

import com.pdx.entity.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pdx.entity.TestCase;
import com.pdx.modal.dto.ItemDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
public interface ItemMapper extends BaseMapper<Item> {

    /**
     * 条件分页查询
     *
     * @param userId
     * @param name
     * @param startPage
     * @param pageSize
     * @return
     */
    List<ItemDto> queryItemByCondition(@Param("userId") String userId, @Param("name")String name, @Param("startPage")Integer startPage, @Param("size")Integer pageSize);

    /**
     * 查询总数
     *
     * @param userId
     * @return
     */
    Integer queryItemTotalCount(String userId);

    /**
     * 查询项目下的用例详情
     *
     * @param id
     * @return
     */
    List<TestCase> selectTestCaseByItemId(@Param("itemId") String id);
}
