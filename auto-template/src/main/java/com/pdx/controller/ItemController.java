package com.pdx.controller;


import com.pdx.entity.Item;
import com.pdx.modal.vo.QueryItemVo;
import com.pdx.response.Result;
import com.pdx.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.GET;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
@Api(tags = "项目模块管理")
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

    @ApiOperation(value = "公共项目广场")
    @RequestMapping(value = "/public", method = RequestMethod.POST)
    public Result<?> publicItem (@RequestBody(required = false) QueryItemVo vo) {
        return itemService.publicItem(vo);
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
    @GetMapping("/clone")
    public Result<?> cloneItem(@RequestParam("itemId") String itemId) {
        return itemService.cloneItem(itemId);
    }

    @ApiOperation(value = "点赞的项目")
    @PostMapping("/like")
    public Result<?> likeItem(@RequestBody QueryItemVo vo) {
        return itemService.likeItem(vo);
    }

    @ApiOperation(value = "收藏的项目")
    @PostMapping("/collect")
    public Result<?> collectItem(@RequestBody QueryItemVo vo) {
        return itemService.collectItem(vo);
    }
}

