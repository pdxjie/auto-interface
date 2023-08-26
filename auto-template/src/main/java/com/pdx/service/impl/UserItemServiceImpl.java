package com.pdx.service.impl;

import com.pdx.entity.UserItem;
import com.pdx.mapper.UserItemMapper;
import com.pdx.service.UserItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
@Service
public class UserItemServiceImpl extends ServiceImpl<UserItemMapper, UserItem> implements UserItemService {

}
