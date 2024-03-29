package com.pdx.service;

import com.pdx.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pdx.modal.vo.QueryRoleVo;
import com.pdx.modal.vo.UpdateRoleVo;
import com.pdx.response.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2023-07-24
 */
public interface RoleService extends IService<Role> {

    Result<?> pages(QueryRoleVo vo);

    Result<?> insertRole(Role role);

    Result<?> updateRole(UpdateRoleVo vo);

    Result<?> getRoleInfo(String id);

    Result<?> changeStatus(String roleId);
}
