package com.pdx.controller;


import com.pdx.entity.Item;
import com.pdx.modal.vo.QueryItemVo;
import com.pdx.response.Result;
import com.pdx.service.ItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
@RestController
@RequestMapping("/pdx/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "前台分页查询项目")
    @PostMapping("/homePages")
    public Result<?> homePages (@RequestBody QueryItemVo vo) {
        return itemService.homePages(vo);
    }

    @ApiOperation(value = "添加项目")
    @PostMapping("/insert")
    public Result<?> insertItem (@RequestBody Item item) {
        return itemService.insertItem(item);
    }

    @ApiOperation(value = "更新项目信息")
    @PostMapping("/update")
    public Result<?> updateItem (@RequestBody Item item) {
        return itemService.updateItem(item);
    }

    @ApiOperation(value = "获取项目信息")
    @GetMapping("/{id}")
    public Result<?> itemInfo (@PathVariable("id") String id) {
        return itemService.selectInfo(id);
    }

    @ApiOperation(value = "删除项目信息")
    @DeleteMapping("/remove/{id}")
    public Result<?> removeItemById (@PathVariable("id") String id) {
        return itemService.removeItemById(id);
    }

    @ApiOperation(value = "克隆项目")
    @PostMapping("/clone")
    public Result<?> cloneItem() {
        // TODO 复制项目
        return null;
    }
}

