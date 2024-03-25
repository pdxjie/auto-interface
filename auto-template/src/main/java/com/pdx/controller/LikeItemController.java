package com.pdx.controller;

import com.pdx.modal.vo.likeVo;
import com.pdx.response.Result;
import com.pdx.service.LikeItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: IT 派同学
 * @DateTime: 2024/3/25
 * @Description:
 */
@RestController
@RequestMapping("/like")
public class LikeItemController {

    @Autowired
    private LikeItemService likeItemService;

    // 点赞
    @ApiOperation(value = "点赞/收藏")
    @PostMapping("/like")
    public Result<?> like(@RequestBody likeVo likeVo){
        return likeItemService.like(likeVo);
    }

    // 取消点赞或收藏
    @ApiOperation(value = "取消点赞/收藏")
    @PostMapping("/unlike")
    public Result<?> unlike(@RequestBody likeVo likeVo){
        return likeItemService.unlike(likeVo);
    }
}
